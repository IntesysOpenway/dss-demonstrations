/**
 * 
 */
package it.openway.orange.ds.dss.webapp.timestamp;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.io.IOUtils;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.tsp.TSPException;
import org.bouncycastle.tsp.TimeStampRequest;
import org.bouncycastle.tsp.TimeStampRequestGenerator;
import org.bouncycastle.tsp.TimeStampResponse;
import org.bouncycastle.tsp.TimeStampToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;

import eu.europa.esig.dss.DSSException;
import eu.europa.esig.dss.DigestAlgorithm;
import eu.europa.esig.dss.client.NonceSource;
import eu.europa.esig.dss.client.http.DataLoader;
import eu.europa.esig.dss.x509.tsp.TSPSource;
import it.openway.orange.ds.dss.webapp.DssWebappCommonUtils;
import it.openway.orange.ds.dss.webapp.exception.DssWebappException;

/**
 * @author marco Classe utilizzata per l'utilizzo di un servizio TSP (Timestamp Protocol, definito dallo standard RFC 3161) per applicare marche temporali
 */
@SuppressWarnings("serial")
public class DssWebappOnlineTSPSource implements TSPSource {

    private static final Logger LOGGER = LoggerFactory.getLogger(DssWebappOnlineTSPSource.class);

    /**
     * The URL of the TSP server
     */
    private String tspServer;

    /**
     * Username
     */
    private String username = null;

    /**
     * Password
     */
    private String password = null;

    /**
     * The requested policy oid
     */
    private ASN1ObjectIdentifier policyOid;

    /**
     * The data loader used to retrieve the TSP response.
     */
    private DataLoader dataLoader;

    /**
     * This variable is used to prevent the replay attack.
     */
    private NonceSource nonceSource;

    /**
     * 
     */
    public DssWebappOnlineTSPSource() {

        super();
    }

    /**
     * Metodo che effettua la richiesta al TSP e ritorna il TimeStampToken ottenuto
     * 
     * @param digest
     *        Digest del docuemento su cui applicare la marca temporale
     * @param digestAlgorithm
     *        algoritmo di digest da utilizzare per l'applicazione della marca temporale
     * @return TimeStampToken di risposta del servizio
     */
    public TimeStampToken getTimeStampResponse(DigestAlgorithm digestAlgorithm, final byte[] digest)
        throws DSSException {

        return getTimeStampResponse(digest);

    }

    /**
     * Metodo che effettua la richiesta al TSP e ritorna il TimeStampToken ottenuto
     * 
     * @param digest
     *        Digest del docuemento su cui applicare la marca temporale
     * @return TimeStampToken di risposta del servizio
     */
    public TimeStampToken getTimeStampResponse(final byte[] digest) {
        
        // NB Per aderire alle validazioni dei software di verifica(Dike, Aruba,..) viene impostato fisso
        // l'algoritmo di digest SHA256
        final DigestAlgorithm digestAlgorithm = DigestAlgorithm.SHA256;

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Timestamp server : " + tspServer + " utenza: " + username);
            LOGGER.debug("Timestamp digest algorithm: " + digestAlgorithm.getName());
            LOGGER.debug("Timestamp digest value    : " + Hex.encodeHexString(digest));
        }

        // Creazione della richiesta
        final TimeStampRequestGenerator tsqGenerator = new TimeStampRequestGenerator();
        tsqGenerator.setCertReq(true);
        if (this.policyOid != null) {
            tsqGenerator.setReqPolicy(policyOid);
        }

        ASN1ObjectIdentifier asn1ObjectIdentifier = new ASN1ObjectIdentifier(digestAlgorithm.getOid());
        TimeStampRequest timeStampRequest = null;
        if (nonceSource == null) {
            timeStampRequest = tsqGenerator.generate(asn1ObjectIdentifier, digest);
        }
        else {
            timeStampRequest = tsqGenerator.generate(asn1ObjectIdentifier, digest, nonceSource.getNonce());
        }

        byte[] encodedTimeStampRequest = null;
        try {
            encodedTimeStampRequest = timeStampRequest.getEncoded();
        }
        catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new DSSException(e);
        }

        URL tsaUrl = null;

        try {
            tsaUrl = new URL(tspServer);

        }
        catch (MalformedURLException e) {
            LOGGER.error(e.getMessage(), e);
            throw new DSSException(String.format("Errore nella creazione dell'url %s. Verificare la sintassi del tspServer configurato", tspServer), e);
        }

        byte[] encodedResponse = null;
        try {
            encodedResponse = callTSAService(tsaUrl, username, password, encodedTimeStampRequest);
        }
        catch (DssWebappException e) {
            LOGGER.error(e.getMessage(), e);
            throw new DSSException(e);
        }

        TimeStampResponse rawTimeStampResponse = null;
        try {
            rawTimeStampResponse = new TimeStampResponse(new ByteArrayInputStream(encodedResponse));
            rawTimeStampResponse.validate(timeStampRequest);
        }
        catch (TSPException | IOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new DSSException("Errore durante la validazione del timestamp generato", e);
        }

        TimeStampToken rawTimeStampToken = rawTimeStampResponse.getTimeStampToken();

        if (rawTimeStampToken != null) {
            LOGGER.info("TSP SID : SN " + rawTimeStampToken.getSID().getSerialNumber() + ", Issuer " + rawTimeStampToken.getSID().getIssuer());
        }

        return rawTimeStampToken;
    }

    /**
     * Effettua una chiamata POST sul Timestamp Authority
     * 
     * @param tsaServiceUrl
     * @param tsaUsername
     * @param tsaPassword
     * @param jsonData
     * @return
     * @throws DssWebappException
     */
    private static byte[] callTSAService(URL tsaServiceUrl, String tsaUsername, String tsaPassword, byte[] encodedTimeStampRequest)
        throws DssWebappException {

        if (encodedTimeStampRequest == null || encodedTimeStampRequest.length == 0) {
            throw new DssWebappException("La request di ingresso non e' valorizzata");
        }

        HttpClient client = new HttpClient();
        PostMethod postMethod = new PostMethod(String.valueOf(tsaServiceUrl));
        byte[] encodedResponse = null;

        try {
            postMethod.setRequestEntity(new ByteArrayRequestEntity(encodedTimeStampRequest));
            // NB Content type specifico definito dallo standard
            postMethod.setRequestHeader("Content-type", "application/timestamp-query");
            String userPassword = (!StringUtils.hasText(tsaUsername) ? "" : tsaUsername) + ":" + (!StringUtils.hasText(tsaPassword) ? "" : tsaPassword);
            String sBase64UserPassword = DssWebappCommonUtils.encodeBase64(userPassword.getBytes());
            String basicAuth = "Basic " + new String(sBase64UserPassword);
            postMethod.setRequestHeader("Authorization", basicAuth);
        }
        catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new DssWebappException(String.format("Errore nella preparazione della richiesta del TSA %s.", tsaServiceUrl), e);
        }

        InputStream isResponse = null;

        try {
            int returnCode = client.executeMethod(postMethod);

            if (returnCode != HttpStatus.OK.value()) {
                String sResponse = postMethod.getResponseBodyAsString();
                throw new DssWebappException(String.format(
                    "Errore nella chiamata del TSA %s. La risposta e' in uno stato di errore: %s", tsaServiceUrl, sResponse));
            }

            isResponse = postMethod.getResponseBodyAsStream();

            // NB : se la response non è disponibile PostMethod ritorna null senza lanciare eccezione
            if (isResponse == null) {
                throw new DssWebappException(String.format("Errore nella chiamata del TSA %s. Il servizio non e' disponibile all'url indicato", tsaServiceUrl));
            }

            try {
                encodedResponse = IOUtils.toByteArray(isResponse);
            }
            catch (IOException e) {
                LOGGER.error(e.getMessage(), e);
                throw new DssWebappException(String.format("Errore nell'elaborazione della response del TSA %s ", tsaServiceUrl), e);
            }

        }
        catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new DssWebappException(String.format("Errore nella chiamata del TSA %s ", tsaServiceUrl), e);
        }
        finally {
            postMethod.releaseConnection();
            if (isResponse != null) {
                try {
                    isResponse.close();
                }
                catch (Exception fe) {
                    LOGGER.warn(fe.getMessage());
                }
            }
        }

        return encodedResponse;

    }

    /**
     * Set the URL of the TSA
     *
     * @param tspServer
     */
    public void setTspServer(final String tspServer) {

        this.tspServer = tspServer;
    }

    /**
     * Set the request policy
     *
     * @param policyOid
     */
    public void setPolicyOid(final String policyOid) {

        this.policyOid = new ASN1ObjectIdentifier(policyOid);
    }

    /**
     * Set the DataLoader to use for querying the TSP server.
     *
     * @param dataLoader
     *        the component that allows to retrieve the TSP response using HTTP.
     */
    public void setDataLoader(final DataLoader dataLoader) {

        this.dataLoader = dataLoader;
    }

    /**
     * Set the NonceSource to use for querying the TSP server.
     *
     * @param nonceSource
     *        the component that prevents the replay attack.
     */
    public void setNonceSource(NonceSource nonceSource) {

        this.nonceSource = nonceSource;
    }

    /**
     * @return the username
     */
    public String getUsername() {

        return username;
    }

    /**
     * @param username
     *        the username to set
     */
    public void setUsername(String username) {

        this.username = username;
    }

    /**
     * @return the password
     */
    public String getPassword() {

        return password;
    }

    /**
     * @param password
     *        the password to set
     */
    public void setPassword(String password) {

        this.password = password;
    }

    /**
     * @return the policyOid
     */
    public ASN1ObjectIdentifier getPolicyOid() {

        return policyOid;
    }

    /**
     * @param policyOid
     *        the policyOid to set
     */
    public void setPolicyOid(ASN1ObjectIdentifier policyOid) {

        this.policyOid = policyOid;
    }

    /**
     * @return the tspServer
     */
    public String getTspServer() {

        return tspServer;
    }

    /**
     * @return the dataLoader
     */
    public DataLoader getDataLoader() {

        return dataLoader;
    }

    /**
     * @return the nonceSource
     */
    public NonceSource getNonceSource() {

        return nonceSource;
    }

}

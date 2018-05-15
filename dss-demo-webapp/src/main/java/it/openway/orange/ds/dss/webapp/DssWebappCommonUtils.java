/**
 * 
 */
package it.openway.orange.ds.dss.webapp;

import it.openway.orange.ds.dss.webapp.exception.DssWebappException;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import eu.europa.esig.dss.DSSDocument;
import eu.europa.esig.dss.InMemoryDocument;

/**
 * @author marco Commons Utilities per Orange DS DSS Webapp
 */
public class DssWebappCommonUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(DssWebappCommonUtils.class);

    /**
     * NB: In DateFormatUtils non esiste una costante con questo formato
     */
    private final static String ISO_DATE_TIME_ZONE_FORMAT_WITH_MILLISECONDS = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

    /**
     * Formatta una data in formato ISO8601
     * 
     * @param date
     *        Data in ingresso
     * @return Stringa in formato ISO8601
     */
    public static String formatISO8601Date(Date date) {

        if (date == null) {
            return null;
        }

        return new SimpleDateFormat(ISO_DATE_TIME_ZONE_FORMAT_WITH_MILLISECONDS).format(date);
    }

    /**
     * Parsing di una stringa in formato ISO8601
     * 
     * @param string
     *        Stringa su cui effetuare il parsing
     * @return istanza di Date
     * @throws ParseException
     */
    public static Date parseISO8601Format(String string)
        throws ParseException {

        if (!StringUtils.hasText(string)) {
            return null;
        }

        Calendar parsedCalendar = javax.xml.bind.DatatypeConverter.parseDateTime(string);
        return parsedCalendar.getTime();

    }

    /**
     * Converte il file in DSSDocument
     * 
     * @param multipartFile
     *        MultiPartFile da convertire
     * @return DSSDocument contenente il file
     * @throws DssWebappException
     */
    public static DSSDocument toDSSDocument(MultipartFile multipartFile)
        throws DssWebappException {

        try {
            if ((multipartFile != null) && !multipartFile.isEmpty()) {
                DSSDocument document = new InMemoryDocument(multipartFile.getBytes(), multipartFile.getOriginalFilename());
                return document;
            }
        }
        catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new DssWebappException("Errore nella creazione del DssDocument", e);
        }
        return null;
    }

    /**
     * Converte il file in DSSDocument
     * 
     * @param bytes
     *        bytes del contenuto del file da convertire
     * @return DssDocument contenente il file
     * @throws DssWebappException
     */
    public static DSSDocument toDSSDocument(byte[] bytes)
        throws DssWebappException {

        DSSDocument dssDocument = null;
        try {
            if (bytes != null && !ArrayUtils.isEmpty(bytes)) {
                dssDocument = new InMemoryDocument(bytes);
            }

        }
        catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new DssWebappException("Errore nella creazione del DssDocument", e);
        }
        return dssDocument;
    }

    /**
     * Converte il file in DSSDocument
     * 
     * @param base64Content
     *        Stringa base64 con il contenuto del file da convertire
     * @return DssDocument contenente il file
     * @throws DssWebappException
     */
    public static DSSDocument toDSSDocument(String base64Content)
        throws DssWebappException {

        if (!StringUtils.hasText(base64Content)) {
            return null;
        }

        byte[] sDecoded = Base64.decodeBase64(base64Content.getBytes());
        return toDSSDocument(sDecoded);
    }

    /**
     * Metodo di validazione utilizzato per verificare se una stringa è in formato base64
     * 
     * @param sBase64
     *        Stringa in formato base64 in ingresso
     * @return True se la stringa è in formato base64
     */
    public static boolean isValidBase64(String sBase64) {

        final String BASE64_PATTERN = "^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)$";

        if (!StringUtils.hasText(sBase64)) {
            return false;
        }

        Pattern pattern = Pattern.compile(BASE64_PATTERN);
        Matcher matcher = pattern.matcher(sBase64);

        boolean isValid = matcher.find();
        return isValid;
    }

    /**
     * Codifica in formato base64 del contenuto di un file
     * 
     * @param bytes
     *        contenuto del file
     * @return Stringa base64 rappresentante il contenuto del file
     * @throws DssWebappException
     */
    public static String encodeBase64(byte[] bytes)
        throws DssWebappException {

        if (bytes == null || bytes.length == 0) {
            throw new DssWebappException("Impossibile codificare i bytes in una stringa base64. L'array e' vuoto");
        }

        String sBase64 = Base64.encodeBase64String(bytes);
        return sBase64;
    }

    /**
     * Decodifica di una stringa in base64
     * 
     * @param sBase64
     *        Stringa base64 da decodificare
     * @return Contenuto del file decodificato
     * @throws DssWebappException
     */
    public static byte[] decodeBase64(String sBase64)
        throws DssWebappException {

        if (!StringUtils.hasText(sBase64)) {
            throw new DssWebappException("Impossibile decodificare la stringa base64. La stringa e' vuota");
        }

        byte[] bytes = Base64.decodeBase64(sBase64);
        return bytes;
    }

}

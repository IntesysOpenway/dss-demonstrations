package it.openway.orange.ds.dss.webapp.model;

/**
 * @author marco
 *  Response del servizio SignDocument
 */
public class SignDocumentResponse extends DssWebappGenericResponse {

    /**
     *  Stringa base64 con il contenuto del file firmato
     */
    private String base64SignedDocument;

    /**
     * @return the base64SignedDocument
     */
    public String getBase64SignedDocument() {

        return base64SignedDocument;
    }

    /**
     * @param base64SignedDocument
     *        the base64SignedDocument to set
     */
    public void setBase64SignedDocument(String base64SignedDocument) {

        this.base64SignedDocument = base64SignedDocument;
    }

}

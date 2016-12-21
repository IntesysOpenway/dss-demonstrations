//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.12.21 at 11:33:26 AM CET 
//


package eu.europa.esig.dss.jaxb.diagnostic;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SigningCertificate complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SigningCertificate">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="AttributePresent" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="DigestValuePresent" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="DigestValueMatch" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="IssuerSerialMatch" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="Signed" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="Id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SigningCertificate", propOrder = {
    "attributePresent",
    "digestValuePresent",
    "digestValueMatch",
    "issuerSerialMatch",
    "signed"
})
public class XmlSigningCertificate {

    @XmlElement(name = "AttributePresent")
    protected Boolean attributePresent;
    @XmlElement(name = "DigestValuePresent")
    protected Boolean digestValuePresent;
    @XmlElement(name = "DigestValueMatch")
    protected Boolean digestValueMatch;
    @XmlElement(name = "IssuerSerialMatch")
    protected Boolean issuerSerialMatch;
    @XmlElement(name = "Signed")
    protected String signed;
    @XmlAttribute(name = "Id", required = true)
    protected String id;

    /**
     * Gets the value of the attributePresent property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isAttributePresent() {
        return attributePresent;
    }

    /**
     * Sets the value of the attributePresent property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setAttributePresent(Boolean value) {
        this.attributePresent = value;
    }

    /**
     * Gets the value of the digestValuePresent property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isDigestValuePresent() {
        return digestValuePresent;
    }

    /**
     * Sets the value of the digestValuePresent property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setDigestValuePresent(Boolean value) {
        this.digestValuePresent = value;
    }

    /**
     * Gets the value of the digestValueMatch property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isDigestValueMatch() {
        return digestValueMatch;
    }

    /**
     * Sets the value of the digestValueMatch property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setDigestValueMatch(Boolean value) {
        this.digestValueMatch = value;
    }

    /**
     * Gets the value of the issuerSerialMatch property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIssuerSerialMatch() {
        return issuerSerialMatch;
    }

    /**
     * Sets the value of the issuerSerialMatch property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIssuerSerialMatch(Boolean value) {
        this.issuerSerialMatch = value;
    }

    /**
     * Gets the value of the signed property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSigned() {
        return signed;
    }

    /**
     * Sets the value of the signed property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSigned(String value) {
        this.signed = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

}

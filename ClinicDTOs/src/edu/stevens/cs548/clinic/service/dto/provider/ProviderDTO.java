//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.10.22 at 01:03:26 PM EDT 
//


package edu.stevens.cs548.clinic.service.dto.provider;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import edu.stevens.cs548.clinic.domain.ITreatmentDAO.TreatmentExn;
import edu.stevens.cs548.clinic.domain.Provider;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="npi" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="providerType" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="treatments" type="{http://www.w3.org/2001/XMLSchema}long" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "id",
    "npi",
    "name",
    "providerType",
    "treatments"
})
@XmlRootElement(name = "providerDTO")
public class ProviderDTO {

    protected long id;
    protected long npi;
    @XmlElement(required = true)
    protected String name;
    @XmlElement(required = true)
    protected String providerType;
    @XmlElement(nillable = true)
    protected long[] treatments;
    
    
    public ProviderDTO() { }
    
    public ProviderDTO(Provider provider){
    	this.id = provider.getId();
		this.npi = provider.getNPI();
		this.name = provider.getName();
		this.providerType = provider.getProviderType();
		List<Long> tids;
		try {
			tids = provider.getTreatmentIds();
			this.treatments = new long[tids.size()];
			for (int i = 0; i<this.treatments.length;i++) {
				this.treatments[i] = tids.get(i);
			}
		} catch (TreatmentExn e) {
			treatments = null;
			e.printStackTrace();
		}
    }

    /**
     * Gets the value of the id property.
     * 
     */
    public long getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     */
    public void setId(long value) {
        this.id = value;
    }

    /**
     * Gets the value of the npi property.
     * 
     */
    public long getNpi() {
        return npi;
    }

    /**
     * Sets the value of the npi property.
     * 
     */
    public void setNpi(long value) {
        this.npi = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the providerType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProviderType() {
        return providerType;
    }

    /**
     * Sets the value of the providerType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProviderType(String value) {
        this.providerType = value;
    }

    /**
     * Gets the value of the treatments property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the treatments property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTreatments().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Long }
     * 
     * 
     */
    public long[] getTreatments() {
        return this.treatments;
    }

}

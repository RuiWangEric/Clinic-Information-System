//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.2-147 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.09.13 at 05:37:37 PM EDT 
//


package edu.stevens.cs.cs548.clinic.schema;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the edu.stevens.cs.cs548.clinic.schema package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Provider_QNAME = new QName("http://www.example.org/schemas/clinic/provider", "Provider");
    private final static QName _Clinic_QNAME = new QName("http://www.example.org/schemas/clinic", "Clinic");
    private final static QName _Patient_QNAME = new QName("http://www.example.org/schemas/clinic/patient", "Patient");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: edu.stevens.cs.cs548.clinic.schema
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link SurgeryType }
     * 
     */
    public SurgeryType createSurgeryType() {
        return new SurgeryType();
    }

    /**
     * Create an instance of {@link RadiologyType }
     * 
     */
    public RadiologyType createRadiologyType() {
        return new RadiologyType();
    }

    /**
     * Create an instance of {@link ClinicType }
     * 
     */
    public ClinicType createClinicType() {
        return new ClinicType();
    }

    /**
     * Create an instance of {@link DrugTreatmentType }
     * 
     */
    public DrugTreatmentType createDrugTreatmentType() {
        return new DrugTreatmentType();
    }

    /**
     * Create an instance of {@link ProviderType }
     * 
     */
    public ProviderType createProviderType() {
        return new ProviderType();
    }

    /**
     * Create an instance of {@link PatientType }
     * 
     */
    public PatientType createPatientType() {
        return new PatientType();
    }

    /**
     * Create an instance of {@link PatientType.Treatments }
     * 
     */
    public PatientType.Treatments createPatientTypeTreatments() {
        return new PatientType.Treatments();
    }

    /**
     * Create an instance of {@link TreatmentType }
     * 
     */
    public TreatmentType createTreatmentType() {
        return new TreatmentType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ProviderType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.example.org/schemas/clinic/provider", name = "Provider")
    public JAXBElement<ProviderType> createProvider(ProviderType value) {
        return new JAXBElement<ProviderType>(_Provider_QNAME, ProviderType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ClinicType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.example.org/schemas/clinic", name = "Clinic")
    public JAXBElement<ClinicType> createClinic(ClinicType value) {
        return new JAXBElement<ClinicType>(_Clinic_QNAME, ClinicType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PatientType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.example.org/schemas/clinic/patient", name = "Patient")
    public JAXBElement<PatientType> createPatient(PatientType value) {
        return new JAXBElement<PatientType>(_Patient_QNAME, PatientType.class, null, value);
    }

}

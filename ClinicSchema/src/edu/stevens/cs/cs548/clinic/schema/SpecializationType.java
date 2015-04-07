//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.2-147 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.09.13 at 05:37:37 PM EDT 
//


package edu.stevens.cs.cs548.clinic.schema;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SpecializationType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="SpecializationType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Radiology"/>
 *     &lt;enumeration value="Surgery"/>
 *     &lt;enumeration value="Oncology"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "SpecializationType", namespace = "http://www.example.org/schemas/clinic/provider")
@XmlEnum
public enum SpecializationType {

    @XmlEnumValue("Radiology")
    RADIOLOGY("Radiology"),
    @XmlEnumValue("Surgery")
    SURGERY("Surgery"),
    @XmlEnumValue("Oncology")
    ONCOLOGY("Oncology");
    private final String value;

    SpecializationType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static SpecializationType fromValue(String v) {
        for (SpecializationType c: SpecializationType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}

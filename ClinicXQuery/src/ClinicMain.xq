(: XQuery main module :)

import schema namespace pat="http://www.example.org/schemas/clinic/patient" at "Patient.xsd";

import schema namespace clin="http://www.example.org/schemas/clinic" at "Clinic.xsd";

import schema namespace prov="http://www.example.org/schemas/clinic/provider" at "Provider.xsd";

declare namespace trmt="http://www.example.org/schemas/clinic/treatment";

import module namespace gt = "http://www.example.com/xquery/clinic/TestProgram" at "TestProgram.xq";




let $clinic :=doc("ClinicData.xml")/clin:Clinic
    return 
    (
        gt:getPatientTreatments($clinic),
        gt:getPatientDrugs($clinic),
        gt:getTreatmentInfo($clinic),
        gt:getProviderInfo($clinic),
        gt:getDrugInfo($clinic)

    )
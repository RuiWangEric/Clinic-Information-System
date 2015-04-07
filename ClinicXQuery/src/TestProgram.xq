module namespace get = 'http://www.example.com/xquery/clinic/TestProgram';

import schema namespace pat="http://www.example.org/schemas/clinic/patient" at "Patient.xsd";

import schema namespace clin="http://www.example.org/schemas/clinic" at "Clinic.xsd";

import schema namespace prov="http://www.example.org/schemas/clinic/provider" at "Provider.xsd";

declare namespace trmt="http://www.example.org/schemas/clinic/treatment";

(:------------------------Question1-----------------------------------------------------:)
declare function get:getPatientTreatments ($klinic1 as element (clin:Clinic)) 
    as element(get:treatment){
        <get:treatment>&#10; 
            {$klinic1/pat:Patient[pat:name="Jane joe"]/trmt:treatment }&#10;
        </get:treatment>
};

(:------------------------Question2-----------------------------------------------------:)
declare function get:getPatientDrugs ($klinic2 as element (clin:Clinic)) 
    as element(get:treatment){
    <get:treatment>&#10;
    
        <drug>{$klinic2/pat:Patient[pat:name="Mark Hamill"]/trmt:treatment/trmt:drug-treatment}</drug>&#10;
        <diagnosis> {$klinic2/pat:Patient[pat:name="Mark Hamill"]/trmt:treatment/trmt:diagnosis/text()} </diagnosis>&#10;
        <dosage>{$klinic2/pat:Patient[pat:name="Mark Hamill"]/trmt:treatment/trmt:drug-treatment/trmt:dosage/text()}</dosage> &#10;
    
    </get:treatment>
};

(:------------------------Question3-----------------------------------------------------:)
declare function get:getTreatmentInfo ($klinic3 as element (clin:Clinic)) 
    as element (get:treatment)*{
    for $ n in $klinic3/pat:Patient/trmt:treatment/node() return
    typeswitch($n)
    case $laterdata as element(trmt:drug-treatment)
    return
    {
        <get:treatment>&#10;
        <treatment-type>"Drug-treatment"</treatment-type>&#10;   
        <patientidentifier>&#10;
        {
         
            for $dtpat in $klinic3/pat:Patient
                where $dtpat/trmt:treatment/trmt:drug-treatment=$laterdata
                return 
                    <info>&#10;
                        <patient-id> { $dtpat/pat:patient-id/text() } </patient-id>&#10;
                        <name> { $dtpat/pat:name/text() } </name>&#10;
                        <dob> { $dtpat/pat:dob/text() } </dob>&#10;
                    </info>
        }
        </patientidentifier>&#10;
        
        <provideridentifier>&#10;
        {
            for $dtprov in $klinic3/prov:Provider
                where $dtprov/prov:provider-id=$laterdata/../trmt:provider-id
                return 
                    <info>&#10;
                        <provider_id>{$dtprov/prov:provider-id/text()}</provider_id>&#10;
                        <name>{$dtprov/prov:name/text()}</name>&#10;
                        <specialization>{$dtprov/prov:specialization/text()}</specialization>&#10;
                    </info>
        }
        </provideridentifier>&#10;
        </get:treatment>
    }

    case $laterdata as element(trmt:radiology)
    return
    {
        <get:treatment>&#10;
        
        <treatment-type>"Radiology"</treatment-type>&#10;
        
        <patientidentifier>&#10;
        { 
            for $rpat in $klinic3/pat:Patient
                where $rpat/trmt:treatment/trmt:radiology=$laterdata
                return 
                    <info>&#10;
                        <patient_id>{$rpat/pat:patient-id/text()}</patient_id>&#10;
                        <name>{$rpat/pat:name/text()}</name>&#10;                
                        <dob>{$rpat/pat:dob/text()}</dob>&#10;
                    </info>
        }
        </patientidentifier>&#10;
        
        <provideridentifier>&#10;
        {
            for $rprov in $klinic3/prov:Provider
                where $rprov/prov:provider-id=$laterdata/../trmt:provider-id
                return 
                    <info>&#10;
                        <provider_id>{$rprov/prov:provider-id/text()}</provider_id>&#10;
                        <name>{$rprov/prov:name/text()}</name>&#10;
                        <specialization>{$rprov/prov:specialization/text()}</specialization>&#10;
                    </info>
        }
        </provideridentifier>&#10;
        
        </get:treatment>
    }

    case $laterdata as element(trmt:surgery)
    return
    {
        <get:treatment>&#10;
        
        <treatment-type>"Surgery"</treatment-type>&#10;
        
        <patientidentifier>&#10;
        { 
            for $spat in $klinic3/pat:Patient
                where $spat/trmt:treatment/trmt:surgery=$laterdata
                return 
                    <info>&#10;
                        <patient_id>{$spat/pat:patient-id/text()}</patient_id>&#10;
                        <name>{$spat/pat:name/text()}</name>&#10;
                        <dob>{$spat/pat:dob/text()}</dob>&#10;
                    </info>
        }
        </patientidentifier>&#10;
        
        <provideridentifier>&#10;
        {
            for $sprov in $klinic3/prov:Provider
                where $sprov/prov:provider-id=$laterdata/../trmt:provider-id
                return 
                    <info>&#10;
                        <provider_id>{$sprov/prov:provider-id/text()}</provider_id>&#10;
                        <name>{$sprov/prov:name/text()}</name>&#10;
                        <specialization>{$sprov/prov:specialization/text()}</specialization>&#10;
                    </info>
        }
        </provideridentifier>&#10;
        
        </get:treatment>
    }

default return { }
};

(:------------------------Question4-----------------------------------------------------:)
declare function get:getProviderInfo ( $klinic4 as element(clin:Clinic))
    as element(get:providerinfo) *{
    <get:providerinfo>
    {
        <providerlist>&#10;
        {
           for $ldprov in $klinic4/prov:Provider
           return
           {
               <providerinfo>&#10;
                    <provider_id> {$ldprov/prov:provider-id/text()}</provider_id>&#10;
                    <name>{$ldprov/prov:name/text()}</name>&#10;
                    <specialization>{$ldprov/prov:specialization/text()}</specialization>&#10;
                
                <PatientList>
                {
                    for $ldpat in $klinic4/pat:Patient
                        where $ldpat/trmt:treatment/trmt:provider-id = $klinic4/prov:Provider/prov:provider-id
                        return
                        {
                            <PatientInfo>
                                 <patient_id>{$ldpat/pat:patient-id/text()}</patient_id>&#10;
                                 <patient_name>{$ldpat/pat:name/text()}</patient_name>&#10;
                                 <patient_dob>{$ldpat/pat:dob/text()}</patient_dob>&#10;
                             
                                 <patient_treatment>&#10;
                                 {
                                     for $ldtrmt in $ldpat/trmt:treatment
                                         where $ldtrmt/trmt:provider-id=$ldprov/prov:provider-id 
                                         return
                                         {
                                             <treatment>{$ldtrmt/*[last()]/name()}</treatment>
                                         }
                                 }&#10;
                                 </patient_treatment>&#10;
                         </PatientInfo>
                      }
              }&#10;
            </PatientList>&#10;
            </providerinfo>
        }
        }&#10; 
        </providerlist>
    }  
    </get:providerinfo>
};


(:------------------------Question5-----------------------------------------------------:)
declare function get:getDrugInfo ($klinic5 as element(clin:Clinic))
    as element (get:druginfo)*{
    for $n in $klinic5/pat:Patient/trmt:treatment/node() return
    typeswitch($n)
    
    case $ druginfo as element(trmt:drug-treatment)
    return
    {
        <get:druginfo>&#10;
        
        <drug>{$druginfo/trmt:name/text()}</drug>&#10;
        
        <patientinfo>
        {
            for $dtpat in $klinic5/pat:Patient
                where $dtpat/trmt:treatment/trmt:drug-treatment/trmt:name=$druginfo/trmt:name
                return
                    <info>&#10;
                        <patient_id>{$dtpat/pat:patient-id/text()}</patient_id>&#10;
                        <name>{$dtpat/pat:name/text()}</name>&#10;
                        <dob>{$dtpat/pat:dob/text()}</dob>&#10;
                        <diagnosis>{$dtpat/trmt:treatment/trmt:diagnosis/text()}</diagnosis>&#10;
                        <physician>{$dtpat/trmt:treatment/trmt:drug-treatment/trmt:physician/text()}</physician>&#10;
                    </info>
        }
        </patientinfo>&#10;
        </get:druginfo>
    }
    default return{ }
};



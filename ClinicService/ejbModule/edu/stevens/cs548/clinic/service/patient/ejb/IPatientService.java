package edu.stevens.cs548.clinic.service.patient.ejb;

import java.util.Date;

import edu.stevens.cs548.clinic.service.dto.patient.PatientDTO;
import edu.stevens.cs548.clinic.service.dto.treatment.TreatmentDto;

public interface IPatientService {
	
	public class PatientServiceExn extends Exception {
		private static final long serialVersionUID = 1L;
		public PatientServiceExn (String m){
			super(m);
		}
	}
	
	public class PatientNotFoundExn extends PatientServiceExn {
		private static final long serialVersionUID = 1L;
		public PatientNotFoundExn (String m) {
			super(m);
		}
	}
	
	public class ProviderNotFoundExn extends PatientServiceExn {
		private static final long serialVersionUID = 1L;
		public ProviderNotFoundExn(String m) {
			super(m);
		}
	}
	
	public class TreatmentNotFoundExn extends PatientServiceExn {
		private static final long serialVersionUID = 1L;
		public TreatmentNotFoundExn (String m) {
			super(m);
		}
	}
	
	//1. Adding a patient to a clinic
	public long createPatient (long patientId, String name, Date dob, int age) throws PatientServiceExn;
	
	//2. Obtaining a list of patient DTOs, given a patient name and dob
	public PatientDTO[] getPatientsByNameDob (String name, Date dob) throws PatientServiceExn;
	
	//3. Obtaining a single patient DTO, given a patient identifier
	public PatientDTO getPatientByDbId(long id) throws PatientServiceExn;
	
	//4. Obtaining a single patient DTO, given a patient key
	public PatientDTO getPatientByPatId (long pid) throws PatientServiceExn;
	
	//5. Deleting a patient record, given the patient name and patient key
	public void deletePatient (String name, long id) throws PatientServiceExn; 
	
	//6. Obtaining a list of treatment DTOs for a patient, given that patient's key
	public TreatmentDto[] getTreatments(long id, long[] tids) throws PatientNotFoundExn, TreatmentNotFoundExn, PatientServiceExn;
	
	//7. Deleting the treatment from the patient 
	public void deleteTreatment(long id, long tid) throws PatientNotFoundExn, TreatmentNotFoundExn, PatientServiceExn;
	
	// Add drug-treatment through the patient
	public long addDrugTreatment(long id, long NPI, String diagnosis, String drug, float dosage) throws PatientNotFoundExn, ProviderNotFoundExn;
	
	// Add radiology through the patient 
	public long addRadiology(long id, long NPI, String diagnosis, Date raddate) throws PatientNotFoundExn, ProviderNotFoundExn;
	
	// Add surgery through the patient 
	public long addSurgery(long id, long NPI, String diagnosis, Date surdate) throws PatientNotFoundExn, ProviderNotFoundExn;

	public String siteInfo();

	int deleteAll() throws PatientServiceExn;
	
}

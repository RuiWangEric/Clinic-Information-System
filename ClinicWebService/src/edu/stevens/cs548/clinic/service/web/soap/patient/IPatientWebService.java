package edu.stevens.cs548.clinic.service.web.soap.patient;

import java.util.Date;

import javax.jws.WebMethod;
import javax.jws.WebService;

import edu.stevens.cs548.clinic.service.dto.patient.PatientDTO;
import edu.stevens.cs548.clinic.service.dto.treatment.TreatmentDto;
import edu.stevens.cs548.clinic.service.patient.ejb.IPatientService.PatientNotFoundExn;
import edu.stevens.cs548.clinic.service.patient.ejb.IPatientService.PatientServiceExn;
import edu.stevens.cs548.clinic.service.patient.ejb.IPatientService.ProviderNotFoundExn;
import edu.stevens.cs548.clinic.service.patient.ejb.IPatientService.TreatmentNotFoundExn;

@WebService(name = "IPatientWebPort",
targetNamespace = "http://cs548.stevens.edu/clinic/service/web/soap/patient")

/*
 * Endpoint interface for the patient Web service.
 */

public interface IPatientWebService {
	
	@WebMethod(operationName="create")
	public long createPatient ( long patientId, String name, Date dob, int age) throws PatientServiceExn;
	
	@WebMethod
	public PatientDTO[] getPatientsByNameDob (String name, Date dob) throws PatientServiceExn;

	@WebMethod
	public PatientDTO getPatientByDbId(long id) throws PatientServiceExn;
	
	@WebMethod
	public PatientDTO getPatientByPatId (long pid) throws PatientServiceExn;
		
	@WebMethod
	public void deletePatient (String name, long id) throws PatientServiceExn; 
	
	@WebMethod
	public TreatmentDto[] getTreatments(long id, long[] tids) throws PatientNotFoundExn, TreatmentNotFoundExn, PatientServiceExn;

	@WebMethod
	public void deleteTreatment(long id, long tid) throws PatientNotFoundExn, TreatmentNotFoundExn, PatientServiceExn;

	@WebMethod
	public void addDrugTreatment(long id, long NPI, String diagnosis, String drug, float dosage) throws PatientNotFoundExn, ProviderNotFoundExn;
	
	@WebMethod
	public void addRadiology(long id, long NPI, String diagnosis, Date date) throws PatientNotFoundExn, ProviderNotFoundExn;
	 
	@WebMethod
	public void addSurgery(long id, long NPI, String diagnosis, Date date) throws PatientNotFoundExn, ProviderNotFoundExn;

	@WebMethod
	public String siteInfo();

}

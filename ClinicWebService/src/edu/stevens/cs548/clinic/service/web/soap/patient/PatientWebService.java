package edu.stevens.cs548.clinic.service.web.soap.patient;

import java.util.Date;

import javax.ejb.EJB;
import javax.jws.WebService;

import edu.stevens.cs548.clinic.service.dto.patient.PatientDTO;
import edu.stevens.cs548.clinic.service.dto.treatment.TreatmentDto;
import edu.stevens.cs548.clinic.service.patient.ejb.IPatientService.ProviderNotFoundExn;
import edu.stevens.cs548.clinic.service.patient.ejb.IPatientServiceRemote;
import edu.stevens.cs548.clinic.service.patient.ejb.IPatientService.PatientNotFoundExn;
import edu.stevens.cs548.clinic.service.patient.ejb.IPatientService.PatientServiceExn;
import edu.stevens.cs548.clinic.service.patient.ejb.IPatientService.TreatmentNotFoundExn;

@WebService(
		endpointInterface="edu.stevens.cs548.clinic.service.web.soap.patient.IPatientWebService",
		targetNamespace="http://cs548.stevens.edu/clinic/service/web/soap/patient",
		serviceName="PatientWebService",
		portName="PatientWebPort")

public class PatientWebService {
	//implements IPatientWebService 

	@EJB(beanName="PatientServiceBean")
	IPatientServiceRemote service;


	public long createPatient(long patientId, String name, Date dob,  int age)
			throws PatientServiceExn {
		return service.createPatient(patientId,name, dob, age);
	}


	public PatientDTO[] getPatientsByNameDob(String name, Date dob) throws PatientServiceExn{
		return service.getPatientsByNameDob(name, dob);
		
	}
		

	public PatientDTO getPatientByDbId(long id) throws PatientServiceExn {
		return service.getPatientByDbId(id);
	}
		

	public PatientDTO getPatientByPatId(long pid) throws PatientServiceExn {
		return service.getPatientByPatId(pid);
	}
	

	public void deletePatient(String name, long id) throws PatientServiceExn {
		this.service.deletePatient(name, id);
		
	}


	public TreatmentDto[] getTreatments(long id, long[] tids)
			throws PatientNotFoundExn, TreatmentNotFoundExn, PatientServiceExn {
		return service.getTreatments(id, tids);
	}


	public void deleteTreatment(long id, long tid) throws PatientNotFoundExn,
			TreatmentNotFoundExn, PatientServiceExn {
		this.service.deleteTreatment(id, tid);
	}


	public void addDrugTreatment(long id, long NPI, String diagnosis, String drug,
			float dosage) throws PatientNotFoundExn, ProviderNotFoundExn {
		this.service.addDrugTreatment(id, NPI, diagnosis, drug, dosage);
		
	}


	public void addRadiology(long id, long NPI, String diagnosis, Date date)
			throws PatientNotFoundExn, ProviderNotFoundExn {
		this.service.addRadiology(id, NPI, diagnosis, date);

	}


	public void addSurgery(long id, long NPI, String diagnosis, Date date)
			throws PatientNotFoundExn, ProviderNotFoundExn {
		this.service.addSurgery(id, NPI, diagnosis, date);
	}


	public String siteInfo() {
		return service.siteInfo();
	}

}

package edu.stevens.cs548.clinic.service.web.soap.provider;

import javax.jws.WebMethod;
import javax.jws.WebService;

import edu.stevens.cs548.clinic.domain.ITreatmentDAO.TreatmentExn;
import edu.stevens.cs548.clinic.service.dto.provider.ProviderDTO;
import edu.stevens.cs548.clinic.service.dto.treatment.TreatmentDto;
import edu.stevens.cs548.clinic.service.provider.ejb.IProviderService.PatientNotFoundExn;
import edu.stevens.cs548.clinic.service.provider.ejb.IProviderService.ProviderNotFoundExn;
import edu.stevens.cs548.clinic.service.provider.ejb.IProviderService.ProviderServiceExn;
import edu.stevens.cs548.clinic.service.provider.ejb.IProviderService.TreatmentNotFoundExn;

@WebService(
		name="IProviderWebPort",
		targetNamespace = "http://cs548.stevens.edu/clinic/service/web/soap/provider"
		)
/*
 * Endpoint interface for the provider Web service.
 */


public interface IProviderWebService {
	
	//1. Adding a provider to a clinic
	@WebMethod(operationName="create")
	public long createProvider (long NPI, String name, String providerType) throws ProviderServiceExn;

	//2. Obtaining a list of provider DTOs, given a provider name 
	@WebMethod
	public ProviderDTO[] getProviderByName (String name) throws ProviderServiceExn, TreatmentExn, ProviderNotFoundExn;
	
	//3. Obtaining a single provider DTO, given a NPI
	@WebMethod
	public ProviderDTO getProviderByNPI (long NPI) throws ProviderServiceExn, ProviderNotFoundExn, TreatmentExn;
	
	//4. Adding a treatment for a patient. Specifies the patientId and NPI and Treatment DTO
	@WebMethod
	public void createTreatment(long pid, long NPI, TreatmentDto tdo) throws ProviderServiceExn, PatientNotFoundExn, ProviderNotFoundExn;

	//5.Delete a treatment for a patient 
	@WebMethod
	public void deleteTreatment(long pid, long NPI, long tid) throws PatientNotFoundExn, ProviderNotFoundExn, TreatmentNotFoundExn, ProviderServiceExn;
	
	//6. Obtaining a list of treatment DTOs, for all treatments for a particular patient that are supervised by this provider
	@WebMethod
	public TreatmentDto[] getTreatments(long pid, long NPI) throws ProviderNotFoundExn, ProviderServiceExn, TreatmentNotFoundExn, PatientNotFoundExn;
	
	//7. Obtaining a list of treatment DTOs for the treatments supervised by this provider
	@WebMethod
	public TreatmentDto[] getTreatmentsByProvider(long NPI, long[] tids) throws ProviderNotFoundExn, ProviderServiceExn;

	// Delete a provider record, given the provider name and NPI
	@WebMethod
	public void deleteProvider(long NPI, String name) throws ProviderServiceExn, TreatmentExn;

	@WebMethod
	public String siteInfo();

}

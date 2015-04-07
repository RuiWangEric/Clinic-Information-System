package edu.stevens.cs548.clinic.service.web.soap.provider;

import javax.ejb.EJB;
import javax.jws.WebService;

import edu.stevens.cs548.clinic.domain.ITreatmentDAO.TreatmentExn;
import edu.stevens.cs548.clinic.service.dto.provider.ProviderDTO;
import edu.stevens.cs548.clinic.service.dto.treatment.TreatmentDto;
import edu.stevens.cs548.clinic.service.provider.ejb.IProviderService.PatientNotFoundExn;
import edu.stevens.cs548.clinic.service.provider.ejb.IProviderService.ProviderNotFoundExn;
import edu.stevens.cs548.clinic.service.provider.ejb.IProviderService.ProviderServiceExn;
import edu.stevens.cs548.clinic.service.provider.ejb.IProviderService.TreatmentNotFoundExn;
import edu.stevens.cs548.clinic.service.provider.ejb.IProviderServiceRemote;


@WebService(
		endpointInterface="edu.stevens.cs548.clinic.service.web.soap.provider.IProviderWebService",
		targetNamespace="http://cs548.stevens.edu/clinic/service/web/soap/provider",
		serviceName="ProviderWebService",
		portName="ProviderWebPort"
		)

public class ProviderWebService implements IProviderWebService{

	
	@EJB(beanName="ProviderServiceBean")
	IProviderServiceRemote service;
	
	@Override
	public long createProvider(long NPI, String name, String providerType)
			throws ProviderServiceExn {
		return service.createProvider(NPI, name, providerType);
	}

	@Override
	public ProviderDTO[] getProviderByName(String name)
			throws ProviderServiceExn, ProviderNotFoundExn, TreatmentExn {
		return service.getProviderByName(name);
	}

	@Override
	public ProviderDTO getProviderByNPI(long NPI) throws ProviderServiceExn,
			ProviderNotFoundExn, TreatmentExn {
		return service.getProviderByNPI(NPI);
	}

	@Override
	public void createTreatment(long pid, long NPI, TreatmentDto tdo)
			throws ProviderServiceExn, PatientNotFoundExn, ProviderNotFoundExn {
		this.service.createTreatment(pid, NPI, tdo);
		
	}

	@Override
	public void deleteTreatment(long pid, long NPI, long tid) throws PatientNotFoundExn, ProviderNotFoundExn,
			TreatmentNotFoundExn, ProviderServiceExn {
		this.service.deleteTreatment(pid,NPI, tid);
		
	}

	@Override
	public TreatmentDto[] getTreatments(long pid, long NPI)
			throws ProviderNotFoundExn, ProviderServiceExn,
			TreatmentNotFoundExn, PatientNotFoundExn {
		return service.getTreatmentsByPaPr(pid, NPI);
	}

	@Override
	public TreatmentDto[] getTreatmentsByProvider(long NPI, long[] tids)
			throws ProviderNotFoundExn, ProviderServiceExn {
		return service.getTreatmentsByProvider(NPI, tids);
	}

	@Override
	public void deleteProvider(long NPI, String name)
			throws ProviderServiceExn, TreatmentExn {
		this.service.deleteProvider(NPI, name);
		
	}

	@Override
	public String siteInfo() {
		return service.siteInfo();
	}

}

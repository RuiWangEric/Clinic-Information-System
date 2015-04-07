package edu.stevens.cs548.clinic.service.provider.ejb;


import edu.stevens.cs548.clinic.service.dto.provider.ProviderDTO;
import edu.stevens.cs548.clinic.service.dto.treatment.TreatmentDto;


public interface IProviderService {
	
	public class ProviderServiceExn extends Exception {
		private static final long serialVersionUID = 1L;
		public ProviderServiceExn (String m) {
			super (m);
		}
	}
	
	public class ProviderNotFoundExn extends Exception {
		private static final long serialVersionUID = 1L;
		public ProviderNotFoundExn (String m) {
			super(m);
		}
	}
	
	public class PatientNotFoundExn extends Exception {
		private static final long serialVersionUID = 1L;
		public PatientNotFoundExn (String msg) {
			super(msg);
		}
	}
	
	public class TreatmentNotFoundExn extends Exception {
		private static final long serialVersionUID = 1L;
		public TreatmentNotFoundExn (String m) {
			super(m);
		}
	}
	
	//1. Adding a provider to a clinic
	public long createProvider (long NPI, String name, String providerType) throws ProviderServiceExn;
	
	//2. Obtaining a list of provider DTOs, given a provider name 
	public ProviderDTO[] getProviderByName (String name) throws ProviderServiceExn;
	
	public ProviderDTO getProviderByDbId (long id) throws ProviderServiceExn;
	
	//3. Obtaining a single provider DTO, given a NPI
	public ProviderDTO getProviderByNPI (long NPI) throws ProviderServiceExn;
	
	//4. Adding a treatment for a patient. Specifies the patientId and NPI and Treatment DTO
	public long createTreatment(long pid, long NPI, TreatmentDto tdo) throws ProviderServiceExn, ProviderNotFoundExn, PatientNotFoundExn;

	//5.Delete a treatment for a patient 
	public void deleteTreatment(long pid, long NPI, long tid) throws ProviderNotFoundExn, ProviderServiceExn, PatientNotFoundExn;
	
	//6. Obtaining a list of treatment DTOs, for all treatments for a particular patient that are supervised by this provider
	public TreatmentDto[] getTreatmentsByPaPr(long pid, long NPI) throws ProviderNotFoundExn, TreatmentNotFoundExn, PatientNotFoundExn;
	
	//7. Obtaining a list of treatment DTOs for the treatments supervised by this provider
	public TreatmentDto[] getTreatmentsByProvider(long NPI, long[] tids) throws ProviderNotFoundExn, ProviderServiceExn;

	// Delete a provider record, given the provider name and NPI
	public void deleteProvider(long NPI, String name) throws ProviderServiceExn;

	public String siteInfo();

	int deleteAll() throws ProviderServiceExn;

}

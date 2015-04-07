package edu.stevens.cs548.clinic.service.provider.ejb;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import edu.stevens.cs548.clinic.domain.IPatientDAO;
import edu.stevens.cs548.clinic.domain.IPatientDAO.PatientExn;
import edu.stevens.cs548.clinic.domain.IProviderDAO;
import edu.stevens.cs548.clinic.domain.IProviderDAO.ProviderExn;
import edu.stevens.cs548.clinic.domain.ITreatmentDAO;
import edu.stevens.cs548.clinic.domain.ITreatmentDAO.TreatmentExn;
import edu.stevens.cs548.clinic.domain.ITreatmentVisitor;
import edu.stevens.cs548.clinic.domain.Patient;
import edu.stevens.cs548.clinic.domain.PatientDAO;
import edu.stevens.cs548.clinic.domain.Provider;
import edu.stevens.cs548.clinic.domain.ProviderDAO;
import edu.stevens.cs548.clinic.domain.ProviderFactory;
import edu.stevens.cs548.clinic.domain.Treatment;
import edu.stevens.cs548.clinic.domain.TreatmentDAO;
import edu.stevens.cs548.clinic.service.dto.provider.ProviderDTO;
import edu.stevens.cs548.clinic.service.dto.treatment.DrugTreatmentType;
import edu.stevens.cs548.clinic.service.dto.treatment.RadiologyType;
import edu.stevens.cs548.clinic.service.dto.treatment.SurgeryType;
import edu.stevens.cs548.clinic.service.dto.treatment.TreatmentDto;

/**
 * Session Bean implementation class ProviderService
 */
@Stateless(name = "ProviderServiceBean")
public class ProviderService implements IProviderServiceLocal,
		IProviderServiceRemote {

	private ProviderFactory providerFactory;
	private IProviderDAO providerDAO;
	private IPatientDAO patientDAO;
	private ITreatmentDAO treatmentDAO;

	/**
	 * Default constructor.
	 */
	public ProviderService() {
		providerFactory = new ProviderFactory();
	}

	@PersistenceContext(unitName = "ClinicDomain")
	private EntityManager em;

	@PostConstruct
	private void initialize() {
		providerDAO = new ProviderDAO(em);
		patientDAO = new PatientDAO(em);
		treatmentDAO = new TreatmentDAO(em);
	}

	/**
	 * @param specializationType 
	 * @param type 
	 * @see IProviderService#createProvider(String, long)
	 * Provider service facade:
	 * 1. Adding a provider to a clinic
	 */
	public long createProvider(long NPI, String name, String providerType) throws ProviderServiceExn{
		Provider provider = this.providerFactory.createProvider(NPI, name, providerType);
		try {
			providerDAO.addProvider(provider);
		} catch (ProviderExn e) {
			throw new ProviderServiceExn(e.toString());
		}
		return provider.getId();
	}

	/**
	 * @throws TreatmentExn 
	 * @throws ProviderExn 
	 * @see IProviderService#getProviderByName(String)
	 * 2. Obtaining a list of provider DTOs, given a provider name.
	 */
	public ProviderDTO[] getProviderByName(String name) throws ProviderServiceExn  {
		try {
			List<Provider> providers = providerDAO.getProviderByName(name);
			ProviderDTO[] dto = new ProviderDTO[providers.size()];
			for(int i=0; i<dto.length; i++) {
				dto[i] = new ProviderDTO(providers.get(i));
			}
			return dto;
		} catch (ProviderExn e) {
			throw new ProviderServiceExn(e.toString());
		}
	}
	
	/**
	 * @throws TreatmentExn 
	 * @see IProviderService#getProviderByDbId(long)
	 */
	public ProviderDTO getProviderByDbId(long id) throws ProviderServiceExn {
		try {
			Provider provider = providerDAO.getProviderByDbId(id);
			if(provider == null) {
				throw new ProviderServiceExn("Provider not found!");
			}
			return new ProviderDTO(provider);
		} catch (ProviderExn e) {
			throw new ProviderServiceExn (e.toString());
		}
	}
	
	/**
	 * @throws TreatmentExn 
	 * @see IProviderService#getProviderByNPI(long)
	 * 3. Obtaining a single provider DTO, given a NPI
	 */
	public ProviderDTO getProviderByNPI(long NPI) throws ProviderServiceExn {
		try {
			Provider provider = providerDAO.getProviderByNPI(NPI);
			return new ProviderDTO(provider);
		} catch (ProviderExn e) {
			throw new ProviderServiceExn(e.toString());
		}
	}
	
	/**
	 * @throws TreatmentExn 
	 * @see IProviderService#createTreatmentByIdNPI(long id, long NPI,TreatmentDto tdo)
	 * 4. Adding a treatment for a patient. Operation the patientId, NPI and Treatment DTO
	 */
	
	
	@Override
	public long createTreatment(long pid, long NPI, TreatmentDto tdo)
			throws ProviderServiceExn, PatientNotFoundExn, ProviderNotFoundExn {
		try {
			Patient patient = patientDAO.getPatientByPatientId(pid);
			Provider provider = providerDAO.getProviderByNPI(NPI);
			long tid = 0;
			if (tdo.getNPI() != NPI) {
				throw new ProviderServiceExn ("The treatment is not supervised by provider: NPI = " + NPI);
			}
			if (!tdo.getDrugTreatment().getName().isEmpty()) {
				tid = provider.addDrugTreatment(tdo.getDiagnosis(), tdo.getDrugTreatment().getName(), tdo.getDrugTreatment().getDosage(), patient);
			} else if (!tdo.getRadiology().getDate().toString().isEmpty()) {
				tid = provider.addRadiology(tdo.getDiagnosis(), tdo.getRadiology().getDate(), patient);
			} else if (!tdo.getSurgery().getDate().toString().isEmpty()) {
				tid = provider.addSurgery(tdo.getDiagnosis(), tdo.getSurgery().getDate(), patient);
			}
			return tid;
		} catch (PatientExn e) {
			throw new PatientNotFoundExn (e.toString());
		} catch (ProviderExn e) {
			throw new ProviderNotFoundExn (e.toString());
		}
		/*
		String treatmentType = tdo.getTreatmentName();
		Patient patient;
		Provider provider;
		try {
			patient = this.patientDAO.getPatientByPatientId(pid);
			System.out.println(patient.getName());
		} catch (PatientExn e) {
			throw new PatientNotFoundExn(e.toString());
		}
		try {
			provider = this.providerDAO.getProviderByNPI(NPI);
			System.out.println(provider.getName());
		} catch (ProviderExn e) {
			throw new ProviderNotFoundExn(e.toString());
		}
		
		if (treatmentType.equals("drugTreatment")) {
			long tid = tdo.getTid();
			String diagnosis = tdo.getDiagnosis();
			float dosage = tdo.getDrugTreatment().getDosage();
			String drug = tdo.getDrugTreatment().getName();
			patient.addDrugTreatment(diagnosis, drug, dosage, provider);
			provider.addDrugTreatment(tid);
		}
		
		if(treatmentType.equals("radiology")) {
			long tid =tdo.getTid();
			String diagnosis = tdo.getDiagnosis();
			Date date = tdo.getRadiology().getDate();
			try {
				patient.addRadiology(diagnosis, date, provider);
				provider.addRadiology(tid);
			} catch (TreatmentExn e) {
				throw new TreatmentNotFoundExn(e.toString());
			}
			
		}
		
		if (treatmentType.equals("surgery")) {
			long tid = tdo.getTid();
			String diagnosis = tdo.getDiagnosis();
			Date date = tdo.getSurgery().getDate();
			try {
				patient.addSurgery(diagnosis, date, provider);
				provider.addSurgery(tid);
			} catch (TreatmentExn e) {
				throw new TreatmentNotFoundExn(e.toString());
			}
		}
		*/

	}
	

	
	/**
	 * @throws TreatmentExn 
	 * @see IProviderService#deleteTreatment(long NPI, long tid)
	 * 5. Delete a treatment for a patient
	 */
	@Override
	public void deleteTreatment(long pid, long NPI, long tid) throws PatientNotFoundExn,
			ProviderNotFoundExn, ProviderServiceExn {
		try {
			Treatment treatment = treatmentDAO.getTreatmentByDbId(tid);
			Patient patient = patientDAO.getPatientByPatientId(pid);
			Provider provider = providerDAO.getProviderByNPI(NPI);
			if(treatment.getPatient().getPatientId() !=pid){
				throw new ProviderServiceExn("The patient can not delete the treatment: tid: "+tid+" by patient: "+ patient.getName());
			}
			provider.deleteTreatment(tid);
		} catch (PatientExn e) {
			throw new ProviderNotFoundExn(e.toString());
		} catch (ProviderExn e) {
			throw new ProviderNotFoundExn(e.toString());
		} catch (TreatmentExn e) {
			throw new ProviderServiceExn(e.toString());
		} 
	}
	
	/**
	 * @throws ProviderExn 
	 * @throws TreatmentExn 
	 * @throws PatientExn 
	 * @see IProviderService#getTreatments(long pid, long NPI, long[] tids)
	 * 6. Obtaining a list of treatment DTOs, for all treatments for a patient that are supervised by this provider
	 */
	@Override
	public TreatmentDto[] getTreatmentsByPaPr(long pid, long NPI)
			throws ProviderNotFoundExn, TreatmentNotFoundExn, PatientNotFoundExn {
		try {
			Patient patient = patientDAO.getPatientByPatientId(pid);
			Provider provider = providerDAO.getProviderByNPI(NPI);
			List<Long> tids = provider.getTreatmentIdsBP(patient);
			TreatmentDto[] treatments = new TreatmentDto[tids.size()];
			TreatmentPDOtoDTO visitor = new TreatmentPDOtoDTO();
			for (int i = 0; i < tids.size(); i++) {
				provider.visitTreatment(tids.get(i), visitor);
				treatments[i] = visitor.getDTO();
			}
			return treatments;
		} catch (PatientExn e) {
			throw new PatientNotFoundExn(e.toString());
		} catch (ProviderExn e) {
			throw new ProviderNotFoundExn(e.toString());
		} catch (TreatmentExn e) {
			throw new TreatmentNotFoundExn(e.toString());
		} 
	}
	/**
	 * @throws TreatmentExn 
	 * @see IProviderService#getTreatments(long NPI, long[] tids)
	 * 7. Obtaining a list of treatment DTOs for the treatments supervised by this provider 
	 */
	@Override
	public TreatmentDto[] getTreatmentsByProvider(long NPI, long[] tids)
			throws ProviderNotFoundExn, ProviderServiceExn {
		try {
			Provider provider = providerDAO.getProviderByNPI(NPI);
			TreatmentDto[] treatments = new TreatmentDto[tids.length];
			TreatmentPDOtoDTO visitor = new TreatmentPDOtoDTO();
			for (int i = 0; i < tids.length; i++) {
				provider.visitTreatment(tids[i], visitor);
				treatments[i] = visitor.getDTO();
			}
			return treatments;
		} catch (ProviderExn e) {
			throw new ProviderNotFoundExn(e.toString());
		} catch (TreatmentExn e) {
			throw new ProviderServiceExn(e.toString());
		}
	}
	
	/**
	 * @throws TreatmentExn 
	 * @see IProviderService#createTreatmentByIdNPI(long id, long NPI,TreatmentDto tdo)
	 * Delete a provider
	 */
	@Override
	public void deleteProvider(long NPI, String name) throws ProviderServiceExn {
		try {
			Provider provider = providerDAO.getProviderByNPI(NPI);
			if(!name.equals(provider.getName())) {
				throw new ProviderServiceExn(
						"Tried to delete wrong provider: name = " + name
						+ ", NPI = " + NPI);
			} else {
				providerDAO.deleteProvider(provider);
			}
		} catch (ProviderExn e) {
			throw new ProviderServiceExn(e.toString());
		}
	}


	static class TreatmentPDOtoDTO implements ITreatmentVisitor {

		private TreatmentDto dto;

		public TreatmentDto getDTO() {
			return dto;
		}

		@Override
		public void visitDrugTreatment(long tid, String diagnosis, String drug,
				float dosage, Provider provider) {
			dto = new TreatmentDto();
			//dto.setTid(tid);
			dto.setDiagnosis(diagnosis);
			//dto.setTreatmentName("drugTreatment");
			DrugTreatmentType drugInfo = new DrugTreatmentType();
			drugInfo.setDosage(dosage);
			drugInfo.setName(drug);
			drugInfo.setPhysician("physician");
			dto.setDrugTreatment(drugInfo);
		}

		@Override
		public void visitRadiology(long tid, String diagnosis,
				Date raddate, Provider provider) {
			dto = new TreatmentDto();
			//dto.setTid(tid);
			dto.setDiagnosis(diagnosis);
			//dto.setTreatmentName("radiology");
			RadiologyType radiologyInfo = new RadiologyType();
			radiologyInfo.setDate(raddate);
			radiologyInfo.setRadiologist("Radiologist");
			dto.setRadiology(radiologyInfo);
		}

		@Override
		public void visitSurgery(long tid, String diagnosis,
				Date surdate, Provider provider) {
			dto = new TreatmentDto();
			//dto.setTid(tid);
			dto.setDiagnosis(diagnosis);
			//dto.setTreatmentName("surgery");
			SurgeryType surgeonInfo = new SurgeryType();
			surgeonInfo.setDate(surdate);
			surgeonInfo.setSurgeon("Surgeon");
			dto.setSurgery(surgeonInfo);
		}

	}
	
	@Override 
	public int deleteAll () throws ProviderServiceExn {
		try {
			treatmentDAO.deleteAll();
			return providerDAO.deleteAll();
		}catch (ProviderExn e) {
			throw new ProviderServiceExn(e.toString());
		}catch (TreatmentExn e) {
			throw new ProviderServiceExn(e.toString());
		}
	}


	@Resource(name = "SiteInfo")
	private String siteInformation;

	@Override
	public String siteInfo() {
		return siteInformation;
	}

}

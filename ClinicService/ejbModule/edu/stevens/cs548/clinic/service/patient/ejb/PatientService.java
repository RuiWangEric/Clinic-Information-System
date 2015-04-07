package edu.stevens.cs548.clinic.service.patient.ejb;

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
import edu.stevens.cs548.clinic.domain.ITreatmentDAO.TreatmentExn;
import edu.stevens.cs548.clinic.domain.ITreatmentVisitor;
import edu.stevens.cs548.clinic.domain.Patient;
import edu.stevens.cs548.clinic.domain.PatientDAO;
import edu.stevens.cs548.clinic.domain.PatientFactory;
import edu.stevens.cs548.clinic.domain.Provider;
import edu.stevens.cs548.clinic.domain.ProviderDAO;
import edu.stevens.cs548.clinic.service.dto.patient.PatientDTO;
import edu.stevens.cs548.clinic.service.dto.treatment.DrugTreatmentType;
import edu.stevens.cs548.clinic.service.dto.treatment.RadiologyType;
import edu.stevens.cs548.clinic.service.dto.treatment.SurgeryType;
import edu.stevens.cs548.clinic.service.dto.treatment.TreatmentDto;

/**
 * Session Bean implementation class PatientService
 */
@Stateless(name = "PatientServiceBean")
public class PatientService implements IPatientServiceLocal,
		IPatientServiceRemote {

	private PatientFactory patientFactory;

	private IPatientDAO patientDAO;
	private IProviderDAO providerDAO;

	/**
	 * Default constructor.
	 */
	public PatientService() {
		patientFactory = new PatientFactory();
	}

	@PersistenceContext(unitName = "ClinicDomain")
	private EntityManager em;

	@PostConstruct
	private void initialize() {
		patientDAO = new PatientDAO(em);
		providerDAO = new ProviderDAO(em);
		//new TreatmentDAO(em);
	}

	/**
	 * @param age
	 * @see IPatientService#createPatient(String, Date, long, int) 
	 * Patient service facade: 
	 * 1. Add a patient to a clinic
	 */
	public long createPatient( long patientId, String name, Date dob, int age)
			throws PatientServiceExn {
		Patient patient = this.patientFactory.createPatient(patientId, name,
				dob, age);
		try {
			patientDAO.addPatient(patient);
		} catch (PatientExn e) {
			throw new PatientServiceExn(e.toString());
		}
		return patient.getId();
	}

	/**
	 * @see IPatientService#getPatientsByNameDob(String, Date) 
	 * Patient service facade: 
	 * 2. Obtaining a list of patient DTOs, given a patient name and dob
	 */
	public PatientDTO[] getPatientsByNameDob(String name, Date dob) throws PatientServiceExn {
		List<Patient> patients = patientDAO.getPatientByNameDob(name, dob);
		PatientDTO[] dto = new PatientDTO[patients.size()];
		for (int i = 0; i < dto.length; i++) {
			dto[i] = new PatientDTO(patients.get(i));
		}
		return dto;
	}

	/**
	 * @see IPatientService#getPatientByPatId(long) 
	 * Patient service facade: 
	 * 3. Obtaining a single patient DTO, given a patient identifier
	 */
	public PatientDTO getPatientByPatId(long pid) throws PatientServiceExn {
		try {
			Patient patient = patientDAO.getPatientByPatientId(pid);
			return new PatientDTO(patient);
		} catch (PatientExn e) {
			throw new PatientServiceExn(e.toString());
		}
	}

	/**
	 * @see IPatientService#getPatientByDbId(long) 
	 * Patient service facade: 
	 * 4. Obtaining a single patient DTO, given a patient key
	 */
	public PatientDTO getPatientByDbId(long id) throws PatientServiceExn {
		try {
			Patient patient = patientDAO.getPatientByDbId(id);
			return new PatientDTO(patient);
		} catch (PatientExn e) {
			throw new PatientServiceExn(e.toString());
		}
	}

	/**
	 * @see IPatientService#deletePatient(String, long)
	 *  Patient service facade:
	 *  5. Deleting a patient record, given the patient name and patient key
	 */
	public void deletePatient(String name, long id) throws PatientServiceExn {
		try {
			Patient patient = patientDAO.getPatientByDbId(id);
			if (!name.equals(patient.getName())) {
				throw new PatientServiceExn(
						"Tried to delete wrong patient: name = " + name
								+ ", id = " + id);
			} else {
				patientDAO.deletePatient(patient);
			}
		} catch (PatientExn e) {
			throw new PatientServiceExn(e.toString());
		}
	}

	/**
	 * Patient service facade: 
	 * 6. Obtaining a list of treatment DTOs for a patient, given that patient's key
	 */
	@Override
	public TreatmentDto[] getTreatments(long id, long[] tids)
			throws PatientNotFoundExn, TreatmentNotFoundExn, PatientServiceExn {
		try {
			Patient patient = patientDAO.getPatientByDbId(id);
			TreatmentDto[] treatments = new TreatmentDto[tids.length];
			TreatmentPDOtoDTO visitor = new TreatmentPDOtoDTO();
			for (int i = 0; i < tids.length; i++) {
				patient.visitTreatment(tids[i], visitor);
				treatments[i] = visitor.getDTO();
			}
			return treatments;
		} catch (PatientExn e) {
			throw new PatientNotFoundExn(e.toString());
		} catch (TreatmentExn e) {
			throw new PatientServiceExn(e.toString());
		}
	}

	/**
	 * Patient service facade: 
	 * 7. Delete treatment for a patient, given that patient's key
	 */
	@Override
	public void deleteTreatment(long id, long tid) throws PatientNotFoundExn,
			TreatmentNotFoundExn, PatientServiceExn {
		try {
			Patient patient = patientDAO.getPatientByDbId(id);
			patient.deleteTreatment(tid);
		} catch (PatientExn e) {
			throw new PatientNotFoundExn(e.toString());
		} catch (TreatmentExn e) {
			throw new PatientServiceExn(e.toString());
		}
	}

	/**
	 * Add specific treatment
	 */
	@Override
	public long addDrugTreatment(long id, long NPI, String diagnosis, String drug,
			float dosage) throws PatientNotFoundExn, ProviderNotFoundExn {
		try {
			Patient patient = patientDAO.getPatientByDbId(id);
			Provider provider =providerDAO.getProviderByNPI(NPI);
			long tid = patient.addDrugTreatment(diagnosis, drug, dosage, provider);
			return tid;
		} catch (PatientExn e) {
			throw new PatientNotFoundExn(e.toString());
		} catch (ProviderExn e) {
			throw new ProviderNotFoundExn(e.toString());
		}
	}

	@Override
	public long addRadiology(long id, long NPI, String diagnosis, Date raddate) 
			throws PatientNotFoundExn, ProviderNotFoundExn {
		try {
			Patient patient = patientDAO.getPatientByDbId(id);
			Provider provider =providerDAO.getProviderByNPI(NPI);
			long tid = patient.addRadiology(diagnosis, raddate, provider);
			return tid;
		} catch (PatientExn e) {
			throw new PatientNotFoundExn(e.toString());
		} catch (ProviderExn e) {
			throw new ProviderNotFoundExn(e.toString());
		}

	}

	@Override
	public long addSurgery(long id, long NPI, String diagnosis, Date surdate) 
			throws PatientNotFoundExn, ProviderNotFoundExn {
		try {
			Patient patient = patientDAO.getPatientByDbId(id);
			Provider provider =providerDAO.getProviderByNPI(NPI);
			long tid = patient.addSurgery(diagnosis, surdate, provider);
			return tid;
		} catch (PatientExn e) {
			throw new PatientNotFoundExn(e.toString());
		} catch (ProviderExn e) {
			throw new ProviderNotFoundExn(e.toString());
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
			DrugTreatmentType drugInfo = new DrugTreatmentType();
			drugInfo.setDosage(dosage);
			drugInfo.setName(drug);
			//drugInfo.setPhysician("physician");
			dto.setDrugTreatment(drugInfo);
		}

		@Override
		public void visitRadiology(long tid, String diagnosis,
				Date raddate, Provider provider) {
			dto = new TreatmentDto();
			//dto.setTid(tid);
			dto.setDiagnosis(diagnosis);
			RadiologyType radiologyInfo = new RadiologyType();
			radiologyInfo.setDate(raddate);
			//radiologyInfo.setRadiologist("Radiologist");
			dto.setRadiology(radiologyInfo);
		}

		@Override
		public void visitSurgery(long tid, String diagnosis,
				Date surdate, Provider provider) {
			dto = new TreatmentDto();
			//dto.setTid(tid);
			dto.setDiagnosis(diagnosis);
			SurgeryType surgeonInfo = new SurgeryType();
			surgeonInfo.setDate(surdate);
			//surgeonInfo.setSurgeon("Surgeon");
			dto.setSurgery(surgeonInfo);
		}
	}

	@Override 
	public int deleteAll () throws PatientServiceExn {
		try {
			return patientDAO.deleteAll();
		} catch (PatientExn e) {
			throw new PatientServiceExn(e.toString());
		}
	}

	@Resource(name="SiteInfo")
	private String siteInformation;
	
	@Override
	public String siteInfo() {
		return siteInformation;
	}


}

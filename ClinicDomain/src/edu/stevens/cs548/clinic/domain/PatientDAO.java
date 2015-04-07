package edu.stevens.cs548.clinic.domain;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

//import edu.stevens.cs548.clinic.domain.ITreatmentDAO.TreatmentExn;

public class PatientDAO implements IPatientDAO {

	// @PersistenceContext
	private EntityManager em;
	private TreatmentDAO treatmentDAO;

	/*
	 * 2. Retrieve a patient aggregate from the system, given the primary key
	 * for the patient
	 */
	@Override
	public Patient getPatientByDbId(long id) throws PatientExn {
		Patient p = em.find(Patient.class, id);
		if (p == null) {
			throw new PatientExn("Patient not found: primary key = " + id);
		} else {
			p.setTreatmentDAO(this.treatmentDAO);
			return p;
		}
	}

	/*
	 * 4. Retrieve a patient aggregate from the system, given the patient
	 * identifier for the patient
	 */
	@Override
	public Patient getPatientByPatientId(long pid) throws PatientExn {
		TypedQuery<Patient> query = em.createNamedQuery(
				"SearchPatientByPatientID", Patient.class).setParameter("pid",
				pid);
		List<Patient> patients = query.getResultList();
		if (patients.size() > 1)
			throw new PatientExn("Duplicate patient records: patient id = "
					+ pid);
		else if (patients.size() < 1)
			throw new PatientExn("Patient not found: patient id = " + pid);
		else {
			Patient p = patients.get(0);
			p.setTreatmentDAO(this.treatmentDAO);
			return p;
		}
	}

	/* 5. Given a patient's name and birthday, retrieve a list of patient */
	@Override
	public List<Patient> getPatientByNameDob(String name, Date dob) {
		TypedQuery<Patient> query = em
				.createNamedQuery("SearchPatientByNameDOB", Patient.class)
				.setParameter("name", name).setParameter("dob", dob);
		List<Patient> patients = query.getResultList();
		if (patients.size() < 1) {
			return null;
		} else {
			for (Patient p : patients) {
				p.setTreatmentDAO(this.treatmentDAO);
			}
		}
		return patients;
	}

	/*--------------------------------add patient-------------------------------------------------------------------*/

	int getAgeByDob(Date patientDob) throws PatientExn {
		Calendar nt = Calendar.getInstance();
		Calendar dobt = Calendar.getInstance();
		dobt.setTime(patientDob);
		if (dobt.after(nt)) {
			throw new PatientExn("Patient can't be born in the future");
		}
		int yearNt = nt.get(Calendar.YEAR);
		int yearDob = dobt.get(Calendar.YEAR);
		int Age = yearNt - yearDob;
		int monthNt = nt.get(Calendar.MONTH);
		int monthDob = dobt.get(Calendar.MONTH);
		if (monthDob > monthNt) {
			Age--;
		} else if (monthNt == monthDob) {
			int dayNt = nt.get(Calendar.DATE);
			int dayDob = dobt.get(Calendar.DATE);
			if (dayDob > dayNt) {
				Age--;
			}
		}
		return Age;

	}

	/* 1. Add a new patient to the clinic */
	@Override
	public long addPatient(Patient patient) throws PatientExn{
		long pid = patient.getPatientId();
		int age = patient.getAge();
		if(age != getAgeByDob(patient.getBirthDate())){
			throw new PatientExn("Insertion: The date of birth is not corresponding to the age.\n");
		} else {	
			TypedQuery<Patient> query = em.createNamedQuery(
					"SearchPatientByPatientID", Patient.class).setParameter(
					"pid", pid);
			List<Patient> patients = query.getResultList();
			if (patients.size() < 1) {
				em.persist(patient);
				patient.setTreatmentDAO(this.treatmentDAO);
				return patient.getId();
			} else {
				Patient patient2 = this.getPatientByPatientId(pid);
				throw new PatientExn("Insertion: Patient with patient id ("
						+ pid + ") already exists\n**Name: "
						+ patient2.getName());
			}
		}
	}

	/*--------------------------------delete patient-------------------------------------------------------------------*/

	/*
	 * 3. Delete a patient aggregate from the system, given its primary key. All
	 * treatments associated with this patient should also be deleted.
	 */
	@Override
	public void deletePatientDbId(long id) throws PatientExn {
		Patient patientById = em.find(Patient.class, id);
		//Patient patientByName = em.find(Patient.class, name);
		// List<Long> tid = patientById.getTreatmentIds();
		if (patientById == null ) {
			throw new PatientExn("Remove: Patient not found: primary key: "
					+ id);
		} else {
			em.createQuery("delete from Treatment t where t.patient.id = :id")
					.setParameter("id", patientById.getId())
					.executeUpdate();
			em.remove(patientById);
		}
	}

	/*
	 * @Override public void deletePatient(long id) throws PatientExn { Patient
	 * patient = getPatientByPatientId(id); List<Long> tid =
	 * patient.getTreatmentIds(); for(int i = 0; i<tid.size(); i++){ try {
	 * patient.deleteTreatment(tid.get(i)); } catch (TreatmentExn e) {
	 * e.printStackTrace(); } } em.remove(patient); }
	 */
	@Override
	public void deletePatient(Patient patient) throws PatientExn {
		//em.createQuery("delete from Treatment t where t.patient.id = :id")
		//		.setParameter("id", patient.getId()).executeUpdate();
		em.remove(patient);

	}

	@Override
	public int deleteAll() throws PatientExn {
		Query query = em.createQuery("delete from Patient p");
		int numPatient = query.executeUpdate();
		return numPatient;
	}

	public PatientDAO(EntityManager em) {
		this.em = em;
		this.treatmentDAO = new TreatmentDAO(em);
	}

}

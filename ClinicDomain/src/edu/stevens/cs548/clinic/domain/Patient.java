package edu.stevens.cs548.clinic.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import edu.stevens.cs548.clinic.domain.ITreatmentDAO.TreatmentExn;
import static javax.persistence.CascadeType.REMOVE;

/**
 * Entity implementation class for Entity: Patient
 *
 */

@Entity
@NamedQueries({
	@NamedQuery(
			name="SearchPatientByPatientID",
			query="select p from Patient p where p.patientId = :pid"),
	@NamedQuery(
			name="SearchPatientByNameDOB",
			query="select p from Patient p where p.name = :name and p.birthDate = :dob")
})

@Table(name="PATIENT")
public class Patient implements Serializable {

	
	private static final long serialVersionUID = 1L;
	
	/*2. Retrieve a patient aggregate from the system, given the primary key for the patient */
	
	@Id
	@GeneratedValue
	private long id;
	
	private long patientId;
	private String name;
	private int age;
	
	@Temporal(TemporalType.DATE)
	private Date birthDate;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getPatientId() {
		return patientId;
	}

	public void setPatientId(long patientId) {
		this.patientId = patientId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}
	
	
	@OneToMany(cascade = REMOVE, mappedBy = "patient")
	@OrderBy
	private List<Treatment> treatments;
		
	public List<Treatment> getTreatments(){
		return treatments;
	}
	
	public void setTreatments(List<Treatment> treatments){
		this.treatments = treatments;
	}
	

	
/*---------------------------------------------------------------------------------------------------*/	
	@Transient
	private ITreatmentDAO treatmentDAO;
	
	public void setTreatmentDAO (ITreatmentDAO tdao){
		this.treatmentDAO = tdao;
	}
	
	void addTreatment (Treatment t) {
		this.treatmentDAO.addTreatment(t);
		this.getTreatments().add(t);
		if(t.getPatient()!=this)
			t.setPatient(this);
	}
/*---------------------------------------------------------------------------------------------------*/	
	
	
	public long addDrugTreatment (String diagnosis, String drug, float dosage, Provider provider){
		/* TODO: Add provider parameter when adding treatments to a patient */
		DrugTreatment treatment = new DrugTreatment();
		treatment.setDiagnosis(diagnosis);
		treatment.setDrug(drug);
		treatment.setDosage(dosage);
		treatment.setProvider(provider);
		this.addTreatment(treatment);
		return treatment.getId();
	}
	
	public long addRadiology (String diagnosis, Date raddate, Provider provider){
		/* TODO: Add provider parameter when adding treatments to a patient */
		Radiology treatment = new Radiology();
		treatment.setDiagnosis(diagnosis);
		treatment.setRaddates(raddate);
		treatment.setProvider(provider);
		this.addTreatment(treatment);
		return treatment.getId();
	}
	
	public long addSurgery (String diagnosis, Date surdate, Provider provider) {
		/* TODO: Add provider parameter when adding treatments to a patient */
		Surgery treatment = new Surgery();
		treatment.setDiagnosis(diagnosis);
		treatment.setSurdate(surdate);
		treatment.setProvider(provider);
		this.addTreatment(treatment);
		return treatment.getId();
	}
	

/*---------------------------------------------------------------------------------------------------*/	
	
	/*
	 * 10. A patient aggregate does not return a set of treatment entities directly to a client,
	 * since this would violate the encapsulation of the aggregate pattern.
	 * Instead a patient aggregate provides access to its treatment entities with these operations:
	 * a. one operation returns a list of treatment identifiers for that patient's treatments.
	 */
	public List<Long> getTreatmentIds() {
		List<Long> tids = new ArrayList<Long>();
		for (Treatment t : this.getTreatments()) {
			tids.add(t.getId());
		}
		return tids;
	}
	
	/*
	 * 10. A patient aggregate does not return a set of treatment entities directly to a client,
	 * since this would violate the encapsulation of the aggregate pattern.
	 * Instead a patient aggregate provides access to its treatment entities with these operations:
	 * b. A second operation allows a particular treatment, identified by a treatment identifier, to be visited.
	 */
	public void visitTreatment (long tid, ITreatmentVisitor visitor) throws TreatmentExn{
		Treatment t = treatmentDAO.getTreatmentByDbId(tid);
		if(t.getPatient() != this) {
			throw new TreatmentExn ("Inappropriate treatment access: patient = " + id
						+ ", treatment = " + tid);
		}
		t.visit(visitor);
	}
	
	public void deleteTreatment (long tid) throws TreatmentExn {
		
		Treatment t = treatmentDAO.getTreatmentByDbId(tid);
		if(t.getPatient() != this) {
			throw new TreatmentExn ("Inappropriate treatment access: patient = " + id
						+ ", treatment = " + tid);
		}	
		treatmentDAO.deleteTreatment(t);
	}
	
	public void visitTreatments (ITreatmentVisitor visitor) {
		for (Treatment t : this.getTreatments()) {
			t.visit(visitor);
		}
	}
	
	public Patient() {
		super();
		treatments = new ArrayList<Treatment>();
	}
   
}

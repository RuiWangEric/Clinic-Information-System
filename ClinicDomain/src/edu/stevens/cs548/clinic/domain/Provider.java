package edu.stevens.cs548.clinic.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import edu.stevens.cs548.clinic.domain.ITreatmentDAO.TreatmentExn;
import static javax.persistence.CascadeType.REMOVE;

/**
 * Entity implementation class for Entity: Provider
 *
 */

@Entity
@NamedQueries({
	@NamedQuery(
			name="SearchProviderByNPI",
			query="select p from Provider p where p.NPI = :NPI"),
	@NamedQuery(
			name="SearchProviderByName",
			query="select p from Provider p where p.name = :name"),
	@NamedQuery(
			name="SearchProviderByNameProviderType",
			query="select p from Provider p where p.name = :name and p.providerType = :providerType")
	
})

@Table(name="PROVIDER")
public class Provider implements Serializable {

	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	private long id;
	private long NPI;
	private String name;
	

	//@Column(name="TTYPE", length=4)  /* add this would be limited the length of string of providerType*/
	private String providerType;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getNPI() {
		return NPI;
	}

	public void setNPI(long NPI) {
		this.NPI = NPI;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getProviderType() {
		return providerType;
	}

	public void setProviderType(String providerType) {
		this.providerType = providerType;
	}
	
	
	@OneToMany(cascade = REMOVE, mappedBy = "provider")
	@OrderBy
	private List<Treatment> treatments;
		
	protected List<Treatment> getTreatments(){
		return treatments;
	}
	
	protected void setTreatments(List<Treatment> treatments){
		this.treatments = treatments;
	}
	
	@Transient
	public ITreatmentDAO treatmentDAO;
	
	public void setTreatmentDAO (ITreatmentDAO tdao){
		this.treatmentDAO = tdao;
	}
	

	
/*----------------------------add treatment-----------------------------------------------------------------------*/	
	/*
	 * 9. Add a treatment for a patient
	 * Define one operation, in the provider aggregate.
	 * This operation should return the primary key 
	 * for the new treatment object in the database
	 */

//Add treatment to this provider,if the treatment doesn't has this provider,then add the provider to the treatment.	
	public void addTreatment (Treatment t) {
		this.treatmentDAO.addTreatment(t);
		this.getTreatments().add(t);
		if(t.getProvider() != this)
			t.setProvider(this);
	}
	
	public long addDrugTreatment (String diagnosis, String drug, float dosage, Patient patient){
		/* TODO: Add provider parameter when adding treatments to a patient */
		DrugTreatment treatment = new DrugTreatment();
		treatment.setDiagnosis(diagnosis);
		treatment.setDrug(drug);
		treatment.setDosage(dosage);
		treatment.setPatient(patient);
		treatment.setProvider(this);
		patient.addTreatment(treatment);
		long id = this.treatmentDAO.addTreatment(treatment);
		return id;
	}
	
	public long addRadiology (String diagnosis, Date raddate, Patient patient){
		/* TODO: Add provider parameter when adding treatments to a patient */
		Radiology treatment = new Radiology();
		treatment.setDiagnosis(diagnosis);
		treatment.setRaddates(raddate);
		treatment.setPatient(patient);
		treatment.setProvider(this);
		patient.addTreatment(treatment);
		long id = this.treatmentDAO.addTreatment(treatment);
		return id;
	}
	
	public long addSurgery ( String diagnosis, Date surdate, Patient patient) {
		/* TODO: Add provider parameter when adding treatments to a patient */
		Surgery treatment = new Surgery();
		treatment.setDiagnosis(diagnosis);
		treatment.setSurdate(surdate);
		treatment.setPatient(patient);
		treatment.setProvider(this);
		long id = this.treatmentDAO.addTreatment(treatment);
		return id;
	}

	
    /*
     * Assignment6 Provider.5

    public void addDrugTreatment (long tid) {
    	Treatment treatment = treatmentDAO.getTreatmentByDbId(tid);
    	this.addTreatment(treatment);
    }
    
    public void addRadiology (long tid) {
    	Treatment treatment = treatmentDAO.getTreatmentByDbId(tid);
    	this.addTreatment(treatment);
    }
    
    public void addSurgery (long tid) {
    	Treatment treatment = treatmentDAO.getTreatmentByDbId(tid);
    	this.addTreatment(treatment);
    }
         */
/*----------------------------get treatment-----------------------------------------------------------------------*/	
	
	/*11-a. Return a list of all treatments associated with that provider	*/
	public List<Long> getTreatmentIds() throws TreatmentExn{
		List<Long> tids = new ArrayList<Long>();
		List<Treatment> treatments = this.getTreatments();
		if(treatments.size()<1){
			throw new TreatmentExn("No treatment under this provider");
		}else{
			for (Treatment t : this.getTreatments()) {
				tids.add(t.getId());
			}
			return tids;
		}
	}
/*
    public List<Long> getTreatmentIds(){
    	List<Long> tids = new ArrayList<Long>();
    	for(Treatment t : this.getTreatments()){
    		tids.add(t.getId());
    	}
    	return tids;
    }
 */   
	
	/*11-b. Return a list of all treatments for treatments administered to a particular patient supervised by that provider*/
	public List<Long> getTreatmentIdsBP(Patient p) throws TreatmentExn{
		List<Long> tids = new ArrayList<Long>();
		List<Treatment> treatments = this.getTreatments();
		for(Treatment treatment : treatments){
			if(treatment.getPatient()==p){
				tids.add(treatment.getId());
			}	
		}
		if(tids.size()<1){
			throw new TreatmentExn("There are no treatments for that patient under this provider. "
					+ "The infomation is that the patient: "+p.getId()+"; Provider: "+this.id);
		}else {
			return tids;
		}
	}
	
	/*11-c. Visit a particular treatment specified by a treatment identifier(primary key)*/
	public void visitTreatment (long tid, ITreatmentVisitor visitor) throws TreatmentExn {
		Treatment t = treatmentDAO.getTreatmentByDbId(tid);
		if(t.getProvider() != this) {
			throw new TreatmentExn ("Inappropriate treatment access: patient = " + id
						+ ", treatment = " + tid);
		}
		t.visit(visitor);
	}
	
	/*11-d. Allows a treatment to be deleted and make sure that the specified treatment is associated with this provider*/
	public void deleteTreatment (long tid) throws TreatmentExn {
		Treatment t = treatmentDAO.getTreatmentByDbId(tid);
		if(t.getProvider() != this) {
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
	
	public Provider() {
		super();
		//treatments = new ArrayList<Treatment>();
	}
   
}

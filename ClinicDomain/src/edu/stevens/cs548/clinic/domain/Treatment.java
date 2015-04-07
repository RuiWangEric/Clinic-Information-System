package edu.stevens.cs548.clinic.domain;

import java.io.Serializable;

import javax.persistence.*;

//import static javax.persistence.CascadeType.REMOVE;

/**
 * Entity implementation class for Entity: Treatment
 *
 */

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name="TTYPE")
@Table(name="TREATMENT")
public abstract class Treatment implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	private long id;
	private String diagnosis;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public String getDiagnosis() {
		return diagnosis;
	}

	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}
	
	@Column(name ="TTYPE",length=2)
	private String treatmentType;
	
	public String getTreatmentType() {
		return treatmentType;
	}

	public void setTreatmentType(String treatmentType) {
		this.treatmentType = treatmentType;
	}

/*-------------------------Provider-----------------------------------------------*/	
	
	@ManyToOne
	@JoinColumn(name = "Provider_fk", referencedColumnName= "id")
	private Provider provider;
	
	public Provider getProvider() {
		return provider;
	}

	public void setProvider(Provider provider) {
		this.provider = provider;
		if(!provider.getTreatments().contains(this))
			provider.addTreatment(this);
	}
	
	
/*-------------------------Patient-----------------------------------------------*/
	
	@ManyToOne
	@JoinColumn(name = "Patient_fk", referencedColumnName= "id")
	private Patient patient;
	
	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
		if(!patient.getTreatments().contains(this))
			patient.addTreatment(this);
	}
	
	public abstract void visit(ITreatmentVisitor visitor);
	
	public Treatment() {
		super();
	}
   
}

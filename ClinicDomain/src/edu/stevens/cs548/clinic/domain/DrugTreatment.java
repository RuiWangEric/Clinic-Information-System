package edu.stevens.cs548.clinic.domain;

import edu.stevens.cs548.clinic.domain.Treatment;
import java.io.Serializable;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: DrugTreatment
 *
 */

@Entity
@DiscriminatorValue("D")

public class DrugTreatment extends Treatment implements Serializable {
	
	private static final long serialVersionUID = 1L;
	

	private String drug;
	private float dosage;
	
	@ManyToOne
	@JoinColumn(name = "provider_fk", referencedColumnName = "id")
	private Provider provider;
	
	
	public String getDrug() {
		return drug;
	}

	public void setDrug(String drug) {
		this.drug = drug;
	}

	public float getDosage() {
		return dosage;
	}

	public void setDosage(float dosage) {
		this.dosage = dosage;
	}
	
	public Provider getProvider() {
		return provider;
	}

	public void setProvider(Provider provider) {
		this.provider = provider;
		if(provider.getProviderType()!="physician") {
			provider.setProviderType("physician");
		}
	}

	
	public void visit (ITreatmentVisitor visitor) {
		visitor.visitDrugTreatment(this.getId(), 
								   this.getDiagnosis(),
								   this.getDrug(),
								   this.getDosage(),
								   this.getProvider());
	}

	public DrugTreatment() {
		super();
		this.setTreatmentType("D");
	}
   
}

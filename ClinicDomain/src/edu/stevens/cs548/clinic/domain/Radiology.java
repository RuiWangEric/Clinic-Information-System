package edu.stevens.cs548.clinic.domain;

import edu.stevens.cs548.clinic.domain.Treatment;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: RadiologyTreatment
 *
 */
@Entity
@DiscriminatorValue("R")

public class Radiology extends Treatment implements Serializable {

	
	private static final long serialVersionUID = 1L;
	

	@Temporal(TemporalType.DATE)
	private Date raddate;
	
	@ManyToOne
	@JoinColumn(name="provider_fk", referencedColumnName = "id")
	private Provider provider;

	
	public Date getRaddates() {
		return raddate;
	}

	public void setRaddates(Date raddate) {
		this.raddate = raddate;
	}
	
	public Provider getProvider() {
		return provider;
	}

	public void setProvider(Provider provider) {
		this.provider = provider;
		if (provider.getProviderType()!="radiologist") {
			provider.setProviderType("radiologist");
		}
	}

	@Override
	public void visit (ITreatmentVisitor visitor) {
		visitor.visitRadiology(this.getId(), 
							   this.getDiagnosis(),
							   this.getRaddates(),
							   this.getProvider());
		
	}

	public Radiology() {
		super();
		this.setTreatmentType("R");
		//raddates = new ArrayList<Date>();
	}


   
}

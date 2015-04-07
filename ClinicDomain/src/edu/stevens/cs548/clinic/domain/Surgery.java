package edu.stevens.cs548.clinic.domain;

import edu.stevens.cs548.clinic.domain.Treatment;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: SurgeryTreatment
 *
 */
@Entity
@DiscriminatorValue("S")

public class Surgery extends Treatment implements Serializable {

	
	private static final long serialVersionUID = 1L;
	
	@Temporal(TemporalType.DATE)
	private Date surdate;
	@ManyToOne
	@JoinColumn(name = "provider_fk", referencedColumnName = "id")
	private Provider provider;

	
	public Date getSurdate() {
		return surdate;
	}

	public void setSurdate(Date surdate) {
		this.surdate = surdate;
	}
	
	public Provider getProvider() {
		return provider;
	}

	public void setProvider(Provider provider) {
		this.provider = provider;
		if(provider.getProviderType()!="surgeon") {
			provider.setProviderType("surgeon");
		}
	}

	
	public void visit (ITreatmentVisitor visitor) {
		visitor.visitSurgery(this.getId(), 
							this.getDiagnosis(),
				   			this.getSurdate(),
				   			this.getProvider());
	}
	
	public Surgery() {
		super();
		this.setTreatmentType("S");
	}
   
}

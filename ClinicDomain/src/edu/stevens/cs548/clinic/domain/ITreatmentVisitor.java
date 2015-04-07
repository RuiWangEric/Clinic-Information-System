package edu.stevens.cs548.clinic.domain;

import java.util.Date;

public interface ITreatmentVisitor {
	
	public void visitDrugTreatment (long tid,
									String diagnosis,
								    String drug,
							        float dosage,
									Provider provider);
	
	public void visitRadiology (long tid,
								String diagnosis,
								Date raddates,
								Provider provider);
	
	public void visitSurgery (long tid,
							  String diagnosis,
							  Date surdate,
							  Provider provider);
}

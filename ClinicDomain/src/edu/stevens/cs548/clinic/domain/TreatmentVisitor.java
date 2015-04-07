package edu.stevens.cs548.clinic.domain;

import java.util.Date;

public class TreatmentVisitor implements ITreatmentVisitor {

	@Override
	public void visitDrugTreatment(long tid, String diagnosis, String drug,
			float dosage, Provider provider) {
		/*
		System.out
				.print("Visiting drug-treatment for treatment information:\nTreatment Id: "
						+ tid
						+ "\nDiagnosis: "
						+ diagnosis
						+ "\nDrug: "
						+ drug
						+ "\nDosage: "
						+ dosage
						+ "\nProvider: "
						+ provider.getNPI());
						*/
	}

	@Override
	public void visitRadiology(long tid, String diagnosis, Date raddate,
			Provider provider) {
		/*
		System.out
				.print("Visiting radiology for treatment information:\nTreatment Id: "
						+ tid
						+ "\nDiagnosis: "
						+ diagnosis
						+ "\nDate: "
						+ raddate
						+ "\nProvider: "
						+ provider.getNPI());
*/
	}

	@Override
	public void visitSurgery(long tid, String diagnosis, Date surdate,
			Provider provider) {
		/*
		System.out
				.print("Visiting surgery for treatment information:\nTreatment Id: "
						+ tid
						+ "\nDiagnosis: "
						+ diagnosis
						+ "\nDate: "
						+ surdate
						+ "\nProvider: "
						+ provider.getNPI());
*/
	}

}

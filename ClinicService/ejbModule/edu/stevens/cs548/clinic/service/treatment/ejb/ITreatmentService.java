package edu.stevens.cs548.clinic.service.treatment.ejb;

import edu.stevens.cs548.clinic.service.dto.treatment.TreatmentDto;

public interface ITreatmentService {
	
	public class TreatmentServiceExn extends Exception {
		private static final long serialVersionUID = 1L;
		public TreatmentServiceExn (String m) {
			super (m);
		}
	}
	
	public TreatmentDto getTreatmentById (long pid) throws TreatmentServiceExn;

}

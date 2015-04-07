package edu.stevens.cs548.clinic.domain;

import java.util.Date;
import java.util.List;

//import edu.stevens.cs548.clinic.domain.IPatientDAO.PatientExn;


public interface IPatientDAO {
	
	public static class PatientExn extends Exception{
		
		private static final long serialVersionUID = 1L;	
		
		public PatientExn(String msg){
			super(msg);
		}
	}
	
	public Patient getPatientByDbId (long id) throws PatientExn;
	
	public Patient getPatientByPatientId (long pid) throws PatientExn;
	
	public List<Patient> getPatientByNameDob (String name, Date dob);
	
	public long addPatient (Patient pat) throws PatientExn;
	
	public void deletePatientDbId(long id) throws PatientExn;

	public void deletePatient (Patient pat) throws PatientExn;
	
	public int deleteAll() throws PatientExn;


	
}

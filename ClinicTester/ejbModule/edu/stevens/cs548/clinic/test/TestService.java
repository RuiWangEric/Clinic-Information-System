package edu.stevens.cs548.clinic.test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import java.util.logging.Logger;

import edu.stevens.cs548.clinic.service.dto.patient.PatientDTO;
import edu.stevens.cs548.clinic.service.dto.provider.ProviderDTO;
import edu.stevens.cs548.clinic.service.dto.treatment.DrugTreatmentType;
import edu.stevens.cs548.clinic.service.dto.treatment.TreatmentDto;
import edu.stevens.cs548.clinic.service.patient.ejb.IPatientService.PatientNotFoundExn;
import edu.stevens.cs548.clinic.service.patient.ejb.IPatientService.PatientServiceExn;
import edu.stevens.cs548.clinic.service.patient.ejb.IPatientService.ProviderNotFoundExn;
import edu.stevens.cs548.clinic.service.patient.ejb.IPatientService.TreatmentNotFoundExn;
import edu.stevens.cs548.clinic.service.patient.ejb.IPatientServiceLocal;
import edu.stevens.cs548.clinic.service.patient.ejb.PatientService;
import edu.stevens.cs548.clinic.service.provider.ejb.IProviderService.ProviderServiceExn;
import edu.stevens.cs548.clinic.service.provider.ejb.IProviderServiceLocal;
import edu.stevens.cs548.clinic.service.provider.ejb.ProviderService;


@Singleton
@LocalBean
@Startup
public class TestService {
	
	private static Logger logger =
			Logger.getLogger(TestService.class.getCanonicalName());
	
	private static void info (String m) {
		logger.info(m);
	}
	
	@Inject
	private IPatientServiceLocal patientService;
	
	@Inject
	private IProviderServiceLocal providerService;
	
	public TestService () {
		patientService = new PatientService();
		providerService = new ProviderService();
	}
	

	@PostConstruct
	public void init () {
		info ("Initializing the service.");
		
		try {
			int numRemoved = patientService.deleteAll();
			info ("Remove all the patients from the clinic service: "+numRemoved);
		} catch (PatientServiceExn e) {
			e.printStackTrace();
			info ("Remove all the patients fails.");
		}
		
		try {
			int numRemoved = providerService.deleteAll();
			info ("Remove all the providers form the clinic service: "+numRemoved);
		} catch (ProviderServiceExn e) {
			e.printStackTrace();
			info ("Remove all the providers fails.");
		}
		
		
		/* Add patient information.*/
		long[] patientIds ={1001,1002,1003,1004};
		String[] patientNames = {"Eric", "Eric", "Tom", "Stone"};
		String[] dobs = { "01/10/1991", "01/10/1991", "10/10/1996", "07/15/1992"};
		int[] ages = {23,23,18,22};
		DateFormat datef = new SimpleDateFormat("mm/dd/yyyy");
		Date dob = new Date ();
		List<Long> patientPKeys = new ArrayList<Long>();
			
		info ("Patient--Test1: Add a patient to a clinic");
		for (int i=0; i<patientIds.length; i++) {
			try {
				String x = dobs[i];
				dob = datef.parse(x);
				long id = patientService.createPatient(patientIds[i], patientNames[i], dob, ages[i]);
				patientPKeys.add(id);
				info ("New patient created with primary id: "+ id +" PatientId: "+patientIds[i]);
				String names = patientNames[i];
				info ("Name: "+names);
			} catch (PatientServiceExn e) {
				e.printStackTrace();
				info ("New patient created fails");
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		
	
		info ("Patient--Test1: Add a patient identifier already exists in the clinic service");
		try {
			dob = datef.parse("07/15/1992");
			long id = patientService.createPatient(patientIds[3], patientNames[3], dob, 22);
			patientPKeys.add(id);
			info ("New patient created with primary id: "+ id);
		} catch (PatientServiceExn e) {
			info ("Patient already exist!"+e.toString());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		info("Patient--Test1: Patient age provided is not consistent with the dob and current date");
		try {
			dob = datef.parse("01/10/1991");
			long id = patientService.createPatient(patientIds[0], patientNames[0], dob, 2);
			patientPKeys.add(id);
			info ("New patient created with primary id: "+ id);
		} catch (ParseException e) {
			info("Not consistent with the dob and current date!"+e.toString());
		} catch (PatientServiceExn e) {
			e.printStackTrace();
		}
		

		info ("Patient--Test2: Obtaining a list of patient DTOs, given a patient name and dob");
		try {
			PatientDTO[] patients;
			String name = "Eric";
			dob = datef.parse("01/10/1991");
			patients = patientService.getPatientsByNameDob(name, dob);
			String getPatientIds ="";
			for (PatientDTO pdto : patients) {
				getPatientIds += "  " + pdto.getId();
			}
			info ("GetPatientByNameDob: patients primary keys: "+getPatientIds);
		} catch (PatientServiceExn e) {
			info ("GetPatientByNameDob failed! "+e.toString());
		} catch (ParseException e) {
			info (e.toString());
		}
		
		
		
		info ("Patient--Test3: Obtaining a single patientDTO, given a patient identifier");
		try {
			long pid = 1001;
			PatientDTO patient = patientService.getPatientByPatId(pid);
			info("Get paitnet 004: " + pid + " Name: " + patient.getName());
		} catch (PatientServiceExn e) {
			info("GetPatientById Failed! " + e.toString());
		}
	

		info ("Patient--Test4: Obtaining a single patientDTO, given a patient key");
		try {
			long id = 3;
			PatientDTO patient = patientService.getPatientByDbId(id);
			info ("GetPatientByDbId: patient Id: "+ patient.getId() + " Name: " + patient.getName());
		} catch (PatientServiceExn e) {
			info ("GetPaitnetById Failed! "+ e.toString());
		}

		
		info ("Patient--Test6: Obtaining a list of treatment DTOs for a patient. given that patient's key.");
		info ("Provider--Test1: Adding a provider to a clinic");
		/* Add provider information.*/
		long[] NPIs = {101,102,103,104};
		String[] providerNames = {"Provider01","Provider01", "Provider03", "Provider04"};
		String[] providerTypes = {"drugTreatment","radiology", "surgery","drugTreatment"};
		List<Long> providerPKeys = new ArrayList<Long>();
		info("First, Add provider information.");
			try {
				long id1 = providerService.createProvider(NPIs[0], providerNames[0], providerTypes[0]);
				providerPKeys.add(id1);
				info("Provider--Test1: New provider created with primary key: "+ id1 + " NPI: " + NPIs[0] + " Name: "+ providerNames[0] + " providerTypes: "+ providerTypes[0]);
			
				long id2 = providerService.createProvider(NPIs[1], providerNames[1], providerTypes[1]);
				providerPKeys.add(id2);
				info("Provider--Test1: New provider created with primary key: "+ id2 + " NPI: " + NPIs[1] + " Name: "+ providerNames[1] + " prviderTypes: " + providerTypes[1]);
				
				long id3 = providerService.createProvider(NPIs[2], providerNames[2], providerTypes[2]);
				providerPKeys.add(id3);
				info("Provider--Test1: New provider created with primary key: "+ id3 + " NPI: " + NPIs[2] + " Name: "+ providerNames[2]+ " prviderTypes: " + providerTypes[2]);
				
				long id4 = providerService.createProvider(NPIs[3], providerNames[3], providerTypes[3]);
				providerPKeys.add(id4);
				info("Provider--Test1: New provider created with primary key: "+ id4 + " NPI: " + NPIs[3] + " Name: "+ providerNames[3]+ " prviderTypes: " + providerTypes[3]);
				
			} catch (ProviderServiceExn e) {
				info("New provider created Failed! " + e.toString());
			}
			

			
			long[][] treatmentIdsByPatient = new long[4][3];
			List<Long> treatmentIdsByProvider = new ArrayList<Long>();
			String[] datesInString = {"01/20/2014","02/20/2014","03/20/2014","04/20/2014","05/20/2014"};
			List<Date> treatmentDates = new ArrayList<Date>();
			Date radDate = new Date();
			for (String dateInString : datesInString) {
				try{
					radDate = datef.parse(dateInString);
					treatmentDates.add(radDate);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			for(int i=0; i<dobs.length; i++) {
				try {
					long id_1 = patientService.addDrugTreatment(patientPKeys.get(i), NPIs[i], "Headache", "Painkiller", (float)3.0);
					long id_2 = patientService.addDrugTreatment(patientPKeys.get(i), NPIs[i], "Cold", "Aspilin", (float)4.0);
					long id_3 = patientService.addSurgery(patientPKeys.get(i), NPIs[i], "BoneBroke", treatmentDates.get(i));
					treatmentIdsByPatient[i][0] = id_1;
					treatmentIdsByPatient[i][1] = id_2;
					treatmentIdsByPatient[i][2] = id_3;
				} catch (PatientNotFoundExn e ) {
					info("Addtreatment: Patientnotfound: "+e.toString());
				} catch (ProviderNotFoundExn e) {
					info("Addtreatment: ProviderNotFound: "+e.toString());
				}
			}			
			
			
			info ("Provider--Test4: Adding a treatment for a patient");
			try {
				TreatmentDto treat1 = new TreatmentDto(); 
				DrugTreatmentType dt1 = new DrugTreatmentType();
				dt1.setName("PainKiller01");
				dt1.setDosage((float)2.3);
				treat1.setDrugTreatment(dt1);
				treat1.setDiagnosis("Headache01");
				treat1.setNPI(NPIs[0]);
				long tid1 = providerService.createTreatment(patientIds[0], NPIs[0], treat1);
				treatmentIdsByProvider.add(tid1);
				info("Add treatment for patient by provider: treatment id: "+ tid1 +" By provider NPIs: "+NPIs[0]+ "  for patientId: "+ patientIds[0]);
				
				TreatmentDto treat2 = new TreatmentDto(); 
				DrugTreatmentType dt2 = new DrugTreatmentType();
				dt2.setName("PainKiller02");
				dt2.setDosage((float)7.4);
				treat2.setDrugTreatment(dt2);
				treat2.setDiagnosis("Headache02");
				treat2.setNPI(NPIs[1]);
				long tid2 = providerService.createTreatment(patientIds[1], NPIs[1], treat2);
				treatmentIdsByProvider.add(tid2);
				info("Add treatment for patient by provider: treatment id: "+ tid2 +" By provider NPIs: "+NPIs[1]+ "  for patientId: "+ patientIds[1]);
				
				TreatmentDto treat3 = new TreatmentDto(); 
				DrugTreatmentType dt3 = new DrugTreatmentType();
				dt3.setName("PainKiller03");
				dt3.setDosage((float)5.8);
				treat3.setDrugTreatment(dt3);
				treat3.setDiagnosis("Headache03");
				treat3.setNPI(NPIs[2]);
				long tid3 = providerService.createTreatment(patientIds[2], NPIs[2], treat3);
				treatmentIdsByProvider.add(tid3);
				info("Add treatment for patient by provider: treatment id: "+ tid3 +" By provider NPIs: "+NPIs[2]+ "  for patientId: "+ patientIds[2]);
				
				TreatmentDto treat4 = new TreatmentDto(); 
				DrugTreatmentType dt4 = new DrugTreatmentType();
				dt4.setName("PainKiller04");
				dt4.setDosage((float)6.9);
				treat4.setDrugTreatment(dt4);
				treat4.setDiagnosis("Headache04");
				treat4.setNPI(NPIs[3]);
				long tid4 = providerService.createTreatment(patientIds[3], NPIs[3], treat4);
				treatmentIdsByProvider.add(tid4);
				info("Add treatment for patient by provider: treatment id: "+ tid4 +" By provider NPIs: "+NPIs[3]+ "  for patientId: "+ patientIds[3]);
				
			} catch (ProviderServiceExn e) {
				info("Add treatment for patient by provider failed!"+e.toString());
			} catch (edu.stevens.cs548.clinic.service.provider.ejb.IProviderService.ProviderNotFoundExn e) {
				info("Add treatment for patient by provider failed!"+e.toString());
			} catch (edu.stevens.cs548.clinic.service.provider.ejb.IProviderService.PatientNotFoundExn e) {
				info("Add treatment for patient by provider failed!"+e.toString());
			}
			
			info("Add treatment: done!");
			
			info("Third, obtaining a list of treatment for a patient");
			TreatmentDto[] treatmentDtos;
			try {
				treatmentDtos = patientService.getTreatments(patientPKeys.get(1), treatmentIdsByPatient[1]);
				//info("a list of treatment: " + treatmentDtos);		
				for (TreatmentDto t: treatmentDtos) {
					info ("Get treatment list by given the patient's key"+patientPKeys.get(1) +" by provider: "+t.getNPI()+ " Tids:"+ t.getTid());
				}
			} catch (PatientNotFoundExn e) {
				info("Get treatment list by given the patient's key Failed! Key= "+ patientPKeys.get(1) + e.toString());
			} catch (PatientServiceExn e) {
				info("Get treatment list by given the patient's key Failed! Key= "+ patientPKeys.get(1) + e.toString());
			}
			
			info ("Provider--Test2: Obtaining a list of provider DTOs, given a provider name");
			try {
				ProviderDTO[] provider;
				provider = providerService.getProviderByName(providerNames[0]);
				String providerFound = "";
				for (ProviderDTO pro : provider) {
					providerFound += " / " + pro.getNpi();
				}
				info ("Provider2: getProviderByName: "+ providerNames[0] + " NPIs: " + providerFound);
			} catch (ProviderServiceExn e) {
				info ("Provider2: getProviderByName: "+ providerNames[0] + " Provider not found: " + e.toString());
			}
			
			info ("Provider--Test3: Obtaining a single providerDTO, given a national provider identifier(NPI).");
			try {
				ProviderDTO provider = providerService.getProviderByNPI(NPIs[0]);
				info ("Provider3: getProviderByNPI: " + NPIs[0] + " Provider name: " + provider.getName());
			} catch (ProviderServiceExn e) {
				info ("Provider3: getProviderByNPI: " + NPIs[0] + " Provider not found: " + e.toString());
			}
		
			info ("Provider--Test6: Obtaining a list of tretment DTOs, for all tretments for a particular patient that are supervised by this provider ");
			try {
				TreatmentDto[] treatDto = providerService.getTreatmentsByPaPr(patientIds[0],NPIs[0]);
				for (TreatmentDto tr : treatDto) {
					info("Provider6: getTreatment for patient by provider: "+patientIds[0]+" "+NPIs[0]+" Treatment:"+tr.getDiagnosis());
				}
			} catch (edu.stevens.cs548.clinic.service.provider.ejb.IProviderService.ProviderNotFoundExn e) {
				info("Provider6: getTreatment for patient by provider: "+patientIds[0]+" "+NPIs[0]+ e.toString());
			} catch (edu.stevens.cs548.clinic.service.provider.ejb.IProviderService.TreatmentNotFoundExn e) {
				info("Provider6: getTreatment for patient by provider: "+patientIds[0]+" "+NPIs[0]+ e.toString());
			} catch (edu.stevens.cs548.clinic.service.provider.ejb.IProviderService.PatientNotFoundExn e) {
				info("Provider6: getTreatment for patient by provider: "+patientIds[0]+" "+NPIs[0]+ e.toString());
			}
			
			info ("Provider--Test7: Obtaining a list of tretment DTOs for the treatments supervised by this provider");
			TreatmentDto[] treat;
			try {
				treat = providerService.getTreatmentsByProvider(NPIs[0], treatmentIdsByPatient[0]);
				for(TreatmentDto t : treat) {
					info("Provider7: getTreatment by provider: "+NPIs[0] + " Treatment: "+ t.getDiagnosis());
				}
			} catch (edu.stevens.cs548.clinic.service.provider.ejb.IProviderService.ProviderNotFoundExn e) {
				info("Provider7: getTreatment by provider: "+NPIs[0] + " Treatment not found!: "+e.toString());
			} catch (ProviderServiceExn e) {
				info("Provider7: getTreatment by provider: "+NPIs[0] + " Treatment not found!: "+e.toString());
			}
			
			
//			info ("Patient--Test5: Deleting a patient record, given the patient name and patient key");
//			try {
//				String name = "Stone";
//				long id = 4;
//				patientService.deletePatient(name, id);
//				info("Deleting a patient record by the name and key: "+name+" "+id);
//			} catch (PatientServiceExn e) {
//				info("Deleting a patient record by the name and key Failed! "+ e.toString());
//			}
//			
//			info("Provider--Test5: delete treatment");
//			try {
//				providerService.deleteTreatment(patientIds[2], NPIs[2],treatmentIdsByPatient[2][0]);
//				info("Provider5: DeletevTreatment tid = " + treatmentIdsByPatient[2][0]);
//			} catch (ProviderServiceExn e) {
//				info("Provider5: Delete Treatment for patient by provider failed! tid = " + treatmentIdsByPatient[2][0] + e.toString());
//			} catch (edu.stevens.cs548.clinic.service.provider.ejb.IProviderService.ProviderNotFoundExn e) {
//				info("Provider5: Delete Treatment for patient by provider failed! tid = " + treatmentIdsByPatient[2][0] + e.toString());
//			} catch (edu.stevens.cs548.clinic.service.provider.ejb.IProviderService.PatientNotFoundExn e) {
//				info("Provider5: Delete Treatment for patient by provider failed! tid = " + treatmentIdsByPatient[2][0] + e.toString());
//			}
			
		
		/* Add treatment by patientService		
		//long[][] treatmentIdByPatient = new long[4][4];
		//List<Long> treatmentIdByProvider = new ArrayList<Long>();
		info("Second, add the treatment by patientService.");
		String[] dates = {"07/15/2014", "08/15/2014", "09/15/2014", "10/15/2014"};
		Date addDate = new Date();
		List<Date> treatmentDate = new ArrayList<Date>();
		long[] tids = new long[4]; 
		try {
			for(String date : dates) {
				addDate = datef.parse(date);
				treatmentDate.add(addDate);
			}
			tids[0] = patientService.addDrugTreatment(patientPKeys.get(0), NPIs[0], "Fever", "Aspirin", 1.5f);
			info("addDrugTreatment by patient:"+patientPKeys.get(0)+" by Provider:"+NPIs[0]+ " to Tids:"+tids[0]);
			
			tids[1] = patientService.addRadiology(patientPKeys.get(0), NPIs[1], "Cancer", treatmentDate.get(0));
			info("addRadiology by patient:"+patientPKeys.get(0)+" by Provider:"+NPIs[1]+ " to Tids:"+tids[1]);
			
			tids[2] = patientService.addRadiology(patientPKeys.get(1), NPIs[1], "Cancer", treatmentDate.get(1));
			info("addDrugTreatment by patient:"+patientPKeys.get(1)+" by Provider:"+NPIs[1]+ " to Tids:"+tids[2]);
			
			tids[3] = patientService.addSurgery(patientPKeys.get(2), NPIs[3], "Fracture", treatmentDate.get(2));
			info("addDrugTreatment by patient:"+patientPKeys.get(2)+" by Provider:"+NPIs[3]+ " to Tids:"+tids[3]);
			
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (PatientNotFoundExn e) {
			info ("Add treatment: patient not found!");
		} catch (ProviderNotFoundExn e) {
			info ("Add treatment: provider not found!");
		}
		info("Add treatment done!!!");

		info("Third, obtaining a list of treatment for a patient");
		TreatmentDto[] treatmentDtos;
		try {
			treatmentDtos = patientService.getTreatments(patientPKeys.get(1), tids);
			//info("a list of treatment: " + treatmentDtos);		
			for (TreatmentDto t: treatmentDtos) {
				info ("Get treatment list by given the patient's key"+patientPKeys.get(1) +" by provider: "+t.getNPI()+ " Tids:"+ t.getTid());
			}
		} catch (PatientNotFoundExn e) {
			info("Get treatment list by given the patient's key Failed! Key= "+ patientPKeys.get(1) + e.toString());
		} catch (PatientServiceExn e) {
			info("Get treatment list by given the patient's key Failed! Key= "+ patientPKeys.get(1) + e.toString());
		}
	*/	
	

	}

}

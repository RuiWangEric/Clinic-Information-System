package edu.stevens.cs548.clinic.service.web.rest.resources;

import java.net.URI;
import java.util.Date;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.DatatypeConverter;

import edu.stevens.cs548.clinic.service.dto.patient.PatientDTO;
import edu.stevens.cs548.clinic.service.patient.ejb.IPatientService.PatientServiceExn;
import edu.stevens.cs548.clinic.service.patient.ejb.IPatientServiceLocal;
import edu.stevens.cs548.clinic.service.web.rest.PatientRepresentation;

@RequestScoped
@Path("patient")
public class PatientResource {
    @SuppressWarnings("unused")
    @Context
    private UriInfo context;
    
    /**
     * Default constructor. 
     */
    public PatientResource() { }
    
    @EJB(beanName="PatientServiceBean")
    IPatientServiceLocal patientService;

	
	@GET
	@Path("site")
	@Produces("application/xml")
	public String getSiteInfo() {
		return patientService.siteInfo();
	
	}
	
    /*
     * Assignment7--patient: 1. Adding a patient to the clinic
     */
	@POST
	@Consumes("application/xml")
	public Response addPatient(PatientRepresentation patientRep) {
		try {
			long id = patientService.createPatient(patientRep.getPatientId(),
												   patientRep.getName(), 
												   patientRep.getDob(),
												   patientRep.getAge());
			UriBuilder ub = context.getAbsolutePathBuilder().path("{id}");
			URI url = ub.build(Long.toString(id));
			return Response.created(url).build();
		} catch (PatientServiceExn e) {
			throw new WebApplicationException(e.toString());
		}
	}

	
    /**
     * Query methods for patient resources.
     */
    /*
     * Assignment7--patient: 2. Obtaining a single patient representation, given a patient resource URI
     */
    @GET
    @Path("{id}")
    @Produces("application/xml")
    public PatientRepresentation getPatientById(@PathParam("id") String id) {
        try {
        	long key = Long.parseLong(id);
        	PatientDTO patientDTO = patientService.getPatientByDbId(key);
        	PatientRepresentation patientRep = new PatientRepresentation(patientDTO,context);
        	return patientRep;
        } catch (PatientServiceExn e) {
        	throw new WebApplicationException(e.toString());
        }
    }
    
    @GET
    @Path("patientId")
    @Produces("application/xml")
    public PatientRepresentation getPatientByPatientId (@QueryParam("id") String patientId) {
        try {
        	long pid = Long.parseLong(patientId);
        	PatientDTO patientDTO = patientService.getPatientByPatId(pid);
        	PatientRepresentation patientRep = new PatientRepresentation(patientDTO,context);
        	return patientRep;
        } catch (PatientServiceExn e) {
        	throw new WebApplicationException(e.toString());
        }
    }

    @GET
    @Produces("application/xml")
    public PatientRepresentation[] getPatientByNameDob (@QueryParam("name") String name,
    												    @QueryParam("dob") String dob) {
        try {
        	Date birthDate = DatatypeConverter.parseDate(dob).getTime();
        	PatientDTO[] patientDTO = patientService.getPatientsByNameDob(name, birthDate);
        	PatientRepresentation[] patientReps = new PatientRepresentation[patientDTO.length];
        	for (int i=0; i<patientDTO.length; i++) {
        		 patientReps[i] = new PatientRepresentation(patientDTO[i], context);
        	}
        	return patientReps;
        } catch (PatientServiceExn e) {
        	throw new WebApplicationException(e.toString());
        }
    }


}

package edu.stevens.cs548.clinic.service.web.rest.resources;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import edu.stevens.cs548.clinic.service.dto.treatment.TreatmentDto;
import edu.stevens.cs548.clinic.service.treatment.ejb.ITreatmentService.TreatmentServiceExn;
import edu.stevens.cs548.clinic.service.treatment.ejb.ITreatmentServiceLocal;
import edu.stevens.cs548.clinic.service.web.rest.TreatmentRepresentation;

@RequestScoped
@Path("treatment")
public class TreatmentResource {
	@SuppressWarnings("unused")
    @Context
    private UriInfo context;
	
	@EJB(beanName="TreatmentServiceBean")
    ITreatmentServiceLocal treatmentService;
	
	 /**
     * Default constructor. 
     */
    public TreatmentResource() {
    }
	
    /*
     * Assignment7--Treatment: 1. Obtaining a representation for a treatment, identified by its URI
     */
    /**
     * Retrieves representation of an instance of TreatmentResource
     * @return an instance of String
     */
	@GET
    @Path("{id}")
    @Produces("application/xml")
    public TreatmentRepresentation getTreatmentById(@PathParam("id") String id) {
    	try {
    		long key = Long.parseLong(id);
    		TreatmentDto treatmentDTO = treatmentService.getTreatmentById(key);    		
    		TreatmentRepresentation treatmentRep = new TreatmentRepresentation(treatmentDTO, context);
     	   	return treatmentRep;
    	} catch (TreatmentServiceExn e) {
    		throw new WebApplicationException(e.toString());
    	}
    }
}


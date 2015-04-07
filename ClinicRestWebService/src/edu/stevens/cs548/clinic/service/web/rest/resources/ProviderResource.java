package edu.stevens.cs548.clinic.service.web.rest.resources;

import java.net.URI;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
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

import edu.stevens.cs548.clinic.service.dto.provider.ProviderDTO;
import edu.stevens.cs548.clinic.service.dto.treatment.RadiologyType;
import edu.stevens.cs548.clinic.service.dto.treatment.SurgeryType;
import edu.stevens.cs548.clinic.service.dto.treatment.TreatmentDto;
import edu.stevens.cs548.clinic.service.dto.treatment.DrugTreatmentType;
import edu.stevens.cs548.clinic.service.provider.ejb.IProviderService.PatientNotFoundExn;
import edu.stevens.cs548.clinic.service.provider.ejb.IProviderService.ProviderNotFoundExn;
import edu.stevens.cs548.clinic.service.provider.ejb.IProviderService.ProviderServiceExn;
import edu.stevens.cs548.clinic.service.provider.ejb.IProviderService.TreatmentNotFoundExn;
import edu.stevens.cs548.clinic.service.provider.ejb.IProviderServiceLocal;
import edu.stevens.cs548.clinic.service.web.rest.ProviderRepresentation;
import edu.stevens.cs548.clinic.service.web.rest.TreatmentRepresentation;

@RequestScoped
@Path("provider")
public class ProviderResource {
    @SuppressWarnings("unused")
    @Context
    private UriInfo context;

    /**
     * Default constructor. 
     */
    public ProviderResource() {
        // TODO Auto-generated constructor stub
    }
    
    @EJB(beanName="ProviderServiceBean")
    IProviderServiceLocal providerService;
    
    @GET
    @Path("site")
    @Produces("application/xml")
    public String getStringInfo() {
    	return providerService.siteInfo();
    }

    /*
     * Assignment7--provider: 1. Adding a provider to the clinic
     */
    @POST
    @Consumes("application/xml")
    public Response addProvider(ProviderRepresentation providerRep) {
    	try {
    		long id = providerService.createProvider(providerRep.getNPI(), 
    												 providerRep.getName(), 
    												 providerRep.getProviderType());
    		UriBuilder ub = context.getAbsolutePathBuilder().path("{id}");
    		URI url = ub.build(Long.toString(id));
    		return Response.created(url).build();
    	} catch (ProviderServiceExn e) {
    		throw new WebApplicationException(e.toString());
    	}
    }
    
    /**
     * Retrieves representation of an instance of ProviderResource
     * @return an instance of String
     */
    /*
     * Assignment7--provider: 2. Obtaining a single provider representation, given a provider NPI
     */
    @GET
    @Path("byNPI")
    @Produces("application/xml")
    public ProviderRepresentation getProviderByNPI(@QueryParam("id") String NPI) {
        try {
        	long npi = Long.parseLong(NPI);
        	ProviderDTO providerDTO = providerService.getProviderByNPI(npi);
        	ProviderRepresentation providerRep = new ProviderRepresentation(providerDTO,context);
        	return providerRep;
        } catch (ProviderServiceExn e) {
        	throw new WebApplicationException(e.toString());
        }
    }
    
    
    /*
     * Assignment7--provider: 3. Obtaining a list of representation URIs for the treatments, for a patient supervised by this provider
     */
    @GET
    @Path("{id}/treatments")
    @Produces("application/xml")
    public TreatmentRepresentation[] getTreatmentByPP (@PathParam("id") String id, @HeaderParam("X-Patient") String xPatient) {
    	String[] temp = xPatient.split("/");
    	long pid = Long.parseLong(temp[temp.length-1]);
    	long npi = Long.parseLong(id);
    	try {
    		TreatmentDto[] treatmentDtos = providerService.getTreatmentsByPaPr(pid, npi);
    		TreatmentRepresentation[] treatmentReps = new TreatmentRepresentation[treatmentDtos.length];
    		for (int i=0; i<treatmentDtos.length; i++) {
    			treatmentReps[i] = new TreatmentRepresentation(treatmentDtos[i],context);
    		}
    		return treatmentReps;
    	} catch (PatientNotFoundExn e) {
    		throw new WebApplicationException(e.toString());
    	} catch (ProviderNotFoundExn e) {
    		throw new WebApplicationException(e.toString());
		} catch (TreatmentNotFoundExn e) {
    		throw new WebApplicationException(e.toString());
    	} 
    }
    
    /*
     * Assignment7--provider: 4. Obtaining a single provider representation, given a database id
    */
    @GET
    @Path("{id}")
    @Produces("application/xml")
    public ProviderRepresentation getProvider(@PathParam("id") String id) {
    	try {
    		long key = Long.parseLong(id);
    		ProviderDTO providerDTO = providerService.getProviderByDbId(key);
    		ProviderRepresentation providerRep = new ProviderRepresentation (providerDTO, context);
    		return providerRep;
    	} catch (ProviderServiceExn e) {
    		throw new WebApplicationException(e.toString());
    	}
    }
   
    
    /*
     * Assignment7--provider: 5. Add treatment for patient by provider
     */ 
    @POST
    @Path("{id}/treatments")
    @Produces("application/xml")
    public Response addTreatmentByPaPr(TreatmentRepresentation treatmentRep, 
    		@PathParam("id") String id, @HeaderParam("X-Patient") String xPatient) {
    	String[] temp = xPatient.split("/");
    	long pid = Long.parseLong(temp[temp.length-1]);
    	long npi = Long.parseLong(id);
    	try {
    		TreatmentDto treatmentDto = new TreatmentDto();
    		treatmentDto.setNPI(npi);
    		treatmentDto.setPatientId(pid);
    		treatmentDto.setDiagnosis(treatmentRep.getDiagnosis());
    		DrugTreatmentType drug;
    		RadiologyType radiology;
    		SurgeryType surgery;
    		if (treatmentRep.getDrugTreatment()!= null) {
    			drug = new DrugTreatmentType();
    			drug.setDosage(treatmentRep.getDrugTreatment().getDosage());
    			drug.setName(treatmentRep.getDrugTreatment().getName());
    			treatmentDto.setDrugTreatment(drug);
    		} else if (treatmentRep.getRadiology()!= null) {
    			radiology = new RadiologyType();
    			radiology.setDate(treatmentRep.getRadiology().getDate());
    			treatmentDto.setRadiology(radiology);
    		} else if (treatmentRep.getSurgery()!=null) {
    			surgery = new SurgeryType();
    			surgery.setDate(treatmentRep.getSurgery().getDate());
    			treatmentDto.setSurgery(surgery);
    		}
    		long tid = providerService.createTreatment(pid, npi, treatmentDto);
    		UriBuilder ub = context.getAbsolutePathBuilder().path("{id}");
    		URI url = ub.build(Long.toString(tid));
    		return Response.created(url).build();
    	} catch (ProviderServiceExn e) {
    		throw new WebApplicationException(e.toString());
    	} catch (ProviderNotFoundExn e) {
    		throw new WebApplicationException(e.toString());
		} catch (PatientNotFoundExn e) {
			throw new WebApplicationException(e.toString());
		}
    }


}
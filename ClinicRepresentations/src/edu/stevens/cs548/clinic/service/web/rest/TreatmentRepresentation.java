
package edu.stevens.cs548.clinic.service.web.rest;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.annotation.XmlRootElement;

import edu.stevens.cs548.clinic.service.dto.treatment.TreatmentDto;
import edu.stevens.cs548.clinic.service.web.rest.data.LinkType;
import edu.stevens.cs548.clinic.service.web.rest.data.ObjectFactory;
import edu.stevens.cs548.clinic.service.web.rest.data.TreatmentType;

@XmlRootElement
public class TreatmentRepresentation extends TreatmentType {
	
	private ObjectFactory representationFactory = new ObjectFactory();
	
	public LinkType getLinkPatient() {
		return this.getPatient();
	}
	
	public LinkType getLinkProvider() {
		return this.getProvider();
	}
	
	public static LinkType getTreatmentLink(long tid, UriInfo uriInfo) {
		UriBuilder ub = uriInfo.getBaseUriBuilder();
		ub.path("treatment");
		UriBuilder ubTreatment = ub.clone().path("{tid}");
		String treatmentURI = ubTreatment.build(Long.toString(tid)).toString();
		LinkType link  =new LinkType();
		link.setUrl(treatmentURI);
		link.setRelation(Representation.RELATION_PATIENT);
		link.setMediaType(Representation.MEDIA_TYPE);
		return link;
	}
	
	public TreatmentRepresentation () {}
	
	public TreatmentRepresentation (TreatmentDto dto, UriInfo uriInfo) {
		this.diagnosis = dto.getDiagnosis();
		this.tid = getTreatmentLink(dto.getTid(),uriInfo);
		this.patient = PatientRepresentation.getPatientLink(dto.getPatientId(), uriInfo);
		this.provider = ProviderRepresentation.getProviderLink(dto.getNPI(), uriInfo);
		if (dto.getDrugTreatment()!=null) {
			this.drugTreatment = representationFactory.createDrugTreatmentType();
			this.drugTreatment.setName(dto.getDrugTreatment().getName());
			this.drugTreatment.setDosage(dto.getDrugTreatment().getDosage());
		//	this.drugTreatment.setPhysician(dto.getDrugTreatment().getPhysician());
		} else if (dto.getSurgery()!=null) {
			this.surgery = representationFactory.createSurgeryType();
			this.surgery.setDate(dto.getSurgery().getDate());
		//	this.surgery.setSurgeon(dto.getSurgery().getSurgeon());
		} else if (dto.getRadiology()!=null) {
			this.radiology = representationFactory.createRadiologyType();
			this.radiology.setDate(dto.getRadiology().getDate());
		//	this.radiology.setRadiologist(dto.getRadiology().getRadiologist());
		}
	}

}


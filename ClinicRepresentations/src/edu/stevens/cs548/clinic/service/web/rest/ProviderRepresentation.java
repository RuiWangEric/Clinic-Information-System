package edu.stevens.cs548.clinic.service.web.rest;

import java.util.List;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.annotation.XmlRootElement;

import edu.stevens.cs548.clinic.service.dto.provider.ProviderDTO;
import edu.stevens.cs548.clinic.service.web.rest.data.LinkType;
import edu.stevens.cs548.clinic.service.web.rest.data.ProviderType;

@XmlRootElement
public class ProviderRepresentation extends ProviderType {

	public List<LinkType> getLinksTreatments() {
		return this.getTreatments();
	}

	public ProviderRepresentation() {
		super();
	}
	
	public static LinkType getProviderLink(long NPI, UriInfo uriInfo) {
		UriBuilder ub = uriInfo.getBaseUriBuilder();
		ub.path("provider");
		UriBuilder ubProvider = ub.clone().path("{id}");
		String providerURI = ubProvider.build(Long.toString(NPI)).toString();
		LinkType link  =new LinkType();
		link.setUrl(providerURI);
		link.setRelation(Representation.RELATION_PATIENT);
		link.setMediaType(Representation.MEDIA_TYPE);
		return link;
	}

	public ProviderRepresentation(ProviderDTO dto, UriInfo uriInfo) {
		this.id = dto.getId();
		this.npi = dto.getNpi();
		this.name = dto.getName();
		this.providerType = dto.getProviderType();
		long[] treatments = dto.getTreatments();
		UriBuilder ub = uriInfo.getBaseUriBuilder();
		ub.path("treatment");
		/*
		 * Call getTreatments to initialize empty list.
		 */
		List<LinkType> links = this.getTreatments();
		for (long t : treatments) {
			LinkType link = new LinkType();
			UriBuilder ubTreatment = ub.clone().path("{tid}");
			String treatmentURI = ubTreatment.build(Long.toString(t)).toString();
			link.setUrl(treatmentURI);
			link.setRelation(Representation.RELATION_TREATMENT);
			link.setMediaType(Representation.MEDIA_TYPE);
			links.add(link);
		}
	}
	

}

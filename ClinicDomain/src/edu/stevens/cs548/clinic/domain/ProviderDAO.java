package edu.stevens.cs548.clinic.domain;

import java.util.List;

import javax.persistence.*;

//import edu.stevens.cs548.clinic.domain.ITreatmentDAO.TreatmentExn;

public class ProviderDAO implements IProviderDAO {
	
	//@PersistenceContext
	private EntityManager em;
	private TreatmentDAO treatmentDAO;

	
/*----------------------------get provider-----------------------------------------------------------------------*/	
	@Override
	public Provider getProviderByDbId(long id) throws ProviderExn {
		Provider provider =em.find(Provider.class, id);
		if(provider ==null) {
			throw new ProviderExn ("Provider not found: primary key = " + id);
		} else {
			provider.setTreatmentDAO(this.treatmentDAO);
			return provider;
		}
	}

	/* 7.b Return a provider given a NPI.*/
	@Override
	public Provider getProviderByNPI(long NPI) throws ProviderExn {
		TypedQuery<Provider> query= 
				em.createNamedQuery("SearchProviderByNPI", Provider.class)
				.setParameter("NPI", NPI);
		List<Provider> providers = query.getResultList();
		//System.out.println(providers.size());
		if(providers.size() > 1)
			throw new ProviderExn ("Duplicate provider records: provider id= " + NPI);
		else if (providers.size() < 1) {
			throw new ProviderExn ("Provider not found: provider id = " + NPI);
		  //return null;
		}
		else{
			Provider provider = providers.get(0);
			provider.setTreatmentDAO(this.treatmentDAO);
			return provider;
		}
	}
	
	/* 7.a Return all providers with a given name.*/
	@Override 
	public List<Provider> getProviderByName(String name) throws ProviderExn {
		TypedQuery<Provider> query = em.createNamedQuery("SearchProviderByName", Provider.class)
				.setParameter("name", name);
		List<Provider> providers = query.getResultList();
		if(providers.size()<1){
			return null;
		}else{
			return providers;
		}
	}
	
	@Override
	public List<Provider> getProviderByNameProviderType(String name, String providerType)  {
		TypedQuery<Provider> query= 
				em.createNamedQuery("SearchProviderByNameProviderType", Provider.class)
				.setParameter("name", name)
				.setParameter("providerType", providerType);
		List<Provider> providers = query.getResultList();
		for(Provider provider : providers){
			provider.setTreatmentDAO(this.treatmentDAO);
		}
		return providers;
	}

	
/*----------------------------add and delete provider---------------------------------------------------------*/	

	/* 6. Add a provider to a clinic */
	@Override
	public long addProvider(Provider provider) throws ProviderExn{
		long NPI = provider.getNPI();
		TypedQuery<Provider> query = em.createNamedQuery("SearchProviderByNPI", Provider.class)
				.setParameter("NPI", NPI);
		List<Provider> providers = query.getResultList();
		if(providers.size()<1){
			provider.setTreatmentDAO(this.treatmentDAO);
			em.persist(provider);
			return provider.getId();
		}else{
			Provider provider2 = providers.get(0);
			throw new ProviderExn("Insertion: Provider with patient id (" + NPI
					+ ") already exist.\n** Name: " + provider2.getName());
		}
	}

	/*
	 * 8. Delete a provider entity from the system. 
	 * The treatment entities associated with the provider entity should not however be deleted.
	 */
	@Override
	public void deleteProvider(Provider provider) throws ProviderExn {
			em.remove(provider);
	}
	
	@Override
	public int deleteAll() throws ProviderExn{ 
		Query query = em.createQuery("delete from Provider provider");
		int numProvider = query.executeUpdate();
		return numProvider;
	}
	
	public ProviderDAO (EntityManager em) {
		this.em = em ;
		this.treatmentDAO = new TreatmentDAO(em);
	}

}

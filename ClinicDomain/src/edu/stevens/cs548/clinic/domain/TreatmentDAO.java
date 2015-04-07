package edu.stevens.cs548.clinic.domain;

import javax.persistence.EntityManager;
import javax.persistence.Query;

public class TreatmentDAO implements ITreatmentDAO {
	
	private EntityManager em;
	
	public TreatmentDAO (EntityManager em){
		this.em = em;
	}

	@Override
	public Treatment getTreatmentByDbId(long id) throws TreatmentExn {
		Treatment t = em.find(Treatment.class, id);
		if(t == null) {
			throw new TreatmentExn("Missing treatment: id = " + id);
		}else {
			return t;
		}
	}

	@Override
	public long addTreatment(Treatment t) {
		em.persist(t);
		return t.getId();
	}
	
	
	@Override
	public int deleteAll() throws TreatmentExn{ 
		Query query = em.createQuery("delete from Treatment treatment");
		int numProvider = query.executeUpdate();
		return numProvider;
	}
	
	
	@Override
	public void deleteTreatment (Treatment t) {
		em.remove(t);
	}
}

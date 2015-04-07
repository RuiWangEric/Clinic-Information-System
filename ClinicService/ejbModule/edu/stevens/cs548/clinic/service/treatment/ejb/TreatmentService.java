package edu.stevens.cs548.clinic.service.treatment.ejb;

import edu.stevens.cs548.clinic.domain.ITreatmentDAO;
import edu.stevens.cs548.clinic.domain.ITreatmentDAO.TreatmentExn;
import edu.stevens.cs548.clinic.domain.Treatment;
import edu.stevens.cs548.clinic.domain.TreatmentDAO;
import edu.stevens.cs548.clinic.service.dto.treatment.TreatmentDto;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Session Bean implementation class TreatmentService
 */
@Stateless(name="TreatmentServiceBean")
public class TreatmentService implements ITreatmentServiceLocal, ITreatmentServiceRemote {

	private ITreatmentDAO treatmentDAO;

    
    @PersistenceContext(unitName="ClinicDomain")
    private EntityManager em;
    
    @PostConstruct
    private void initialize() {
    	treatmentDAO = new TreatmentDAO(em);
    }

	/**
     * @see ITreatmentService#getTreatmentByTid(long)
     */
    @Override
    public TreatmentDto getTreatmentById(long tid) throws TreatmentServiceExn {
			try {
				Treatment t = treatmentDAO.getTreatmentByDbId(tid);
				return new TreatmentDto(t);
			} catch (TreatmentExn e) {
				throw new TreatmentServiceExn (e.toString());
			}
    }

}

package PetCryptoViewer.DAO;

import antlr.debug.InputBufferEventSupport;
import org.springframework.stereotype.Repository;
import PetCryptoViewer.Model.Pairs;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.time.Instant;
import java.util.List;

@Transactional
@Repository
public class DAOofPairs {

    @PersistenceContext
    private EntityManager em;

    public void savePair(Pairs pair) {
        em.persist(pair);
        em.flush();
    }

    public Pairs getLastOneValueInTime(Integer id1, Integer id2) {
        Query query = em.createQuery("select p from Pairs p where p.firstCurrency.id = :id1 and p.secondCurrency.id = :id2 order by p.time desc");
        query.setParameter("id1", id1);
        query.setParameter("id2", id2);
        query.setMaxResults(1);
        return (Pairs) query.getSingleResult();
    }

    public List<Pairs> getListOfValuesBetweenTheDates(Instant start, Instant end, Integer currency) {
        Query query = em.createQuery("select p from Pairs p where first_currency = :currency and time between :start and :end");
        query.setParameter("start", start);
        query.setParameter("end", end);
        query.setParameter("currency", currency);
        List<Pairs> res =query.getResultList();
        return res;
    }

}

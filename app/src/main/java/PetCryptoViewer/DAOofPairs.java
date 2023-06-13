package PetCryptoViewer;

import org.springframework.stereotype.Repository;
import PetCryptoViewer.Model.Pairs;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

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
        Query query = em.createQuery("select p from Pairs p where p.first_currency.id = :id1 and p.second_currency.id = :id2 order by p.time desc");
        query.setParameter("id1", id1);
        query.setParameter("id2", id2);
        query.setMaxResults(1);
        return (Pairs) query.getSingleResult();
    }
}

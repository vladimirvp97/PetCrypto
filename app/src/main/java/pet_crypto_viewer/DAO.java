package pet_crypto_viewer;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Transactional
@Repository
public class DAO {
    @PersistenceContext
    private EntityManager em;

    public void saveBTC(BTC_price cryptoEntity) {
        em.persist(cryptoEntity);
        em.flush();
    }
}

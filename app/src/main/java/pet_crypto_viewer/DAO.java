package pet_crypto_viewer;

import org.springframework.stereotype.Repository;
import pet_crypto_viewer.Crypto.Pairs;
import pet_crypto_viewer.Crypto.Currency;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Transactional
@Repository
public class DAO {

    @PersistenceContext
    private EntityManager em;


    public void saveBTC(Pairs cryptoEntity) {

        em.persist(cryptoEntity);
        em.flush();
    }


    public Currency getCurrencyById(int id) {
        em.flush();
        Currency currency = em.find(Currency.class, id);
        return currency;
    }
}

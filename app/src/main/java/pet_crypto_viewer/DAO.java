package pet_crypto_viewer;

import org.springframework.stereotype.Repository;
import pet_crypto_viewer.Crypto.Pairs;
import pet_crypto_viewer.Crypto.Currency;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.Arrays;

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

    public Integer getIdOfCurrencyByName(String currency) {
        Query query = em.createQuery("select id from Currency where name = :name");
        query.setParameter("name", currency);
        Integer obtainedValue = (Integer) query.getSingleResult();
        return obtainedValue;
    }

    public Pairs getLastOneValueInTime(Integer id1, Integer id2) {
        // как здесь работает принцип с Object и getSingleResult. Почему нельзя явно указать что это будет массив? Почему можно только через приведение типов.
        Query query = em.createQuery("select p from Pairs p where p.first_currency.id = :id1 and p.second_currency.id = :id2 order by p.time desc");
        query.setParameter("id1", id1);
        query.setParameter("id2", id2);
        query.setMaxResults(1);
        Pairs result = (Pairs) query.getSingleResult(); // под капотом преобразует Object к Object[] всегда
        /*String[] resultInString = Arrays.copyOf(result, result.length, String[].class);*/
        return result;
    }
}

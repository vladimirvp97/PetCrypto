package PetCryptoViewer.DAO;

import org.springframework.stereotype.Repository;
import PetCryptoViewer.Model.Currency;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

@Transactional
@Repository
public class DAOofCurrency {

    @PersistenceContext
    private EntityManager em;

    public void saveCurrency(Currency currency) {
        Integer check = getIdOfCurrencyByName(currency.getStr());
        if (check != null) return;
        em.persist(currency);
        em.flush();
    }
    // использовать интеграционный тест. test container
    public Currency getCurrencyById(int id) {
        Currency currency = em.find(Currency.class, id);
        return currency;
    }

    public Integer getIdOfCurrencyByName(String currency) {
        Query query = em.createQuery("select id from Currency where name = :name");
        query.setParameter("name", currency);
        Integer result;
        try {
            result = (Integer) query.getSingleResult(); // особенность API, что если результата нет то выбрасывается Exception
        } catch (Exception e) {
            return null;
        }
        return result;
    }
}

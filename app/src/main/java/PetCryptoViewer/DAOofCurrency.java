package PetCryptoViewer;

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
        em.persist(currency);
        em.flush();
    }

    public Currency getCurrencyById(int id) {
        Currency currency = em.find(Currency.class, id);
        return currency;
    }

    public Integer getIdOfCurrencyByName(String currency) {
        Query query = em.createQuery("select id from Currency where name = :name");
        query.setParameter("name", currency);
        return (Integer) query.getSingleResult();
    }
}

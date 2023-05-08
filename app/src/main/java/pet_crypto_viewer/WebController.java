package pet_crypto_viewer;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.security.auth.login.Configuration;
import java.util.List;

@Controller
public class WebController {
    @PersistenceContext
    private EntityManager em;
    @GetMapping("/price")
    @ResponseBody
    public String getPrice(){
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<BTC_price> criteriaQuery = criteriaBuilder.createQuery(BTC_price.class);
        Root<BTC_price> root = criteriaQuery.from(BTC_price.class);
        criteriaQuery.select(root);
        criteriaQuery.orderBy(criteriaBuilder.desc(root.get("id")));
        TypedQuery<BTC_price> typedQuery = em.createQuery(criteriaQuery);
        typedQuery.setMaxResults(1);
        StringBuilder sb = new StringBuilder();
        sb.append(typedQuery.getSingleResult().getTime()).append(" ").append(typedQuery.getSingleResult().getValue());
        return sb.toString();
    }
}

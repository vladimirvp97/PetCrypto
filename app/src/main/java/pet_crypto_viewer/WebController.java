package pet_crypto_viewer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import pet_crypto_viewer.Crypto.Pairs;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@Controller
public class WebController {
    @PersistenceContext
    private EntityManager em;

    @Autowired
    private DAO dao;
    @GetMapping("/price")
    // /price?currency1=BTC&currency2=RUB
    @ResponseBody
    public String getPrice(@RequestParam String currency1, @RequestParam String currency2){
        Integer id1 = dao.getIdOfCurrencyByName(currency1);
        Integer id2 = dao.getIdOfCurrencyByName(currency2);

        Pairs actualValues = dao.getLastOneValueInTime(id1, id2);
        // Сделать с помощью средств ORM. Создавать сразу pairs и возвращать его сюда и вынимать поля.
        String resultOfQueryByUser = "Value is " + actualValues.getValue() +  " Time when data recieved: " + actualValues.getTime();
        return resultOfQueryByUser;

        // ✅найти последний (во времени) курс currency1 к currency2
        // ✅1. отправить sql запрос к таблице currency чтобы получить ID currency1 и currency2
        // ✅2. отправить sql запрос к таблице Pairs first_currency = currency1 AND second_currency = currency2
        // ✅3. отдать результат пользователю
        // сделать правильное преобразование времени с добавлением UTC
        // !спарсить все возможные симбулы в table Currency
        // !нужно добавлять все возможные пары к нам в базу данных по определенному расписанию. Сейчас это 162 пары.
        // !сделать обновление и пополнение таблицы Currency по определенному временному интервалу или ? если пользователь
        // запросил пару которой у нас нет - обновить таблицу преждевременно и найти это значение.
        // ?Когда заполнить Currency в первый раз. Через event pointы в spring
        /*CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Pairs> criteriaQuery = criteriaBuilder.createQuery(Pairs.class);
        Root<Pairs> root = criteriaQuery.from(Pairs.class);
        criteriaQuery.select(root);
        criteriaQuery.orderBy(criteriaBuilder.desc(root.get("id")));
        TypedQuery<Pairs> typedQuery = em.createQuery(criteriaQuery);
        typedQuery.setMaxResults(1);
        StringBuilder sb = new StringBuilder();
        sb.append(typedQuery.getSingleResult().getTime()).append(" ").append(typedQuery.getSingleResult().getValue());
        return sb.toString();*/
    }
}

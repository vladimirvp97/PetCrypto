package pet_crypto_viewer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
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
    @Autowired
    private Service service;

    @Autowired
    private Requester requester;
    @GetMapping("/")
    public String showForm() {
        return "index";
    }
    @GetMapping ("/price")
    // /price?currency1=BTC&currency2=RUB
    @ResponseBody
    public String getPrice(@RequestParam String currency1, @RequestParam String currency2) throws Exception {

        Integer id1 = dao.getIdOfCurrencyByName(currency1);
        Integer id2 = dao.getIdOfCurrencyByName(currency2);

        if(id1==null || id2==null) {
            service.additionalCurrenciesParser(); // парсим еще раз все возможные валюты, допуская возможность их пополнения после первого парсинга при первой загрузке приложения
            id1 = dao.getIdOfCurrencyByName(currency1);
            id2 = dao.getIdOfCurrencyByName(currency2);
            if(id1==null || id2==null) { // если и сейчас не находим, то таких валют впринципе на данный момент времени нет
                return "No have data about this currencies, please try the another request";
            }
        }
        // добавляем свежее значение валютной пары в нашу БД
        requester.requestToAPI(currency1, currency2);
        // достаем это свежее значение из БД
        Pairs actualValues = dao.getLastOneValueInTime(id1, id2);

        String resultOfQueryByUser = "Value is " + actualValues.getValue() +  " Time when data recieved: " + actualValues.getConciseTime();
        return resultOfQueryByUser;

    }
}

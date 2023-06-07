package pet_crypto_viewer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import pet_crypto_viewer.Crypto.Pairs;
import pet_crypto_viewer.Crypto.Currency;

import java.io.IOException;
import java.time.Instant;
import java.time.format.DateTimeFormatter;


@Component
public class Service implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    private DAO dao;

    @Autowired
    private Requester requester;
    // распаковываем JSON, находим нужное нам поле, создаем валютную пару и сохраняем в БД
    public void extractAndPushPrice(String responseJSON, String nameOfCurr1, String nameOfCurr2) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(responseJSON);
        Double price24h = rootNode.get("last_trade_price").asDouble();

        Currency curr1 = dao.getCurrencyById(dao.getIdOfCurrencyByName(nameOfCurr1));
        Currency curr2 = dao.getCurrencyById(dao.getIdOfCurrencyByName(nameOfCurr2));
        Pairs currencyPair = new Pairs(price24h, Instant.now());

        currencyPair.setFirst_currency(curr1);
        currencyPair.setSecond_currency(curr2);

        dao.saveBTC(currencyPair);
    }
    // обновляет(если есть новые валюты) валюты в момент запуска приложения(после окончания формирования ApplicationContext)
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try {
            String json = requester.getAllSymbols();
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(json);
            for (int i = 0; i < rootNode.size(); i++){
                JsonNode node = rootNode.get(i);
                String pair = node.get("symbol").toString();
                int delimiterIndex = pair.indexOf("-");
                dao.saveCurrency(new Currency(pair.substring(1, delimiterIndex)));
                dao.saveCurrency(new Currency(pair.substring(delimiterIndex + 1, pair.length() - 1)));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    // еще одно обнавление таблицы валют в момент когда на запрос пользователя - валюта не была найдена
    public void additionalCurrenciesParser() {
        try {
            String json = requester.getAllSymbols();
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(json);
            for (int i = 0; i < rootNode.size(); i++){
                JsonNode node = rootNode.get(i);
                String pair = node.get("symbol").toString();
                int delimiterIndex = pair.indexOf("-");
                dao.saveCurrency(new Currency(pair.substring(1, delimiterIndex)));
                dao.saveCurrency(new Currency(pair.substring(delimiterIndex + 1, pair.length() - 1)));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}

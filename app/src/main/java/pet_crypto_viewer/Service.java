package pet_crypto_viewer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pet_crypto_viewer.Crypto.Pairs;
import pet_crypto_viewer.Crypto.Currency;

import java.io.IOException;
import java.time.Instant;
import java.time.format.DateTimeFormatter;


@Component
public class Service {
    @Autowired
    private DAO dao;

    @Autowired
    private DateTimeFormatter dtf;

    public void extractAndPushPriceBTC(String responseJSON) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(responseJSON);
        Integer price24h = rootNode.get("price_24h").asInt();

        String time = Instant.now().toString();
        time = time.replaceAll("T|\\.\\d+Z", " ").substring(0, time.lastIndexOf("."));

        Currency btc = dao.getCurrencyById(1);
        Currency rub = dao.getCurrencyById(2);
        Pairs btcEntity = new Pairs(price24h, time);

        btcEntity.setFirst_currency(btc);
        btcEntity.setSecond_currency(rub);

        dao.saveBTC(btcEntity);
    }

}

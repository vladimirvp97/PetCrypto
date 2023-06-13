package PetCryptoViewer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import PetCryptoViewer.Model.Currency;

import java.io.IOException;
//// Пополняет(если у внешнего API есть новые значения) таблицу Currency в момент запуска приложения(после окончания формирования ApplicationContext)
public class SpringEvents implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    BlockChainClient blockChainClient;
    @Autowired
    DAOofCurrency DAOofCurrency;
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        String json = null;
        try {
            json = blockChainClient.getAllSymbols();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = null;
        try {
            rootNode = mapper.readTree(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        for (int i = 0; i < rootNode.size(); i++){
            JsonNode node = rootNode.get(i);
            String pair = node.get("symbol").toString();
            int delimiterIndex = pair.indexOf("-"); // от API мы получаем валютную пару в виде BTC-USD, поэтому ее нужно разделить на две части
            DAOofCurrency.saveCurrency(new Currency(pair.substring(1, delimiterIndex)));
            DAOofCurrency.saveCurrency(new Currency(pair.substring(delimiterIndex + 1, pair.length() - 1)));
        }

    }
}

package PetCryptoViewer;

import PetCryptoViewer.Client.BlockChainClient;
import PetCryptoViewer.DAO.DAOofCurrency;
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
    DAOofCurrency daOofCurrency;
    @Autowired
    ObjectMapper objectMapper;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        String json = null;
        try {
            json = blockChainClient.getAllSymbols();
        } catch (IOException e) {
            throw new RuntimeException("Empty data from foreign API", e);
        }
        JsonNode rootNode = null;
        try {
            rootNode = objectMapper.readTree(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        try {
            for (int i = 0; i < rootNode.size(); i++){
                JsonNode node = rootNode.get(i);
                String pair = node.get("symbol").toString();
                int delimiterIndex = pair.indexOf("-"); // от API мы получаем валютную пару в виде BTC-USD, поэтому ее нужно разделить на две части
                daOofCurrency.saveCurrency(new Currency(pair.substring(1, delimiterIndex)));
                daOofCurrency.saveCurrency(new Currency(pair.substring(delimiterIndex + 1, pair.length() - 1)));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}

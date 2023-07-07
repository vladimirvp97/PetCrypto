package PetCryptoViewer;

import PetCryptoViewer.Client.BlockChainClient;
import PetCryptoViewer.DAO.DAOofCurrency;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import PetCryptoViewer.Model.Currency;
import org.springframework.stereotype.Component;

import java.io.IOException;
//// Пополняет(если у внешнего API есть новые значения) таблицу Currency в момент запуска приложения(после окончания формирования ApplicationContext)
@Component
@Profile("!test")
public class SpringEvents implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    BlockChainClient blockChainClient;
    @Autowired
    DAOofCurrency daOofCurrency;
    @Autowired
    ObjectMapper objectMapper;

    @Override
    @SneakyThrows
    public void onApplicationEvent(ContextRefreshedEvent event) {

        String json = blockChainClient.getAllSymbols();
        JsonNode rootNode = objectMapper.readTree(json);

        for (int i = 0; i < rootNode.size(); i++){
            JsonNode node = rootNode.get(i);
            String pair = node.get("symbol").toString();
            int delimiterIndex = pair.indexOf("-"); // от API мы получаем валютную пару в виде BTC-USD, поэтому ее нужно разделить на две части
            daOofCurrency.saveCurrency(new Currency(pair.substring(1, delimiterIndex)));
            daOofCurrency.saveCurrency(new Currency(pair.substring(delimiterIndex + 1, pair.length() - 1)));
        }
    }
}

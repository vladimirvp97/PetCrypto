package PetCryptoViewer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import PetCryptoViewer.Model.Pairs;
import PetCryptoViewer.Model.Currency;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;


@Component
public class Service  {
    @Autowired
    private DAOofPairs DAOofPairs;
    @Autowired
    private DAOofCurrency DAOofCurrency;
    @Autowired
    private BlockChainClient blockChainClient;

    public Pairs getValueByNameOfCurrencies(String curr1, String curr2) throws Exception {
        Integer id1 = DAOofCurrency.getIdOfCurrencyByName(curr1);
        Integer id2 = DAOofCurrency.getIdOfCurrencyByName(curr2);

        if(id1==null || id2==null) {
            additionalCurrenciesParser(); // парсим еще раз все возможные валюты, допуская возможность их пополнения после первого парсинга при первой загрузке приложения
            id1 = DAOofCurrency.getIdOfCurrencyByName(curr1);
            id2 = DAOofCurrency.getIdOfCurrencyByName(curr2);
            if(id1==null || id2==null) { // если и сейчас не находим, то таких валют впринципе на данный момент времени нет
                return null;
            }
        }

        Pairs lastTimeValue = DAOofPairs.getLastOneValueInTime(id1,id2);

        if (lastTimeValue == null) {
            String JSONofLastTimeValue = blockChainClient.requestToAPI(curr1, curr2);
            lastTimeValue = extractAndPushPairsPrice(JSONofLastTimeValue, curr1, curr2);
            return lastTimeValue;
        }

        Duration difference = Duration.between(lastTimeValue.getTime(), Instant.now());
        if (difference.toHours() >= 2) {
            String JSONofLastTimeValue = blockChainClient.requestToAPI(curr1, curr2);
            lastTimeValue = extractAndPushPairsPrice(JSONofLastTimeValue, curr1, curr2);
        }

        return lastTimeValue;
    }
    public Pairs extractAndPushPairsPrice(String responseJSON, String nameOfCurr1, String nameOfCurr2) throws JsonProcessingException {

        JsonNode rootNode = new ObjectMapper().readTree(responseJSON);
        Double price24h = rootNode.get("last_trade_price").asDouble();

        Pairs currencyPair = new Pairs(price24h, Instant.now());
        currencyPair.setFirst_currency(DAOofCurrency.getCurrencyById(DAOofCurrency.getIdOfCurrencyByName(nameOfCurr1)));
        currencyPair.setSecond_currency(DAOofCurrency.getCurrencyById(DAOofCurrency.getIdOfCurrencyByName(nameOfCurr2)));

        DAOofPairs.savePair(currencyPair);
        return currencyPair;
    }

    public void additionalCurrenciesParser() {
        try {
            String json = blockChainClient.getAllSymbols();
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(json);
            for (int i = 0; i < rootNode.size(); i++){
                JsonNode node = rootNode.get(i);
                String pair = node.get("symbol").toString();
                int delimiterIndex = pair.indexOf("-");
                DAOofCurrency.saveCurrency(new Currency(pair.substring(1, delimiterIndex)));
                DAOofCurrency.saveCurrency(new Currency(pair.substring(delimiterIndex + 1, pair.length() - 1)));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

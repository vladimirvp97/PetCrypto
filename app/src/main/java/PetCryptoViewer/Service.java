package PetCryptoViewer;

import PetCryptoViewer.Client.BlockChainClient;
import PetCryptoViewer.DAO.DAOofCurrency;
import PetCryptoViewer.DAO.DAOofPairs;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import PetCryptoViewer.Model.Pairs;
import PetCryptoViewer.Model.Currency;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@EnableScheduling
@Component
public class Service  {
    @Autowired
    private DAOofPairs daOofPairs;
    @Autowired
    private DAOofCurrency daOofCurrency;
    @Autowired
    private BlockChainClient blockChainClient;
    @Autowired
    private ObjectMapper objectMapper;

    public Pairs getValueByNameOfCurrencies(String curr1, String curr2) throws Exception {
        Integer id1 = daOofCurrency.getIdOfCurrencyByName(curr1);
        Integer id2 = daOofCurrency.getIdOfCurrencyByName(curr2);

        if(id1==null || id2==null) {
            additionalCurrenciesParser(); // парсим еще раз все возможные валюты, допуская возможность их пополнения после первого парсинга при первой загрузке приложения
            id1 = daOofCurrency.getIdOfCurrencyByName(curr1);
            id2 = daOofCurrency.getIdOfCurrencyByName(curr2);
            if(id1==null || id2==null) { // если и сейчас не находим, то таких валют впринципе на данный момент времени нет
                return null;
            }
        }

        Pairs lastTimeValue = daOofPairs.getLastOneValueInTime(id1,id2);

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

        JsonNode rootNode = objectMapper.readTree(responseJSON);
        Double price24h = rootNode.get("last_trade_price").asDouble();

        Pairs currencyPair = new Pairs(price24h, Instant.now());
        currencyPair.setFirstCurrency(daOofCurrency.getCurrencyById(daOofCurrency.getIdOfCurrencyByName(nameOfCurr1)));
        currencyPair.setSecondCurrency(daOofCurrency.getCurrencyById(daOofCurrency.getIdOfCurrencyByName(nameOfCurr2)));

        daOofPairs.savePair(currencyPair);
        return currencyPair;
    }


    @Scheduled(fixedRate = 10 * 60 * 1000)
    public void addingPairsBySchedule() throws Exception {
        blockChainClient.requestToAPI("BTC","USD");
        blockChainClient.requestToAPI("ADA","USD");
        blockChainClient.requestToAPI("ETH","USD");
    }

    public Double computingOfMA(String startDate, String endDate,Integer interval, String currency){
        interval = interval/10;
        LocalDateTime localDateTime1 = LocalDateTime.parse(startDate);
        LocalDateTime localDateTime2 = LocalDateTime.parse(endDate);
        Instant instant1 = localDateTime1.toInstant(ZoneOffset.UTC);
        Instant instant2 = localDateTime2.toInstant(ZoneOffset.UTC);
        List<Pairs> resultsList = daOofPairs.getListOfValuesBetweenTheDates(instant1,instant2, daOofCurrency.getIdOfCurrencyByName(currency));
        double ma = 0;
        int counter = 0;
        for (int i = 0; i < resultsList.size(); i+=interval){
            ma+=resultsList.get(i).getValue();
            ++counter;
            if (i + interval > resultsList.size()) break; // избегаем ошибки Array index out of bound
        }
        return (ma/counter);
    }

    public void additionalCurrenciesParser() {
        try {
            String json = blockChainClient.getAllSymbols();
            JsonNode rootNode = objectMapper.readTree(json);
            for (int i = 0; i < rootNode.size(); i++){
                JsonNode node = rootNode.get(i);
                String pair = node.get("symbol").toString();
                int delimiterIndex = pair.indexOf("-");
                daOofCurrency.saveCurrency(new Currency(pair.substring(1, delimiterIndex)));
                daOofCurrency.saveCurrency(new Currency(pair.substring(delimiterIndex + 1, pair.length() - 1)));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void dop(){

    }
}

package pet_crypto_viewer;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pet_crypto_viewer.Service;

import java.io.IOException;

@Component
public class Requester {
    @Autowired
    Service service;

    // конструируем запрос к внешнему API на основе запроса пользователя
    public void requestToAPI(String curr1, String curr2) throws Exception {
        StringBuilder sb = new StringBuilder("https://api.blockchain.com/v3/exchange/tickers/");
        sb.append(curr1).append("-").append(curr2);
        String externalServerUrl = String.valueOf(sb);

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(externalServerUrl);
        CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
        HttpEntity httpEntity = httpResponse.getEntity();
        String response = EntityUtils.toString(httpEntity);

        httpResponse.close();
        httpClient.close();

        service.extractAndPushPrice(response, curr1, curr2);
    }
    // получаем весь JSON где перечислены доступные валюты к поиску по API
    public String getAllSymbols() throws IOException {
        String externalServerUrl = "https://api.blockchain.com/v3/exchange/tickers";

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(externalServerUrl);
        CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
        HttpEntity httpEntity = httpResponse.getEntity();
        String response = EntityUtils.toString(httpEntity);

        httpResponse.close();
        httpClient.close();

        return response;
    }
}

package PetCryptoViewer.Client;

import PetCryptoViewer.Service;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class BlockChainClient {
    @Autowired
    Service service;
    @Autowired
    HTTPClient httpClient;


    public String requestToAPI(String curr1, String curr2) throws Exception {
        StringBuilder sb = new StringBuilder("https://api.blockchain.com/v3/exchange/tickers/");
        sb.append(curr1).append("-").append(curr2); // создаем уникальный запрос к API
        String externalServerUrl = String.valueOf(sb);

        String response = httpClient.getHttpRequest(externalServerUrl);

        service.extractAndPushPairsPrice(response, curr1, curr2);
        return response;
    }
    // Получаем весь JSON где перечислены доступные валютные пары к поиску по API
    public String getAllSymbols() throws IOException {
        String externalServerUrl = "https://api.blockchain.com/v3/exchange/tickers";

        return httpClient.getHttpRequest(externalServerUrl);
    }
}

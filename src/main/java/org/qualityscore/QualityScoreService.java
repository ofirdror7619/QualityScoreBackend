package org.qualityscore;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Cat;
import model.Dog;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class QualityScoreService {

    static Logger log = Logger.getLogger(QualityScoreController.class.getName());

    LocalDateTime lastUpdateTimeDogRequest = LocalDateTime.now();
    LocalDateTime lastUpdateTimeCatRequest = LocalDateTime.now();
    HttpResponse<String> cachedDogResponse;
    HttpResponse<String> cachedCatResponse;

    String dogUrl = Main.configurationList.get("DOG_URL");
    String catUrl = Main.configurationList.get("CAT_URL");
    String apiKey = Main.configurationList.get("API_KEY");

    public String getDogData() throws JsonProcessingException {
        if (getDataFromUrl(lastUpdateTimeDogRequest, cachedDogResponse)) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(dogUrl))
                    .header("x-api-key", apiKey)
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();
            HttpResponse<String> response = null;

            try {
                response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
            log.info("response from " + dogUrl);
            lastUpdateTimeDogRequest = LocalDateTime.now();
            cachedDogResponse = response;
            return convertHttpResponseToDogResponse(response.body());
        } else {
            log.info("response from cache");
            lastUpdateTimeDogRequest = LocalDateTime.now();
            return convertHttpResponseToDogResponse(cachedDogResponse.body());
        }
    }

    public String getCatData() throws JsonProcessingException {
        if (getDataFromUrl(lastUpdateTimeCatRequest, cachedCatResponse)) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(catUrl))
                    .header("x-api-key", apiKey)
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();
            HttpResponse<String> response = null;

            try {
                response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
            log.info("response from " + dogUrl);
            lastUpdateTimeCatRequest = LocalDateTime.now();
            cachedCatResponse = response;
            return convertHttpResponseToCatResponse(response.body());
        } else {
            log.info("response from cache");
            lastUpdateTimeCatRequest = LocalDateTime.now();
            return convertHttpResponseToCatResponse(cachedCatResponse.body());
        }
    }

    private boolean getDataFromUrl(LocalDateTime lastUpdateTime, HttpResponse<String> cachedResponse) {
        Integer cacheTTL = Integer.valueOf(Main.configurationList.get("cacheTTL"));

        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(lastUpdateTime, now);
        Integer durationInSeconds = Integer.valueOf((int) duration.toHours());
        if (durationInSeconds > cacheTTL || cachedResponse == null) {
            return true;
        }
        return false;
    }

    public static String convertHttpResponseToDogResponse(String responseBody) throws JsonProcessingException {

        StringBuilder strResult = new StringBuilder();
        strResult.append("[");
        ObjectMapper objectMapper = new ObjectMapper();

        List<Dog> dogs = objectMapper.readValue(responseBody, new TypeReference<List<Dog>>() {
        });

        for (Dog dog : dogs) {
            strResult.append("{");
            strResult.append("\"image\": \"" + dog.getImage_link() + "\", ");
            strResult.append("\"name\": \" " + dog.getName() + "\", ");
            strResult.append("\"min_life_span\": \"" + dog.getMin_life_expectancy() + "\", ");
            strResult.append("\"max_life_span\": \"" + dog.getMax_life_expectancy() + "\"");
            strResult.append("},");

        }
        strResult.delete(strResult.length() - 1, strResult.length());
        strResult.append("]");

        return strResult.toString();

    }

    public static String convertHttpResponseToCatResponse(String responseBody) throws JsonProcessingException {

        StringBuilder strResult = new StringBuilder();
        strResult.append("[");
        ObjectMapper objectMapper = new ObjectMapper();

        List<Cat> cats = objectMapper.readValue(responseBody, new TypeReference<List<Cat>>() {
        });

        for (Cat cat : cats) {
            strResult.append("{");
            strResult.append("\"image\": \"" + cat.getImage_link() + "\", ");
            strResult.append("\"name\": \" " + cat.getName() + "\", ");
            strResult.append("\"min_life_span\": \"" + cat.getMin_life_expectancy() + "\", ");
            strResult.append("\"max_life_span\": \"" + cat.getMax_life_expectancy() + "\"");
            strResult.append("},");

        }
        strResult.delete(strResult.length() - 1, strResult.length());
        strResult.append("]");

        return strResult.toString();

    }
}

package org.qualityscore;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

import org.apache.log4j.Logger;

@RestController
public class QualityScoreController {

    static Logger log = Logger.getLogger(QualityScoreController.class.getName());

    LocalDateTime lastUpdateTime = LocalDateTime.now();
    HttpResponse<String> cachedResponse;

    @GetMapping("/")
    public String getData() {
        if (getDataFromUrl(cachedResponse)) {
            String url = Main.configurationList.get("URL");
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();
            HttpResponse<String> response = null;

            try {
                response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
            log.info("response from " + url);
            lastUpdateTime = LocalDateTime.now();
            cachedResponse = response;
            return response.body();
        } else {
            log.info("response from cache");
            lastUpdateTime = LocalDateTime.now();
            return cachedResponse.body();
        }
    }

    private boolean getDataFromUrl(HttpResponse<String> cachedResponse) {
        Integer cacheTTL = Integer.valueOf(Main.configurationList.get("cacheTTL"));

        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(lastUpdateTime, now);
        Integer durationInSeconds = Integer.valueOf((int) duration.toSeconds());
        if (durationInSeconds > cacheTTL || cachedResponse == null) {
            return true;
        }
        return false;
    }
}

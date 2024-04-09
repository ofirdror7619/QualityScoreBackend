package org.qualityscore;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QualityScoreController {

    static Logger log = Logger.getLogger(QualityScoreController.class.getName());

    QualityScoreService qualityScoreService = new QualityScoreService();

    @GetMapping("/dogs")
    public String getDogData() throws JsonProcessingException {
        log.info("Controller: getDogData called...");
        return qualityScoreService.getDogData();
    }

    @GetMapping("/cats")
    public String getCatData() throws JsonProcessingException {
        log.info("Controller: getCatData called...");
        return qualityScoreService.getCatData();
    }

}

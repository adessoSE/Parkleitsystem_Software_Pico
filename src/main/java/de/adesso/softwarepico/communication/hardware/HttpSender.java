package de.adesso.softwarepico.communication.hardware;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class HttpSender implements HardwareSender{
    private final RestTemplate restTemplate;
    private final HttpHeaders httpHeaders;
    private final Logger logger = LoggerFactory.getLogger(HttpSender.class);

    public HttpSender() {
        this.restTemplate = new RestTemplate();
        this.httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
    }

    @Override
    public void send(String uri, JSONObject j) {
        HttpEntity<String> requestEntity = new HttpEntity<>(j.toString(), httpHeaders);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(buildUrl(uri), requestEntity, String.class);
        logger.info("[Send Http-Message with response code: " + responseEntity.getStatusCode() + "] : [Request=" + j.get("messageType") + "]");
    }

    private String buildUrl(String uri){
        return "http://" + uri + ":80/pico";
    }
}

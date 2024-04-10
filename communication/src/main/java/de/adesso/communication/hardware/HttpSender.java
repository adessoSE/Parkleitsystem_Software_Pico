package de.adesso.communication.hardware;

import de.adesso.communication.hardware.errorHandling.DidNotRespondException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
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
        try {
            restTemplate.postForEntity(buildUrl(uri), requestEntity, String.class);
        }
        catch (HttpServerErrorException e){
            if(j.getString("messageType").equals("heartbeat") && j.getInt("important") == 1){
                throw new DidNotRespondException();
            }
        }
        catch (Exception ignore){}
    }

    private String buildUrl(String uri){
        return "http://" + uri + ":80/pico";
    }

}

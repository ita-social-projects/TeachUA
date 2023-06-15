package com.softserve.teachua.service.impl.viberbot;

import com.softserve.teachua.service.viberbot.MessageDeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MessageDeliveryServiceImpl implements MessageDeliveryService {

    @Value("${viber.bot.header}")
    private String tokenHeaderName;
    @Value("${viber.bot.message.url}")
    private String url;
    private final RestTemplate restTemplate;

    @Autowired
    public MessageDeliveryServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void send(String message) {
        HttpEntity<String> entity = new HttpEntity<>(message, getHeaders());
        restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set(tokenHeaderName, System.getenv("viber.token"));
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

}

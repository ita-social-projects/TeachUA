package com.softserve.teachua.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.teachua.utils.viberbot.enums.CallbackType;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ViberBotListener implements ApplicationListener<ApplicationReadyEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ViberBotListener.class);
    @Value("${viber.bot.webhook.url}")
    private String webhookUrl;
    @Value("${server.url}")
    private String serverUrl;
    @Value("${viber.bot.header}")
    private String tokenHeaderName;
    @Value("${viber.token}")
    private String viberToken;

    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    public ViberBotListener(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.objectMapper = new ObjectMapper();
    }

    @SneakyThrows
    @Override
    public void onApplicationEvent(@NonNull ApplicationReadyEvent event) {
        String body = objectMapper.writeValueAsString(createWebHookParams());
        HttpEntity<String> entity = new HttpEntity<>(body, getHeaders());
        ResponseEntity<String> response = restTemplate.exchange(webhookUrl, HttpMethod.POST, entity, String.class);
        LOGGER.info("Connection to Viber status {}, body {}", response.getStatusCode(), response.getBody());
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set(tokenHeaderName, System.getenv("viber.token"));
        return headers;
    }

    private Map<String, Object> createWebHookParams() {
        LOGGER.info(serverUrl);
        Map<String, Object> param = new HashMap<>();
        param.put("url", serverUrl);
        param.put("send_name", true);
        param.put("event_types", getEvents());
        return param;
    }

    private List<String> getEvents() {
        return Arrays.stream(CallbackType.values()).filter(value -> value != CallbackType.NONE)
                .map(CallbackType::toString).collect(Collectors.toList());
   }

}

package com.softserve.teachua.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import java.util.HashMap;
import java.util.Map;

@Component
public class LiqPayParent {
    @Value("${publicKey}")
    private String publicKey;
    @Value("${privateKey}")
    private String privateKey;

    public String getPublicKey() {
        return publicKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void liqPayParam(Model model) {
        Map<String, String> map = new HashMap<>();
        map.put("action", "paydonate");
        map.put("amount", "1");
        map.put("currency", "UAH");
        map.put("description", "Help project");
        map.put("language", "uk");
        LiqPayChild liqPayChild = new LiqPayChild(getPublicKey(), getPrivateKey());
        Map<String, String> liqPayParam = liqPayChild.generateData(map);
        model.addAttribute("liqPayParam", liqPayParam);
    }
}

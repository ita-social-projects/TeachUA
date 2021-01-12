package com.softserve.teachua.config;

import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import java.util.HashMap;
import java.util.Map;

@Component
public class LiqPayParent {
    public void liqPayParam(Model model) {
        Map<String, String> map = new HashMap<>();
        map.put("action", "paydonate");
        map.put("amount", "1");
        map.put("currency", "UAH");
        map.put("description", "Help project");
        map.put("language", "uk");
        LiqPayChild liqPayChild = new LiqPayChild("sandbox_i96549488438", "sandbox_p6nzimBma81AJ0ynQBx7LAjcZSXoTe5zqHjxyfJ2");
        Map<String, String> liqPayParam = liqPayChild.generateData(map);
        model.addAttribute("liqPayParam", liqPayParam);
    }
}

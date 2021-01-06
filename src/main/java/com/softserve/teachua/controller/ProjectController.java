package com.softserve.teachua.controller;

import com.liqpay.LiqPay;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.Proxy;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/")
public class ProjectController {
    @GetMapping("/project-page")
    public String projectPage(Model model) {
        Map<String, String> map = new HashMap<>();
        map.put("action", "paydonate");
        map.put("amount", "1");
        map.put("currency", "UAH");
        map.put("description", "Help project");
        map.put("language", "uk");
        LiqPayChild liqPayChild = new LiqPayChild("sandbox_i96549488438", "sandbox_p6nzimBma81AJ0ynQBx7LAjcZSXoTe5zqHjxyfJ2");
        Map<String, String> liqPayParam = liqPayChild.generateData(map);
        model.addAttribute("liqPayParam", liqPayParam);
        return "projectpage";
    }

    private class LiqPayChild extends LiqPay {
        public LiqPayChild(String publicKey, String privateKey) {
            super(publicKey, privateKey);
        }

        public LiqPayChild(String publicKey, String privateKey, Proxy proxy, String proxyLogin, String proxyPassword) {
            super(publicKey, privateKey, proxy, proxyLogin, proxyPassword);
        }

        @Override
        protected Map<String, String> generateData(Map<String, String> params) {
            return super.generateData(params);
        }
    }
}

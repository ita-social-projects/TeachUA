package com.softserve.teachua.config;

import com.liqpay.LiqPay;

import java.net.Proxy;
import java.util.Map;


public class LiqPayChild extends LiqPay {
    public LiqPayChild(String publicKey, String privateKey) {
        super(publicKey, privateKey);
    }

    public LiqPayChild(String publicKey, String privateKey, Proxy proxy, String proxyLogin, String proxyPassword) {
        super(publicKey, privateKey, proxy, proxyLogin, proxyPassword);
    }

    @Override
    public Map<String, String> generateData(Map<String, String> params) {
        return super.generateData(params);
    }
}

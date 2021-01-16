package com.softserve.teachua.liqpay;

import com.liqpay.LiqPay;

import java.net.Proxy;
import java.util.Map;


public class LiqPayDataGenerator extends LiqPay {
    public LiqPayDataGenerator(String publicKey, String privateKey) {
        super(publicKey, privateKey);
    }

    public LiqPayDataGenerator(String publicKey, String privateKey, Proxy proxy, String proxyLogin, String proxyPassword) {
        super(publicKey, privateKey, proxy, proxyLogin, proxyPassword);
    }

    @Override
    public Map<String, String> generateData(Map<String, String> params) {
        return super.generateData(params);
    }
}

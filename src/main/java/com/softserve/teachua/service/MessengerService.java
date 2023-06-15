package com.softserve.teachua.service;

public interface MessengerService {
    boolean messengerWithAccessKeyExist(String key);
    void createMessenger(String name, String accessKey);
}

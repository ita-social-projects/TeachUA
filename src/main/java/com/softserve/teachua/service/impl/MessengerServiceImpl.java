package com.softserve.teachua.service.impl;

import com.softserve.teachua.model.Messenger;
import com.softserve.teachua.repository.MessengerRepository;
import com.softserve.teachua.service.MessengerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessengerServiceImpl implements MessengerService {
    private final MessengerRepository messengerRepository;

    @Autowired
    public MessengerServiceImpl(MessengerRepository messengerRepository) {
        this.messengerRepository = messengerRepository;
    }

    @Override
    public boolean messengerWithAccessKeyExist(String key) {
        return messengerRepository.existsByAccessKey(key);
    }

    @Override
    public void createMessenger(String name, String accessKey) {
        messengerRepository.save(Messenger.builder()
            .accessKey(accessKey)
            .userName(name)
            .build());
    }
}

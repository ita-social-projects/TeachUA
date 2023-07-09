package com.softserve.club.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.amqp.message_producer.impl.ArchiveMQMessageProducer;
import com.softserve.club.dto.message.MessageProfile;
import com.softserve.club.dto.message.MessageResponseDto;
import com.softserve.club.dto.message.MessageUpdateIsActive;
import com.softserve.club.dto.message.MessageUpdateText;
import com.softserve.club.model.Message;
import com.softserve.club.repository.ClubRepository;
import com.softserve.club.repository.MessageRepository;
import com.softserve.club.service.MessageService;
import com.softserve.commons.client.ArchiveClient;
import com.softserve.commons.client.UserClient;
import com.softserve.commons.exception.DatabaseRepositoryException;
import com.softserve.commons.exception.NotExistException;
import com.softserve.commons.util.converter.DtoConverter;
import jakarta.validation.ValidationException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;
    private final ClubRepository clubRepository;
    private final DtoConverter dtoConverter;
    private final UserClient userClient;
    private final ArchiveMQMessageProducer<Message> archiveMQMessageProducer;
    private final ArchiveClient archiveClient;
    private final ObjectMapper objectMapper;

    public MessageServiceImpl(MessageRepository messageRepository, ClubRepository clubRepository,
                              DtoConverter dtoConverter, UserClient userClient,
                              ArchiveMQMessageProducer<Message> archiveMQMessageProducer, ArchiveClient archiveClient,
                              ObjectMapper objectMapper) {
        this.messageRepository = messageRepository;
        this.clubRepository = clubRepository;
        this.dtoConverter = dtoConverter;
        this.userClient = userClient;
        this.archiveMQMessageProducer = archiveMQMessageProducer;
        this.archiveClient = archiveClient;
        this.objectMapper = objectMapper;
    }

    @Override
    public MessageResponseDto addMessage(MessageProfile messageProfile) {
        if (!clubRepository.existsById(messageProfile.getClubId())) {
            log.warn("Message not added because club with id - {} doesn't exists", messageProfile.getClubId());
            throw new NotExistException(String.format("Club with id - %s doesn't exists", messageProfile.getClubId()));
        }
        if (!userClient.existsById(messageProfile.getSenderId())) {
            log.warn("Message not added because user with id - {} doesn't exists", messageProfile.getSenderId());
            throw new NotExistException(
                    String.format("User with id - %s doesn't exists", messageProfile.getSenderId()));
        }
        if (!userClient.existsById(messageProfile.getRecipientId())) {
            log.warn("Message not added because user with id - {} doesn't exists", messageProfile.getRecipientId());
            throw new NotExistException(
                    String.format("User with id - %s doesn't exists", messageProfile.getRecipientId()));
        }
        messageProfile.setIsActive(true);
        Message message = messageRepository.save(dtoConverter.convertToEntity(messageProfile, new Message()));

        log.debug("new message added - " + message);
        return dtoConverter.convertToDto(message, MessageResponseDto.class);
    }

    @Override
    public Message getMessageById(Long id) {
        Message message = messageRepository.findById(id).orElseThrow(() -> {
            log.warn("Message with id - {} doesn't exist", id);
            return new NotExistException(String.format("Message with id - %s doesn't exist", id));
        });
        log.debug("get message by id - {}", id);
        return message;
    }

    @Override
    public List<Message> getMessagesByUserId(Long id, boolean isSender) {
        List<Message> messages;
        if (isSender) {
            messages = messageRepository.findAllBySenderIdOrderByDateDesc(id).orElseThrow(() -> {
                log.warn("Messages with sender id - {} doesn't exist", id);
                return new NotExistException(String.format("Messages with sender id - %s doesn't exist", id));
            });
            log.debug("get messages by sender id - {}", id);
        } else {
            messages = messageRepository.findAllByRecipientIdOrderByDateDesc(id).orElseThrow(() -> {
                log.warn("Messages with recipient id - {} doesn't exist", id);
                return new NotExistException(String.format("Messages with recipient id - %s doesn't exist", id));
            });
            log.debug("get messages by recipient id - {}", id);
        }
        return messages;
    }

    @Override
    public MessageResponseDto getMessageResponseById(Long id) {
        return dtoConverter.convertToDto(getMessageById(id), MessageResponseDto.class);
    }

    @Override
    public List<MessageResponseDto> getMessageResponsesByUserId(Long id, boolean isSender) {
        return getMessagesByUserId(id, isSender).stream()
                .map(message -> (MessageResponseDto) dtoConverter.convertToDto(message, MessageResponseDto.class))
                .toList();
    }

    @Override
    public MessageResponseDto updateMessageTextById(Long id, MessageUpdateText messageUpdateText) {
        Message updatedMessage = getMessageById(id).withText(messageUpdateText.getText());
        MessageResponseDto messageResponseDto = dtoConverter.convertToDto(messageRepository.save(updatedMessage),
                MessageResponseDto.class);
        log.debug("update message text by id - {}", id);
        return messageResponseDto;
    }

    @Override
    public MessageResponseDto updateMessageIsActiveById(Long id, MessageUpdateIsActive messageUpdateIsActive) {
        Message updatedMessage = getMessageById(id).withIsActive(messageUpdateIsActive.getIsActive());
        MessageResponseDto messageResponseDto = dtoConverter.convertToDto(messageRepository.save(updatedMessage),
                MessageResponseDto.class);
        log.debug("update message isActive by id - {}", id);
        return messageResponseDto;
    }

    @Override
    public MessageResponseDto deleteMessageById(Long id) {
        Message message = getMessageById(id);

        try {
            messageRepository.deleteById(id);
        } catch (DataAccessException | ValidationException e) {
            log.warn("Message with id - {} not deleted cause of relationship", id);
            throw new DatabaseRepositoryException(
                    String.format("Can't delete message with id - %s, cause of relationship", id));
        }

        archiveModel(message);
        MessageResponseDto messageResponseDto = dtoConverter.convertToDto(message, MessageResponseDto.class);
        log.debug(String.format("delete message by id - %s", id));
        return messageResponseDto;
    }

    private void archiveModel(Message message) {
        archiveMQMessageProducer.publish(message);
    }

    @Override
    public void restoreModel(Long id) {
        var message = objectMapper.convertValue(
                archiveClient.restoreModel(Message.class.getName(), id),
                Message.class);
        messageRepository.save(message);
    }
}

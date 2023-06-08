package com.softserve.teachua.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.message.MessageProfile;
import com.softserve.teachua.dto.message.MessageResponseDto;
import com.softserve.teachua.dto.message.MessageUpdateIsActive;
import com.softserve.teachua.dto.message.MessageUpdateText;
import com.softserve.teachua.exception.DatabaseRepositoryException;
import com.softserve.commons.exception.NotExistException;
import com.softserve.teachua.model.Message;
import com.softserve.teachua.model.archivable.MessageArch;
import com.softserve.teachua.repository.ClubRepository;
import com.softserve.teachua.repository.MessageRepository;
import com.softserve.teachua.repository.UserRepository;
import com.softserve.teachua.service.ArchiveMark;
import com.softserve.teachua.service.ArchiveService;
import com.softserve.teachua.service.ClubService;
import com.softserve.teachua.service.MessageService;
import com.softserve.teachua.service.UserService;
import java.util.List;
import java.util.Optional;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService, ArchiveMark<Message> {
    private final MessageRepository messageRepository;
    private final ClubRepository clubRepository;
    private final UserRepository userRepository;
    private final DtoConverter dtoConverter;
    private final ArchiveService archiveService;
    private final ObjectMapper objectMapper;
    private final ClubService clubService;
    private final UserService userService;

    @Override
    public MessageResponseDto addMessage(MessageProfile messageProfile) {
        if (!clubRepository.existsById(messageProfile.getClubId())) {
            log.warn("Message not added because club with id - {} doesn't exists", messageProfile.getClubId());
            throw new NotExistException(String.format("Club with id - %s doesn't exists", messageProfile.getClubId()));
        }
        if (!userRepository.existsById(messageProfile.getSenderId())) {
            log.warn("Message not added because user with id - {} doesn't exists", messageProfile.getSenderId());
            throw new NotExistException(
                    String.format("User with id - %s doesn't exists", messageProfile.getSenderId()));
        }
        if (!userRepository.existsById(messageProfile.getRecipientId())) {
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

    @Override
    public void archiveModel(Message message) {
        archiveService.saveModel(dtoConverter.convertToDto(message, MessageArch.class));
    }

    @Override
    public void restoreModel(String archiveObject) throws JsonProcessingException {
        MessageArch messageArch = objectMapper.readValue(archiveObject, MessageArch.class);
        Message message = dtoConverter.convertToEntity(messageArch, new Message()).withId(null);

        if (Optional.ofNullable(messageArch.getClubId()).isPresent()) {
            message.setClub(clubService.getClubById(messageArch.getClubId()));
        }
        if (Optional.ofNullable(messageArch.getSenderId()).isPresent()) {
            message.setSender(userService.getUserById(messageArch.getSenderId()));
        }
        if (Optional.ofNullable(messageArch.getRecipientId()).isPresent()) {
            message.setRecipient(userService.getUserById(messageArch.getRecipientId()));
        }
        messageRepository.save(message);
    }
}

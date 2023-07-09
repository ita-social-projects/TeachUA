package com.softserve.teachua.service;

import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.club.MessagesClub;
import com.softserve.teachua.dto.message.MessageProfile;
import com.softserve.teachua.dto.message.MessageResponseDto;
import com.softserve.teachua.dto.message.MessageUpdateIsActive;
import com.softserve.teachua.dto.message.MessageUpdateText;
import com.softserve.teachua.dto.user.UserPreview;
import com.softserve.teachua.exception.DatabaseRepositoryException;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.Club;
import com.softserve.teachua.model.Message;
import com.softserve.teachua.model.User;
import com.softserve.teachua.model.archivable.MessageArch;
import com.softserve.teachua.repository.ClubRepository;
import com.softserve.teachua.repository.MessageRepository;
import com.softserve.teachua.repository.UserRepository;
import com.softserve.teachua.service.impl.MessageServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MessageServiceTest {
    @Mock
    private MessageRepository messageRepository;

    @Mock
    private ClubRepository clubRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private DtoConverter dtoConverter;

    @Mock
    private ArchiveService archiveService;

    @InjectMocks
    private MessageServiceImpl messageService;

    private Message message;
    private MessageProfile messageProfile;
    private MessageUpdateText updateTextDto;
    private MessageUpdateIsActive updateIsActiveDto;
    private MessageResponseDto messageResponseDto;
    private MessageArch messageArch;
    private Archive archiveMessage;

    private final Long EXISTING_ID = 1L;
    private final Long NOT_EXISTING_ID = 5L;
    private final Long SENDER_ID = 1L;
    private final Long RECIPIENT_ID = 2L;
    private final String TEXT = "Test text";
    private final String UPDATED_TEXT = "Updated test text";
    private final String CLUB_NAME = "Some club name";
    private final Boolean ACTIVE = true;
    private final LocalDateTime TIME_NOW = LocalDateTime.now();
    private final Message MESSAGE_ENTITY = new Message();
    private final Class<MessageResponseDto> MESSAGE_RESPONSE_DTO_CLASS = MessageResponseDto.class;
    private final String ARCHIVE_CLASS_NAME = "com.softserve.club.service.impl.MessageServiceImpl";
    private final String ARCHIVE_DATA = "MessageArch instance converted into String";

    @BeforeEach
    void setUp() {
        Club club = Club.builder().id(EXISTING_ID).name(CLUB_NAME).build();
        User sender = User.builder().id(SENDER_ID).build();
        User recipient = User.builder().id(RECIPIENT_ID).build();
        UserPreview senderPrev = UserPreview.builder().id(SENDER_ID).build();
        UserPreview recipientPrev = UserPreview.builder().id(RECIPIENT_ID).build();
        MessagesClub messagesClub = MessagesClub.builder().id(EXISTING_ID).name(CLUB_NAME).build();
        message = Message.builder().id(EXISTING_ID).club(club).date(TIME_NOW).sender(sender).recipient(recipient)
                .text(TEXT).isActive(ACTIVE).build();
        messageProfile = MessageProfile.builder().id(EXISTING_ID).clubId(EXISTING_ID).senderId(SENDER_ID)
                .recipientId(RECIPIENT_ID).text(TEXT).isActive(null).build();
        messageResponseDto = MessageResponseDto.builder().id(EXISTING_ID).club(messagesClub).date(TIME_NOW)
                .sender(senderPrev).recipient(recipientPrev).text(TEXT).isActive(ACTIVE).build();
        updateTextDto = MessageUpdateText.builder().text(UPDATED_TEXT).build();
        updateIsActiveDto = MessageUpdateIsActive.builder().isActive(false).build();
        messageArch = MessageArch.builder().id(EXISTING_ID).clubId(EXISTING_ID).date(TIME_NOW).senderId(SENDER_ID)
                .recipientId(RECIPIENT_ID).text(TEXT).isActive(ACTIVE).build();
        archiveMessage = Archive.builder().id(EXISTING_ID).className(ARCHIVE_CLASS_NAME).data(ARCHIVE_DATA).build();
    }

    @Test
    void addMessage() {
        when(clubRepository.existsById(EXISTING_ID)).thenReturn(true);
        when(userRepository.existsById(SENDER_ID)).thenReturn(true);
        when(userRepository.existsById(RECIPIENT_ID)).thenReturn(true);
        when(dtoConverter.convertToEntity(messageProfile, MESSAGE_ENTITY)).thenReturn(message);
        when(messageRepository.save(any())).thenReturn(message);
        when(dtoConverter.convertToDto(message, MESSAGE_RESPONSE_DTO_CLASS)).thenReturn(messageResponseDto);

        assertEquals(messageResponseDto, messageService.addMessage(messageProfile));
    }

    @Test
    void addMessageClubNotExist() {
        messageProfile.setClubId(NOT_EXISTING_ID);

        assertThrows(NotExistException.class, () -> messageService.addMessage(messageProfile));
    }

    @Test
    void addMessageSenderNotExist() {
        messageProfile.setSenderId(NOT_EXISTING_ID);

        when(clubRepository.existsById(EXISTING_ID)).thenReturn(true);

        assertThrows(NotExistException.class, () -> messageService.addMessage(messageProfile));
    }

    @Test
    void addMessageRecipientNotExist() {
        messageProfile.setRecipientId(NOT_EXISTING_ID);

        when(clubRepository.existsById(EXISTING_ID)).thenReturn(true);
        when(userRepository.existsById(EXISTING_ID)).thenReturn(true);

        assertThrows(NotExistException.class, () -> messageService.addMessage(messageProfile));
    }

    @Test
    void getMessageById() {
        when(messageRepository.findById(EXISTING_ID)).thenReturn(Optional.of(message));

        assertEquals(message, messageService.getMessageById(EXISTING_ID));
    }

    @Test
    void getMessageByIdMessageNotExist() {
        when(messageRepository.findById(NOT_EXISTING_ID)).thenReturn(Optional.empty());

        assertThrows(NotExistException.class, () -> messageService.getMessageById(NOT_EXISTING_ID));
    }

    @Test
    void getMessageResponseById() {
        when(messageRepository.findById(EXISTING_ID)).thenReturn(Optional.of(message));
        when(dtoConverter.convertToDto(message, MESSAGE_RESPONSE_DTO_CLASS)).thenReturn(messageResponseDto);

        assertEquals(messageResponseDto, messageService.getMessageResponseById(EXISTING_ID));
    }

    @Test
    void updateMessageTextById() {
        Message messageUpdatedText = message.withText(UPDATED_TEXT);
        messageResponseDto.setText(UPDATED_TEXT);

        when(messageRepository.findById(EXISTING_ID)).thenReturn(Optional.of(message));
        when(messageRepository.save(messageUpdatedText)).thenReturn(messageUpdatedText);
        when(dtoConverter.convertToDto(messageUpdatedText, MESSAGE_RESPONSE_DTO_CLASS)).thenReturn(messageResponseDto);

        assertEquals(messageResponseDto, messageService.updateMessageTextById(EXISTING_ID, updateTextDto));
    }

    @Test
    void updateMessageIsActiveById() {
        Message messageUpdatedIsActive = message.withIsActive(false);
        messageResponseDto.setIsActive(false);

        when(messageRepository.findById(EXISTING_ID)).thenReturn(Optional.of(message));
        when(messageRepository.save(messageUpdatedIsActive)).thenReturn(messageUpdatedIsActive);
        when(dtoConverter.convertToDto(messageUpdatedIsActive, MESSAGE_RESPONSE_DTO_CLASS))
                .thenReturn(messageResponseDto);

        assertEquals(messageResponseDto, messageService.updateMessageIsActiveById(EXISTING_ID, updateIsActiveDto));
    }

    @Test
    void deleteMessageById() {
        when(messageRepository.findById(EXISTING_ID)).thenReturn(Optional.of(message));
        doNothing().when(messageRepository).deleteById(EXISTING_ID);
        when(dtoConverter.convertToDto(message, MessageArch.class)).thenReturn(messageArch);
        when(archiveService.saveModel(messageArch)).thenReturn(archiveMessage);
        when(dtoConverter.convertToDto(message, MESSAGE_RESPONSE_DTO_CLASS)).thenReturn(messageResponseDto);

        assertEquals(messageResponseDto, messageService.deleteMessageById(EXISTING_ID));
    }

    @Test
    void deleteMessageByIdThrowsExceptions() {
        when(messageRepository.findById(EXISTING_ID)).thenReturn(Optional.of(message));

        doThrow(ValidationException.class).when(messageRepository).deleteById(EXISTING_ID);

        assertThrows(DatabaseRepositoryException.class, () -> messageService.deleteMessageById(EXISTING_ID));
    }
}

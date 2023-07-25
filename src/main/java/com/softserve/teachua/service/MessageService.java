package com.softserve.teachua.service;

import com.softserve.teachua.dto.message.MessageProfile;
import com.softserve.teachua.dto.message.MessageResponseDto;
import com.softserve.teachua.dto.message.MessageUpdateIsAnswered;
import com.softserve.teachua.dto.message.MessageUpdateIsActive;
import com.softserve.teachua.dto.message.MessageUpdateText;
import com.softserve.teachua.model.Message;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * This interface contains all needed methods to manage messages.
 */

public interface MessageService {
    /**
     * Add message.
     *
     * @param messageProfile MessageProfile dto
     * @return dto {@link MessageResponseDto} if a message successfully added
     */
    MessageResponseDto addMessage(MessageProfile messageProfile);

    /**
     * Get a message by id.
     *
     * @param id a message id
     * @return entity {@link Message} by id
     */
    Message getMessageById(Long id);

    /**
     * Get a new message by id.
     *
     * @param id a message id
     * @return entity {@link Message} by id
     */
    Message getNewMessageById(Long id);

    /**
     * This method searches for the {@code List<Message>} entities by the {@code User} id.
     *
     * @param id       put {@code User} id here.
     * @param isSender put true or false if User is sender or not.
     * @return {@code List<Message>}.
     **/
    List<Message> getMessagesByUserId(Long id, boolean isSender);

    /**
     * This method searches for the new {@code List<Message>} entities by the {@code User} id.
     *
     * @param id       put {@code User} id here.
     * @param isSender put true or false if User is sender or not.
     * @return {@code List<Message>}.
     **/
    List<Message> getNewMessagesByUserId(Long id, boolean isSender);

    /**
     * This method searches for a {@link Message} entity, and convert it to the {@link MessageResponseDto}.
     *
     * @param id put {@code Message} id here.
     * @return {@code MessageResponseDto}.
     **/
    MessageResponseDto getMessageResponseById(Long id);

    /**
     * This method searches for the {@code List<Message>} entities by the {@code User} id, and convert it to the
     * {@code List<MessageResponseDto>}.
     *
     * @param id       put {@code User} id here.
     * @param isSender put true or false if User is sender or not.
     * @return {@code List<MessageResponseDto>}.
     **/
    List<MessageResponseDto> getMessageResponsesByUserId(Long id, boolean isSender);

    /**
     * This method searches for a new {@link Message} entity, and convert it to the {@link MessageResponseDto}.
     *
     * @param id put {@code Message} id here.
     * @return {@code MessageResponseDto}.
     **/
    MessageResponseDto getNewMessageResponseById(Long id);

    /**
     * This method searches for the new {@code List<Message>} entities by the {@code User} id, and convert it to the
     * {@code List<MessageResponseDto>}.
     *
     * @param id       put {@code User} id here.
     * @param isSender put true or false if User is sender or not.
     * @return {@code List<MessageResponseDto>}.
     **/
    List<MessageResponseDto> getNewMessageResponsesByUserId(Long id, boolean isSender);


    /**
     * This method searches for a {@link Message} by id, update text in it with {@link MessageUpdateText} data, and
     * returns {@link MessageResponseDto}.
     *
     * @param id                put {@code Message} id here
     * @param messageUpdateText put {@code MessageUpdateText} dto here.
     * @return {@code MessageResponseDto}.
     **/
    MessageResponseDto updateMessageTextById(Long id, MessageUpdateText messageUpdateText);

    /**
     * This method searches for a {@link Message} by id, update text in it with {@link MessageUpdateIsActive} data, and
     * returns {@link MessageResponseDto}.
     *
     * @param id                    put {@code Message} id here
     * @param messageUpdateIsActive put {@code MessageUpdateIsActive} dto here.
     * @return {@code MessageResponseDto}.
     **/
    MessageResponseDto updateMessageIsActiveById(Long id, MessageUpdateIsActive messageUpdateIsActive);

    /**
     * This method searches for a {@link Message} by id, update text in it with {@link MessageUpdateIsAnswered} data, and
     * returns {@link MessageResponseDto}.
     *
     * @param id                    put {@code Message} id here
     * @param messageUpdateIsAnswered put {@code MessageUpdateIsAnswered} dto here.
     * @return {@code MessageResponseDto}.
     **/
    @Transactional
    MessageResponseDto updateMessageIsAnsweredById(Long id, MessageUpdateIsAnswered messageUpdateIsAnswered);

    /**
     * This method delete {@link Message}.
     *
     * @param id put {@code Message} id here.
     * @return {@code MessageResponseDto}.
     **/
    @Transactional
    MessageResponseDto deleteMessageById(Long id);
}

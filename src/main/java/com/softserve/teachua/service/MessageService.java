package com.softserve.teachua.service;

import com.softserve.teachua.dto.message.MessageProfile;
import com.softserve.teachua.dto.message.MessageResponseDto;
import com.softserve.teachua.model.Message;

/**
 * This interface contains all needed methods to manage messages.
 */

public interface MessageService {

    /**
     * This method returns dto {@link MessageResponseDto} if message successfully added.
     *
     * @param messageProfile put {@code MessageProfile} dto here.
     * @return {@code MessageResponseDto}.
     */
    MessageResponseDto addMessage(MessageProfile messageProfile);

    /**
     * This method returns entity {@link Message} by id.
     *
     * @param id put {@code Message} id here.
     * @return {@code Message}.
     */
    Message getMessageById(Long id);

    /**
     * This method searches for a {@link Message} entity, and convert it to the {@link MessageResponseDto}.
     *
     * @param id put {@code Message} id here.
     * @return {@code FeedbackResponse}.
     **/
    MessageResponseDto getMessageResponseById(Long id);

    /**
     * This method searches for a {@link Message} by id,
     * update text in it with {@link MessageProfile} data,
     * and returns {@link MessageResponseDto}.
     *
     * @param id put {@code Message} id here
     * @param messageProfile put {@code MessageProfile} dto here.
     * @return {@code MessageResponseDto}.
     **/
    MessageResponseDto updateMessageById(Long id, MessageProfile messageProfile);

    /**
     * This method delete {@link Message}.
     *
     * @param id put {@code Message} id here.
     * @return {@code MessageResponseDto}.
     **/
    MessageResponseDto deleteMessageById(Long id);
}

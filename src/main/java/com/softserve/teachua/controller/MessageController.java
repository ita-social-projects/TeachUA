package com.softserve.teachua.controller;

import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.dto.message.MessageProfile;
import com.softserve.teachua.dto.message.MessageResponseDto;
import com.softserve.teachua.dto.message.MessageUpdateIsAnswered;
import com.softserve.teachua.dto.message.MessageUpdateIsActive;
import com.softserve.teachua.dto.message.MessageUpdateText;
import com.softserve.teachua.service.MessageService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Tag(name = "message", description = "the Message API")
@SecurityRequirement(name = "api")
@RequiredArgsConstructor
public class MessageController implements Api {
    private final MessageService messageService;

    /**
     * Use this endpoint to create a new Message. The controller returns {@link MessageResponseDto}.
     *
     * @param messageProfile
     *            put {@code MessageProfile} dto here.
     *
     * @return {@code MessageResponseDto}.
     */
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/message")
    public MessageResponseDto addMessage(@Valid @RequestBody MessageProfile messageProfile) {
        return messageService.addMessage(messageProfile);
    }

    /**
     * Use this endpoint to get Message by id. The controller returns {@link MessageResponseDto}.
     *
     * @param id
     *            put {@code Message} id here.
     *
     * @return {@code MessageResponseDto}.
     */
    @PreAuthorize("isAuthenticated() and "
            + "authentication.principal.id == @messageServiceImpl.getMessageById(#id).sender.id or "
            + "authentication.principal.id == @messageServiceImpl.getMessageById(#id).recipient.id")
    @GetMapping("/message/{id}")
    public MessageResponseDto getMessageById(@PathVariable Long id) {
        return messageService.getMessageResponseById(id);
    }

    /**
     * Use this endpoint to get MessageResponses by sender id. The controller returns {@code List<MessageResponseDto>}.
     *
     * @param id
     *            put {@code User} sender id here.
     *
     * @return {@code List<MessageResponseDto>}.
     */
    @PreAuthorize("isAuthenticated() and authentication.principal.id = #id")
    @GetMapping("/messages/sender/{id}")
    public List<MessageResponseDto> getMessagesBySenderId(@PathVariable Long id) {
        return messageService.getMessageResponsesByUserId(id, true);
    }

    /**
     * Use this endpoint to get MessageResponses by recipient id. The controller returns
     * {@code List<MessageResponseDto>}.
     *
     * @param id
     *            put {@code User} recipient id here.
     *
     * @return {@code List<MessageResponseDto>}.
     */
    @PreAuthorize("isAuthenticated() and authentication.principal.id == #id")
    @GetMapping("/messages/recipient/{id}")
    public List<MessageResponseDto> getMessagesByRecipientId(@PathVariable Long id) {
        return messageService.getMessageResponsesByUserId(id, false);
    }

    /**
     * Use this endpoint to get new MessageResponses by recipient id. The controller returns
     * {@code List<MessageResponseDto>}.
     *
     * @param id
     *            put {@code User} recipient id here.
     *
     * @return {@code List<MessageResponseDto>}.
     */
    @PreAuthorize("isAuthenticated() and authentication.principal.id == #id")
    @GetMapping("/messages/recipient-new/{id}")
    public ResponseEntity<List<MessageResponseDto>> getNewMessagesByRecipientId(@PathVariable Long id) {
        List<MessageResponseDto> messages = messageService.getNewMessageResponsesByUserId(id, false);
        return ResponseEntity.ok(messages);
    }

    /**
     * Use this endpoint to update Message text by id. The controller returns {@code MessageResponseDto}.
     *
     * @param id
     *            put {@code Message} id here.
     * @param updateText
     *            put {@code MessageUpdateText} dto here.
     *
     * @return {@code MessageResponseDto}.
     */
    @PreAuthorize("isAuthenticated() and "
            + "authentication.principal.id == @messageServiceImpl.getMessageById(#id).sender.id")
    @PutMapping("/message/text/{id}")
    public MessageResponseDto updateMessageTextById(@PathVariable Long id,
            @Valid @RequestBody MessageUpdateText updateText) {
        return messageService.updateMessageTextById(id, updateText);
    }

    /**
     * Use this endpoint to update Message isActive by id. The controller returns {@code MessageResponseDto}.
     *
     * @param id
     *            put {@code Message} id here.
     * @param updateIsActive
     *            put {@code MessageUpdateIsActive} dto here.
     *
     * @return {@code MessageResponseDto}.
     */
    @PreAuthorize("isAuthenticated() and "
            + "authentication.principal.id == @messageServiceImpl.getMessageById(#id).recipient.id")
    @PutMapping("/message/active/{id}")
    public MessageResponseDto updateMessageIsActiveById(@PathVariable Long id,
            @Valid @RequestBody MessageUpdateIsActive updateIsActive) {
        return messageService.updateMessageIsActiveById(id, updateIsActive);
    }

    /**
     * Use this endpoint to update Message isAnswered by id. The controller returns {@code MessageResponseDto}.
     *
     * @param id
     *            put {@code Message} id here.
     * @param updateIsAnswered
     *            put {@code MessageUpdateAnswered} dto here.
     *
     * @return {@code MessageResponseDto}.
     */
    @PreAuthorize("isAuthenticated() and "
            + "authentication.principal.id == @messageServiceImpl.getMessageById(#id).recipient.id")
    @PutMapping("/message/answered/{id}")
    public MessageResponseDto updateMessageIsAnsweredById(@PathVariable Long id,
                                                          @Valid @RequestBody MessageUpdateIsAnswered updateIsAnswered) {
        return messageService.updateMessageIsAnsweredById(id, updateIsAnswered);
    }

    /**
     * Use this endpoint to delete Message by id. The controller returns {@code MessageResponseDto}.
     *
     * @param id
     *            - put {@code Message} id here.
     *
     * @return {@code MessageResponseDto}.
     */
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/message/{id}")
    public MessageResponseDto deleteMessageById(@PathVariable Long id) {
        return messageService.deleteMessageById(id);
    }
}

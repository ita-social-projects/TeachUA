package com.softserve.teachua.controller;

import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.dto.message.MessageProfile;
import com.softserve.teachua.dto.message.MessageResponseDto;
import com.softserve.teachua.service.MessageService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@Tag(name = "message", description = "the Message API")
@SecurityRequirement(name = "api")
@RequiredArgsConstructor
public class MessageController implements Api {
    private final MessageService messageService;
    private final PasswordEncoder passwordEncoder;

    /**
     * Use this endpoint to create a new Message.
     * The controller returns {@link MessageResponseDto}.
     *
     * @param messageProfile put {@code MessageProfile} dto here.
     * @return {@code MessageResponseDto}.
     */
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/message")
    public MessageResponseDto addMessage(
            @Valid
            @RequestBody MessageProfile messageProfile) {
        return messageService.addMessage(messageProfile);
    }

    /**
     * Use this endpoint to get Message by id.
     * The controller returns {@link  MessageResponseDto}.
     *
     * @param id put {@code Message} id here.
     * @return {@code MessageResponseDto}.
     */
    @PreAuthorize("isAuthenticated() and " +
            "authentication.principal.id == @messageServiceImpl.getMessageById(#id).sender.id or " +
            "authentication.principal.id == @messageServiceImpl.getMessageById(#id).recipient.id")
    @GetMapping("/message/{id}")
    public MessageResponseDto getMessageById(@PathVariable Long id) {
        return messageService.getMessageResponseById(id);
    }

    /**
     * Use this endpoint to update Message by id.
     * The controller returns {@code MessageResponseDto}.
     *
     * @param id put {@code Message} id here.
     * @param messageProfile put {@code MessageProfile} dto here.
     * @return {@code MessageResponseDto}.
     */
    @PreAuthorize("isAuthenticated() and " +
            "authentication.principal.id == @messageServiceImpl.getMessageById(#id).sender.id")
    @PutMapping("/message/{id}")
    public MessageResponseDto updateMessageById(@PathVariable Long id,
                                                @Valid @RequestBody MessageProfile messageProfile) {
        return messageService.updateMessageById(id, messageProfile);
    }

    /**
     * Use this endpoint to delete Message by id.
     * The controller returns {@code MessageResponseDto}.
     *
     * @param id - put {@code Message} id here.
     * @return {@code MessageResponseDto}.
     */
    @PreAuthorize("isAuthenticated() and " +
            "authentication.principal.id == @messageServiceImpl.getMessageById(#id).sender.id")
    @DeleteMapping("/message/{id}")
    public MessageResponseDto deleteMessageById(@PathVariable Long id) {
        return messageService.deleteMessageById(id);
    }
}

package com.softserve.teachua.dto.viberbot;

import com.softserve.teachua.dto.viberbot.message.Message;
import lombok.Data;

@Data
public class RequestDto {
    private String event;
    private Sender sender;
    private Message message;
}

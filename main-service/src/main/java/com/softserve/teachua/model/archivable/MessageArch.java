package com.softserve.teachua.model.archivable;

import com.softserve.commons.util.marker.Convertible;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class MessageArch implements Convertible/*, Archivable*/ {
    private Long id;
    private Long clubId;
    private LocalDateTime date;
    private Long senderId;
    private Long recipientId;
    private String text;
    private Boolean isActive;

    //todo@
    /*
    @Override
    public Class<MessageServiceImpl> getServiceClass() {
        return MessageServiceImpl.class;
    }
    */
}

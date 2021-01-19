package com.softserve.teachua.controller.club;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.teachua.dto.controller.club.SuccessDeletedClubContact;
import org.springframework.web.bind.annotation.*;

@RestController
public class ClubContactController {

    /**
     * The controller returns information {@code ...} about club's contact.
     *
     * @param clubId - put club id.
     * @param contactId - put club's contact id.
     * @return new {@code ...}.
     * @author Denis Burko
     */
    @GetMapping("/club/{clubId}/contact/{contactId}")
    public Object getClubContact(@PathVariable Long clubId,
                                 @PathVariable Long contactId) throws JsonProcessingException {
        return new ObjectMapper().readValue("{ \"clubId\" : "+ clubId +", \"contactId\" : "+ contactId +" }", Object.class);
    }

    /**
     * The controller returns dto {@code ...} of created club's contact.
     *
     * @param id - put club id.
     * @param name - insert contact name.
     * @return new {@code ...}.
     * @author Denis Burko
     */
    @PostMapping("/club/{id}/contact")
    public Object addClubContact(
            @PathVariable Long id,
            @RequestParam String name) throws JsonProcessingException {
        return new ObjectMapper().readValue("{ \"id\" : "+ id +", \"name\" : "+ name +" }", Object.class);
    }

    /**
     * The controller returns dto {@code SuccessDeletedClubContact} of deleted club's contact.
     *
     * @param clubId - put club id.
     * @param contactId - put contact id.
     * @return new {@code SuccessDeletedClubContact}.
     * @author Denis Burko
     */
    @DeleteMapping("/club/{clubId}/contact")
    public SuccessDeletedClubContact deleteClubContact(
            @PathVariable Integer clubId,
            @RequestParam Integer contactId) {
        return SuccessDeletedClubContact.builder()
                .clubId(clubId)
                .contactId(contactId)
                .build();
    }
}

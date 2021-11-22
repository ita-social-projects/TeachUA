package com.softserve.teachua.controller;

import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.dto.contactType.ContactTypeProfile;
import com.softserve.teachua.dto.contactType.ContactTypeResponse;
import com.softserve.teachua.dto.contactType.SuccessCreatedContactType;
import com.softserve.teachua.model.ContactType;
import com.softserve.teachua.service.ContactTypeService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Tag(name="contact", description="the Contact API")
@SecurityRequirement(name = "api")
public class ContactTypeController implements Api {

    private final ContactTypeService contactTypeService;

    @Autowired
    public ContactTypeController(ContactTypeService contactTypeService) {
        this.contactTypeService = contactTypeService;
    }

    /**
     * Use this endpoint to get contact type by id.
     * The controller returns {@code ContactType}.
     * @param id - put id here.
     * @return {@code ContactType}.
     */
    @GetMapping("/contact-type-view/{id}")
    public ContactType getContactType(@PathVariable Long id) {
        return contactTypeService.getContactTypeById(id);
    }

    /**
     * Use this endpoint to create contact type.
     * The controller returns {@code SuccessCreatedContactType}.
     * @param contactTypeProfile - put contact type info here.
     * @return new {@code SuccessCreatedContactType}.
     */
    @PostMapping("/contact-type")
    public SuccessCreatedContactType addContactType(@Valid @RequestBody ContactTypeProfile contactTypeProfile) {
        return contactTypeService.addContactType(contactTypeProfile);
    }

    /**
     * Use this endpoint to update contact type by id.
     * The controller returns {@code ContactTypeProfile}.
     * @param id - put contact type id here.
     * @param contactTypeProfile - put contact type info here.
     * @return {@code ContactTypeProfile}.
     */
    @PutMapping("/contact-type/{id}")
    public ContactTypeProfile updateContactType(@PathVariable Long id, @Valid @RequestBody ContactTypeProfile contactTypeProfile) {
        return contactTypeService.updateContactType(id, contactTypeProfile);
    }

    /**
     * Use this endpoint to delete contact type by id.
     * The controller returns {@code ContactTypeResponse}.
     * @param id - put id here.
     * @return {@code ContactTypeResponse}.
     */
    @DeleteMapping("/contact-type/{id}")
    public ContactTypeResponse deleteContactType(@PathVariable Long id) {
        return contactTypeService.deleteContactTypeById(id);
    }

    /**
     * Use this endpoint to get all contact types.
     * The controller returns {@code List<ContactTypeResponse>}.
     * @return new {@code List<ContactTypeResponse>}.
     */
    @GetMapping("/contact-types")
    public List<ContactTypeResponse> getContactTypes() {
        return contactTypeService.getListOfContactTypes();
    }
}

package com.softserve.teachua.controller;

import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.dto.contactType.ContactTypeProfile;
import com.softserve.teachua.dto.contactType.ContactTypeResponse;
import com.softserve.teachua.dto.contactType.SuccessCreatedContactType;
import com.softserve.teachua.model.ContactType;
import com.softserve.teachua.service.ContactTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class ContactTypeController implements Api {

    private final ContactTypeService contactTypeService;

    @Autowired
    public ContactTypeController(ContactTypeService contactTypeService) {
        this.contactTypeService = contactTypeService;
    }

    @GetMapping("/contact-type-view/{id}")
    public ContactType getContactType(@PathVariable Long id) {
        return contactTypeService.getContactTypeById(id);
    }

    @PostMapping("/contact-type")
    public SuccessCreatedContactType addContactType(@Valid @RequestBody ContactTypeProfile contactTypeProfile) {
        return contactTypeService.addContactType(contactTypeProfile);
    }

    @PutMapping("/contact-type/{id}")
    public ContactTypeProfile updateContactType(@PathVariable Long id, @Valid @RequestBody ContactTypeProfile contactTypeProfile) {
        return contactTypeService.updateContactType(id, contactTypeProfile);
    }

    @DeleteMapping("/contact-type/{id}")
    public ContactTypeResponse deleteContactType(@PathVariable Long id) {
        return contactTypeService.deleteContactTypeById(id);
    }

    @GetMapping("/contact-types")
    public List<ContactTypeResponse> getContactTypes() {
        return contactTypeService.getListOfContactTypes();
    }
}

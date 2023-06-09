package com.softserve.club.controller;

import com.softserve.club.controller.marker.Api;
import com.softserve.club.dto.contact_type.ContactTypeProfile;
import com.softserve.club.dto.contact_type.ContactTypeResponse;
import com.softserve.club.dto.contact_type.SuccessCreatedContactType;
import com.softserve.club.model.ContactType;
import com.softserve.club.service.ContactTypeService;
import com.softserve.club.util.annotation.AllowedRoles;
import com.softserve.commons.constant.RoleData;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * This controller is for managing the contact types.
 */

@RestController
//@Tag(name = "contact", description = "the Contact API")
//@SecurityRequirement(name = "api")
public class ContactTypeController implements Api {
    private final ContactTypeService contactTypeService;

    @Autowired
    public ContactTypeController(ContactTypeService contactTypeService) {
        this.contactTypeService = contactTypeService;
    }

    /**
     * Use this endpoint to get contact type by id. The controller returns {@code ContactType}.
     *
     * @param id
     *            - put id here.
     *
     * @return {@code ContactType}.
     */
    @GetMapping("/contact-type-view/{id}")
    public ContactType getContactType(@PathVariable Long id) {
        return contactTypeService.getContactTypeById(id);
    }

    /**
     * Use this endpoint to create contact type. The controller returns {@code SuccessCreatedContactType}.
     *
     * @param contactTypeProfile
     *            - put contact type info here.
     *
     * @return new {@code SuccessCreatedContactType}.
     */
    @AllowedRoles(RoleData.ADMIN)
    @PostMapping("/contact-type")
    public SuccessCreatedContactType addContactType(@Valid @RequestBody ContactTypeProfile contactTypeProfile) {
        return contactTypeService.addContactType(contactTypeProfile);
    }

    /**
     * Use this endpoint to update contact type by id. The controller returns {@code ContactTypeProfile}.
     *
     * @param id
     *            - put contact type id here.
     * @param contactTypeProfile
     *            - put contact type info here.
     *
     * @return {@code ContactTypeProfile}.
     */
    @AllowedRoles(RoleData.ADMIN)
    @PutMapping("/contact-type/{id}")
    public ContactTypeProfile updateContactType(@PathVariable Long id,
            @Valid @RequestBody ContactTypeProfile contactTypeProfile) {
        return contactTypeService.updateContactType(id, contactTypeProfile);
    }

    /**
     * Use this endpoint to delete contact type by id. The controller returns {@code ContactTypeResponse}.
     *
     * @param id
     *            - put id here.
     *
     * @return {@code ContactTypeResponse}.
     */
    @AllowedRoles(RoleData.ADMIN)
    @DeleteMapping("/contact-type/{id}")
    public ContactTypeResponse deleteContactType(@PathVariable Long id) {
        return contactTypeService.deleteContactTypeById(id);
    }

    /**
     * Use this endpoint to get all contact types. The controller returns {@code List<ContactTypeResponse>}.
     *
     * @return new {@code List<ContactTypeResponse>}.
     */
    @GetMapping("/contact-types")
    public List<ContactTypeResponse> getContactTypes() {
        return contactTypeService.getListOfContactTypes();
    }
}

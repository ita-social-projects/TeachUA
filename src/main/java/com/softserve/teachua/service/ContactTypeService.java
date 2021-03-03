package com.softserve.teachua.service;

import com.softserve.teachua.dto.contactType.ContactTypeProfile;
import com.softserve.teachua.dto.contactType.ContactTypeResponse;
import com.softserve.teachua.dto.contactType.SuccessCreatedContactType;
import com.softserve.teachua.model.ContactType;

import java.util.List;

public interface ContactTypeService {

    List<ContactTypeResponse> getListOfContactTypes();

    SuccessCreatedContactType addContactType(ContactTypeProfile contactTypeProfile);

    ContactTypeProfile updateContactType(Long id, ContactTypeProfile contactTypeProfile);

    ContactTypeResponse deleteContactTypeById(Long id);

    ContactType getContactTypeById(Long id);

    ContactTypeProfile getContactTypeProfileById(Long id);
}

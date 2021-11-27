package com.softserve.teachua.service.impl;

import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.contactType.ContactTypeProfile;
import com.softserve.teachua.dto.contactType.ContactTypeResponse;
import com.softserve.teachua.dto.contactType.SuccessCreatedContactType;
import com.softserve.teachua.exception.AlreadyExistException;
import com.softserve.teachua.exception.DatabaseRepositoryException;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.ContactType;
import com.softserve.teachua.repository.ContactTypeRepository;
import com.softserve.teachua.service.ArchiveService;
import com.softserve.teachua.service.ContactTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ValidationException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class ContactTypeServiceImpl implements ContactTypeService {
    private static final String CONTACT_TYPE_ALREADY_EXIST = "Contact type already exist with name: %s";
    private static final String CONTACT_TYPE_NOT_FOUND_BY_ID = "Contact type not found by id: %s";
    private static final String CONTACT_TYPE_DELETING_ERROR = "Can't delete contact type cause of relationship";

    private final DtoConverter dtoConverter;
    private final ArchiveService archiveService;
    private final ContactTypeRepository contactTypeRepository;

    @Autowired
    public ContactTypeServiceImpl(DtoConverter dtoConverter, ArchiveService archiveService,
                                  ContactTypeRepository contactTypeRepository) {
        this.dtoConverter = dtoConverter;
        this.archiveService = archiveService;
        this.contactTypeRepository = contactTypeRepository;
    }

    @Override
    public List<ContactTypeResponse> getListOfContactTypes() {
        List<ContactTypeResponse> contactTypeResponses = contactTypeRepository.findAll()
                .stream()
                .map(type -> (ContactTypeResponse) dtoConverter.convertToDto(type, ContactTypeResponse.class))
                .collect(Collectors.toList());

        log.debug("**/getting list of contact type = " + contactTypeResponses);

        return contactTypeResponses;
    }

    @Override
    public SuccessCreatedContactType addContactType(ContactTypeProfile contactTypeProfile) {
        if (isContactTypeExistByName(contactTypeProfile.getName())) {
            throw new AlreadyExistException(String.format(CONTACT_TYPE_ALREADY_EXIST, contactTypeProfile.getName()));
        }

        ContactType contactType = contactTypeRepository.save(dtoConverter
                .convertToEntity(contactTypeProfile, new ContactType()));
        log.debug("**/adding new contact type = " + contactType);
        return dtoConverter.convertToDto(contactType, SuccessCreatedContactType.class);
    }

    @Override
    public ContactTypeProfile updateContactType(Long id, ContactTypeProfile contactTypeProfile) {
        ContactType contactType = getContactTypeById(id);
        ContactType newContactType = dtoConverter.convertToEntity(contactTypeProfile, contactType)
                .withId(id);

        log.debug("**/updating contact type by id = " + newContactType);

        return dtoConverter.convertToDto(contactTypeRepository.save(newContactType), ContactTypeProfile.class);
    }

    @Override
    public ContactTypeResponse deleteContactTypeById(Long id) {
        ContactType contactType = getContactTypeById(id);

//        archiveService.saveModel(contactType);

        try {
            contactTypeRepository.deleteById(id);
            contactTypeRepository.flush();
        } catch (DataAccessException | ValidationException e) {
            throw new DatabaseRepositoryException(CONTACT_TYPE_DELETING_ERROR);
        }

        log.debug("contact type {} was successfully deleted", contactType);
        return dtoConverter.convertToDto(contactType, ContactTypeResponse.class);
    }

    @Override
    public ContactType getContactTypeById(Long id) {
        Optional<ContactType> optionalContactType = getOptionalContactTypeById(id);
        if (!optionalContactType.isPresent()) {
            throw new NotExistException(String.format(CONTACT_TYPE_NOT_FOUND_BY_ID, id));
        }

        ContactType contactType = optionalContactType.get();
        return contactType;
    }

    @Override
    public ContactTypeProfile getContactTypeProfileById(Long id) {
        return dtoConverter.convertToDto(getContactTypeById(id), ContactTypeProfile.class);
    }

    private Optional<ContactType> getOptionalContactTypeById(Long id) {
        return contactTypeRepository.findById(id);
    }

    private boolean isContactTypeExistByName(String name) {
        return contactTypeRepository.existsByName(name);
    }
}

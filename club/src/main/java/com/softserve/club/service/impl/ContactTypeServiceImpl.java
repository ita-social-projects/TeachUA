package com.softserve.club.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.amqp.message_producer.impl.ArchiveMQMessageProducer;
import com.softserve.club.dto.contact_type.ContactTypeProfile;
import com.softserve.club.dto.contact_type.ContactTypeResponse;
import com.softserve.club.dto.contact_type.SuccessCreatedContactType;
import com.softserve.club.model.ContactType;
import com.softserve.club.repository.ContactTypeRepository;
import com.softserve.club.service.ContactTypeService;
import com.softserve.commons.client.ArchiveClient;
import com.softserve.commons.exception.AlreadyExistException;
import com.softserve.commons.exception.DatabaseRepositoryException;
import com.softserve.commons.exception.NotExistException;
import com.softserve.commons.util.converter.DtoConverter;
import jakarta.validation.ValidationException;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class ContactTypeServiceImpl implements ContactTypeService {
    private static final String CONTACT_TYPE_ALREADY_EXIST = "Contact type already exist with name: %s";
    private static final String CONTACT_TYPE_NOT_FOUND_BY_ID = "Contact type not found by id: %s";
    private static final String CONTACT_TYPE_DELETING_ERROR = "Can't delete contact type cause of relationship";
    private final DtoConverter dtoConverter;
    private final ContactTypeRepository contactTypeRepository;
    private final ArchiveMQMessageProducer<ContactType> archiveMQMessageProducer;
    private final ArchiveClient archiveClient;
    private final ObjectMapper objectMapper;

    public ContactTypeServiceImpl(DtoConverter dtoConverter, ContactTypeRepository contactTypeRepository,
                                  ArchiveMQMessageProducer<ContactType> archiveMQMessageProducer,
                                  ArchiveClient archiveClient, ObjectMapper objectMapper) {
        this.dtoConverter = dtoConverter;
        this.contactTypeRepository = contactTypeRepository;
        this.archiveMQMessageProducer = archiveMQMessageProducer;
        this.archiveClient = archiveClient;
        this.objectMapper = objectMapper;
    }

    @Override
    public List<ContactTypeResponse> getListOfContactTypes() {
        List<ContactTypeResponse> contactTypeResponses = contactTypeRepository.findAll().stream()
                .map(type -> (ContactTypeResponse) dtoConverter.convertToDto(type, ContactTypeResponse.class))
                .toList();

        log.debug("**/getting list of contact type = " + contactTypeResponses);

        return contactTypeResponses;
    }

    @Override
    public SuccessCreatedContactType addContactType(ContactTypeProfile contactTypeProfile) {
        if (isContactTypeExistByName(contactTypeProfile.getName())) {
            throw new AlreadyExistException(String.format(CONTACT_TYPE_ALREADY_EXIST, contactTypeProfile.getName()));
        }

        ContactType contactType = contactTypeRepository
                .save(dtoConverter.convertToEntity(contactTypeProfile, new ContactType()));
        log.debug("**/adding new contact type = " + contactType);
        return dtoConverter.convertToDto(contactType, SuccessCreatedContactType.class);
    }

    @Override
    public ContactTypeProfile updateContactType(Long id, ContactTypeProfile contactTypeProfile) {
        ContactType contactType = getContactTypeById(id);
        ContactType newContactType = dtoConverter.convertToEntity(contactTypeProfile, contactType).withId(id);

        log.debug("**/updating contact type by id = " + newContactType);

        return dtoConverter.convertToDto(contactTypeRepository.save(newContactType), ContactTypeProfile.class);
    }

    @Override
    public ContactTypeResponse deleteContactTypeById(Long id) {
        ContactType contactType = getContactTypeById(id);

        try {
            contactTypeRepository.deleteById(id);
            contactTypeRepository.flush();
        } catch (DataAccessException | ValidationException e) {
            throw new DatabaseRepositoryException(CONTACT_TYPE_DELETING_ERROR);
        }

        archiveModel(contactType);

        log.debug("contact type {} was successfully deleted", contactType);
        return dtoConverter.convertToDto(contactType, ContactTypeResponse.class);
    }

    @Override
    public ContactType getContactTypeById(Long id) {
        Optional<ContactType> optionalContactType = getOptionalContactTypeById(id);
        if (optionalContactType.isEmpty()) {
            throw new NotExistException(String.format(CONTACT_TYPE_NOT_FOUND_BY_ID, id));
        }

        return optionalContactType.get();
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

    private void archiveModel(ContactType contactType) {
        archiveMQMessageProducer.publish(contactType);
    }

    @Override
    public void restoreModel(Long id) {
        var contactType = objectMapper.convertValue(
                archiveClient.restoreModel(ContactType.class.getName(), id),
                ContactType.class);
        contactTypeRepository.save(contactType);
    }
}

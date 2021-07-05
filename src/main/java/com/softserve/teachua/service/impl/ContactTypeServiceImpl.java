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
    public ContactTypeServiceImpl(DtoConverter dtoConverter, ArchiveService archiveService, ContactTypeRepository contactTypeRepository) {
        this.dtoConverter = dtoConverter;
        this.archiveService = archiveService;
        this.contactTypeRepository = contactTypeRepository;
    }

    /**
     * The method returns list of dto {@code List<ContactTypeResponse>} of all contact types.
     *
     * @return new {@code List<ContactTypeResponse>}.
     */
    @Override
    public List<ContactTypeResponse> getListOfContactTypes() {
        List<ContactTypeResponse> contactTypeResponses = contactTypeRepository.findAll()
                .stream()
                .map(type -> (ContactTypeResponse) dtoConverter.convertToDto(type, ContactTypeResponse.class))
                .collect(Collectors.toList());

        log.info("**/getting list of contact type = " + contactTypeResponses);

        return contactTypeResponses;
    }

    /**
     * The method returns dto {@code SuccessCreatedContactType} if contact type successfully added.
     *
     * @param contactTypeProfile - place dto with all params.
     * @return new {@code SuccessCreatedContactType}.
     * @throws AlreadyExistException if contact type already exists.
     */
    @Override
    public SuccessCreatedContactType addContactType(ContactTypeProfile contactTypeProfile) {
        if (isContactTypeExistByName(contactTypeProfile.getName())) {
            throw new AlreadyExistException(String.format(CONTACT_TYPE_ALREADY_EXIST, contactTypeProfile.getName()));
        }

        ContactType contactType = contactTypeRepository.save(dtoConverter.convertToEntity(contactTypeProfile, new ContactType()));
        log.info("**/adding new contact type = " + contactType);
        return dtoConverter.convertToDto(contactType, SuccessCreatedContactType.class);
    }

    /**
     * The method returns dto {@code ContactTypeProfile} of updated club.
     *
     * @param id                 - put contact type id
     * @param contactTypeProfile - place body of dto {@code ContactTypeProfile}.
     * @return new {@code ContactTypeProfile}.
     * @throws NotExistException if contact type not exists by id.
     */
    @Override
    public ContactTypeProfile updateContactType(Long id, ContactTypeProfile contactTypeProfile) {
        ContactType contactType = getContactTypeById(id);
        ContactType newContactType = dtoConverter.convertToEntity(contactTypeProfile, contactType)
                .withId(id);

        log.info("**/updating contact type by id = " + newContactType);

        return dtoConverter.convertToDto(contactTypeRepository.save(newContactType), ContactTypeProfile.class);
    }

    /**
     * The method returns dto {@code ContactTypeResponse} of deleted contact type by id.
     *
     * @param id - put contact type id
     * @return new {@code ContactTypeResponse}.
     * @throws DatabaseRepositoryException if contact type contain foreign keys.
     */
    @Override
    public ContactTypeResponse deleteContactTypeById(Long id) {
        ContactType contactType = getContactTypeById(id);

        archiveService.saveModel(contactType);

        try {
            contactTypeRepository.deleteById(id);
            contactTypeRepository.flush();
        } catch (DataAccessException | ValidationException e) {
            throw new DatabaseRepositoryException(CONTACT_TYPE_DELETING_ERROR);
        }

        log.info("contact type {} was successfully deleted", contactType);
        return dtoConverter.convertToDto(contactType, ContactTypeResponse.class);
    }

    /**
     * Method find {@link ContactType}
     *
     * @param id - place contact type id
     * @return ContactType
     */
    @Override
    public ContactType getContactTypeById(Long id) {
        Optional<ContactType> optionalContactType = getOptionalContactTypeById(id);
        if (!optionalContactType.isPresent()) {
            throw new NotExistException(String.format(CONTACT_TYPE_NOT_FOUND_BY_ID, id));
        }

        ContactType contactType = optionalContactType.get();
//        log.info("**/getting contact type by id = " + contactType);
        return contactType;
    }

    /**
     * Method find {@link ContactType}, and convert it to object of DTO class
     *
     * @param id - place contact type id
     * @return new {@code ContactTypeProfile}
     */
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

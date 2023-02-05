package com.softserve.teachua.service;

import com.softserve.teachua.dto.contactType.ContactTypeProfile;
import com.softserve.teachua.dto.contactType.ContactTypeResponse;
import com.softserve.teachua.dto.contactType.SuccessCreatedContactType;
import com.softserve.teachua.exception.AlreadyExistException;
import com.softserve.teachua.exception.DatabaseRepositoryException;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.ContactType;
import java.util.List;

/**
 * This interface contains all needed methods to manage contact types.
 */

public interface ContactTypeService {
    /**
     * The method returns list of dto {@code List<ContactTypeResponse>} of all contact types.
     *
     * @return new {@code List<ContactTypeResponse>}.
     */
    List<ContactTypeResponse> getListOfContactTypes();

    /**
     * The method returns dto {@code SuccessCreatedContactType} if contact type successfully added.
     *
     * @param contactTypeProfile
     *            - place dto with all params.
     *
     * @return new {@code SuccessCreatedContactType}.
     *
     * @throws AlreadyExistException
     *             if contact type already exists.
     */
    SuccessCreatedContactType addContactType(ContactTypeProfile contactTypeProfile);

    /**
     * The method returns dto {@code ContactTypeProfile} of updated club.
     *
     * @param id
     *            - put contact type id
     * @param contactTypeProfile
     *            - place body of dto {@code ContactTypeProfile}.
     *
     * @return new {@code ContactTypeProfile}.
     *
     * @throws NotExistException
     *             if contact type not exists by id.
     */
    ContactTypeProfile updateContactType(Long id, ContactTypeProfile contactTypeProfile);

    /**
     * The method returns dto {@code ContactTypeResponse} of deleted contact type by id.
     *
     * @param id
     *            - put contact type id
     *
     * @return new {@code ContactTypeResponse}.
     *
     * @throws DatabaseRepositoryException
     *             if contact type contain foreign keys.
     */
    ContactTypeResponse deleteContactTypeById(Long id);

    /**
     * Method finds {@link ContactType}.
     *
     * @param id
     *            - place contact type id
     *
     * @return ContactType
     */
    ContactType getContactTypeById(Long id);

    /**
     * Method finds {@link ContactType}, and convert it to object of DTO class.
     *
     * @param id
     *            - place contact type id
     *
     * @return new {@code ContactTypeProfile}
     */
    ContactTypeProfile getContactTypeProfileById(Long id);
}

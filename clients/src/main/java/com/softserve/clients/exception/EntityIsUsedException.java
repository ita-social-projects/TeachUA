package com.softserve.clients.exception;

/**
 * This class is a custom exception for entity that is already used in Database or other data storage. The constructor
 * accepts message for Exception
 *
 * <p>Use {@code throw new EntityIsUsedException("Entity in use")}
 *
 * @author BNazar002
 */
public class EntityIsUsedException extends IllegalStateException {
    private static final long serialVersionUID = 1L;
    private static final String ENTITY_IS_USED_EXCEPTION = "Entity is used";

    public EntityIsUsedException(String message) {
        super(message.isEmpty() ? ENTITY_IS_USED_EXCEPTION : message);
    }

    public EntityIsUsedException() {
        super(ENTITY_IS_USED_EXCEPTION);
    }
}

package com.softserve.commons.util.marker;

@SuppressWarnings({"checkstyle:InterfaceTypeParameterName", "squid:S119"})
public interface Archiver<ID extends Number> {
    void restoreModel(ID id);
}

package com.softserve.teachua.service;

import com.softserve.teachua.dto.location.AddressProfile;
import java.util.List;

public interface AddressService {
    List<AddressProfile> getNotRelativeAddress();

    List<AddressProfile> replaceAllIncorrectCity(List<AddressProfile> addressProfileList);
}

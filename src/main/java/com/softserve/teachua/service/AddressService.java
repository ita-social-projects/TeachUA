package com.softserve.teachua.service;
import java.util.List;
import com.softserve.teachua.dto.location.AddressProfile;

public interface AddressService {
    List<AddressProfile> getNotRelativeAddress();
}

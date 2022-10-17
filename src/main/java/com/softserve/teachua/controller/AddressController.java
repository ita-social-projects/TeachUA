package com.softserve.teachua.controller;

import com.softserve.teachua.constants.RoleData;
import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.dto.location.AddressProfile;
import com.softserve.teachua.service.impl.AddressServiceImpl;
import java.util.List;

import com.softserve.teachua.utils.annotation.AllowedRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AddressController implements Api {

    private final AddressServiceImpl addressService;

    @Autowired
    public AddressController(AddressServiceImpl addressService) {
        this.addressService = addressService;
    }

    @AllowedRoles(RoleData.ADMIN)
    @GetMapping("/getAllBadAddress")
    public List<AddressProfile> getAllAddress() {
        return addressService.getNotRelativeAddress();
    }

    @AllowedRoles(RoleData.ADMIN)
    @PostMapping("/replaceIncorrectCity")
    public List<AddressProfile> replaceIncorrectCity() {
        return addressService.replaceAllIncorrectCity(addressService.getNotRelativeAddress());
    }

}

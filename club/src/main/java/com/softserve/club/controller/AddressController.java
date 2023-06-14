package com.softserve.club.controller;

import com.softserve.club.controller.marker.Api;
import com.softserve.club.dto.location.AddressProfile;
import com.softserve.club.service.impl.AddressServiceImpl;
import com.softserve.club.util.annotation.AllowedRoles;
import com.softserve.commons.constant.RoleData;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/club/address")
public class AddressController implements Api {
    private final AddressServiceImpl addressService;

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

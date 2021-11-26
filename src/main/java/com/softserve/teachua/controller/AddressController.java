package com.softserve.teachua.controller;


import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.dto.location.AddressProfile;
import com.softserve.teachua.service.impl.AddressServiceImpl;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AddressController implements Api {

    private final AddressServiceImpl addressService;

    @Autowired
    public AddressController(AddressServiceImpl addressService) {
        this.addressService = addressService;
    }

    @PreAuthorize("hasAnyRole(T(com.softserve.teachua.constants.RoleData).ADMIN.getDBRoleName())")
    @GetMapping("/getAllBadAddress")
    public List<AddressProfile> getAllAddress(){
        return addressService.getNotRelativeAddress();
    }

}

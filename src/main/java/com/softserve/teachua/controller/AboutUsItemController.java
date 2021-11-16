package com.softserve.teachua.controller;

import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.dto.about_us_item.AboutUsItemProfile;
import com.softserve.teachua.dto.about_us_item.AboutUsItemResponse;
import com.softserve.teachua.dto.about_us_item.NumberDto;
import com.softserve.teachua.service.AboutUsItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
public class AboutUsItemController implements Api {

    private final AboutUsItemService aboutUsItemService;

    @Autowired
    public AboutUsItemController(AboutUsItemService aboutUsItemService) {
        this.aboutUsItemService = aboutUsItemService;
    }

    @GetMapping("/about")
    public List<AboutUsItemResponse> getAboutUsItems(){
        return aboutUsItemService.getListOfAboutUsItemResponses();
    }

    @GetMapping("/about/{id}")
    public AboutUsItemResponse getAboutUsItems(
            @PathVariable Long id
    ){
        return aboutUsItemService.getAboutUsItemResponseById(id);
    }

    @PreAuthorize("hasRole(T(com.softserve.teachua.constants.RoleData).ADMIN.getDBRoleName())")
    @PostMapping("/about")
    public AboutUsItemResponse addAboutUsItem(
            @Valid
            @RequestBody AboutUsItemProfile aboutUsItemProfile
            ){
        return aboutUsItemService.addAboutUsItem(aboutUsItemProfile);
    }

    @PreAuthorize("hasRole(T(com.softserve.teachua.constants.RoleData).ADMIN.getDBRoleName())")
    @PutMapping("/about/{id}")
    public AboutUsItemResponse updateAboutUsItem(
            @PathVariable Long id,
            @Valid
            @RequestBody AboutUsItemProfile aboutUsItemProfile
    ){
        return aboutUsItemService.updateAboutUsItem(id, aboutUsItemProfile);
    }

    @PreAuthorize("hasRole(RoleD)")
    @DeleteMapping("/about/{id}")
    public AboutUsItemResponse deleteAboutUsItem(
            @PathVariable Long id
    ){
        return aboutUsItemService.deleteAboutUsItemById(id);
    }

    @PreAuthorize("hasRole(T(com.softserve.teachua.constants.RoleData).ADMIN.getDBRoleName())")
    @PatchMapping("/about/{id}")
    public String changeOrder(
            @PathVariable Long id,
            @RequestBody NumberDto number
    ){
        aboutUsItemService.changeOrder(id, number.getNumber());
        return "success";
    }

}

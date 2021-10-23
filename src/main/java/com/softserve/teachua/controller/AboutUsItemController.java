package com.softserve.teachua.controller;

import com.softserve.teachua.dto.about_us_item.AboutUsItemProfile;
import com.softserve.teachua.dto.about_us_item.AboutUsItemResponse;
import com.softserve.teachua.service.AboutUsItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
public class AboutUsItemController {

    private final AboutUsItemService aboutUsItemService;

    @Autowired
    public AboutUsItemController(AboutUsItemService aboutUsItemService) {
        this.aboutUsItemService = aboutUsItemService;
    }

    @GetMapping("/about_us_items")
    public List<AboutUsItemResponse> getAboutUsItems(){
        return aboutUsItemService.getListOfAboutUsItemResponses();
    }

    @PostMapping("/about_us_item")
    public AboutUsItemResponse addAboutUsItem(
            @Valid
            @RequestBody AboutUsItemProfile aboutUsItemProfile
            ){
        return aboutUsItemService.addAboutUsItem(aboutUsItemProfile);
    }

    @PutMapping("/about_us_item/{id}")
    public AboutUsItemResponse updateAboutUsItem(
            @PathVariable Long id,
            @Valid
            @RequestBody AboutUsItemProfile aboutUsItemProfile
    ){
        return aboutUsItemService.updateAboutUsItem(id, aboutUsItemProfile);
    }

    @DeleteMapping("/about_us_item/{id}")
    public AboutUsItemResponse deleteAboutUsItem(
            @PathVariable Long id
    ){
        return aboutUsItemService.deleteAboutUsItemById(id);
    }

}

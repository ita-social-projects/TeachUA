package com.softserve.teachua.controller;

import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.dto.child.ChildProfile;
import com.softserve.teachua.dto.child.SuccessCreatedChild;
import com.softserve.teachua.service.ChildService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "api")
@Tag(name = "children", description = "the Children API")
public class ChildController implements Api {
    private final ChildService childService;

    @GetMapping("/child")
    public String test() {
        return "Test";
    }
    @PostMapping("/child")
    public SuccessCreatedChild addChild(@Valid @RequestBody ChildProfile childProfile) {
        log.debug("Add child {}", childProfile);

        return childService.create(childProfile);
    }
}


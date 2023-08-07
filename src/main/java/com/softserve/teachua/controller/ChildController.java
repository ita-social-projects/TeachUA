package com.softserve.teachua.controller;

import com.softserve.teachua.constants.RoleData;
import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.dto.child.ChildProfile;
import com.softserve.teachua.dto.child.ChildResponse;
import com.softserve.teachua.service.ChildService;
import com.softserve.teachua.utils.annotation.AllowedRoles;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @AllowedRoles(RoleData.USER)
    @PostMapping("/child")
    public ResponseEntity<ChildResponse> addChild(@Valid @RequestBody ChildProfile childProfile) {
        log.debug("Add child {}", childProfile);
        ChildResponse createdChild = childService.create(childProfile);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdChild);
    }
}


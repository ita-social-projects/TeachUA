package com.softserve.teachua.controller;

import com.softserve.teachua.constants.RoleData;
import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.dto.location.LocationProfile;
import com.softserve.teachua.model.Location;
import com.softserve.teachua.service.LocationService;
import com.softserve.teachua.utils.annotation.AllowedRoles;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * This controller is for managing the banner items.
 */

@Slf4j
@RestController
@Tag(name = "location", description = "the Location API")
@SecurityRequirement(name = "api")
public class LocationController implements Api {
    private final LocationService locationService;

    @Autowired
    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    /**
     * The controller returns entity {@code Location}.
     *
     * @param id
     *            - put Location id.
     *
     * @return new {@code Location}.
     */
    @GetMapping("/location/{id}")
    public Location getLocation(@PathVariable Long id) {
        return locationService.getLocationById(id);
    }

    /**
     * The controller returns list of entities {@code List<Location>} of all locations.
     *
     * @return new {@code List<Location>}.
     */
    @GetMapping("/locations")
    public List<Location> getLocations() {
        return locationService.getListOfAllLocations();
    }

    /**
     * The controller returns entity {@code Location} of created location.
     *
     * @param locationProfile
     *            - place body to {@link LocationProfile}.
     *
     * @return new {@code Location}.
     */
    @AllowedRoles(RoleData.ADMIN)
    @PostMapping("/location")
    public Location addLocation(@Valid @RequestBody LocationProfile locationProfile) {
        return locationService.addLocation(locationProfile);
    }

    /**
     * The controller returns entity {@code Location } of updated location.
     *
     * @param id
     *            - put Location id.
     * @param locationProfile
     *            - place body to {@link LocationProfile}.
     *
     * @return new {@code Location}.
     */
    @AllowedRoles(RoleData.ADMIN)
    @PutMapping("/location/{id}")
    public Location updateLocation(@PathVariable Long id, @Valid @RequestBody LocationProfile locationProfile) {
        return locationService.updateLocation(id, locationProfile);
    }

    /**
     * The controller returns dto {@code LocationProfile} of deleted location by id.
     *
     * @param id
     *            - put Location id.
     *
     * @return new {@code LocationProfile}.
     */
    @AllowedRoles(RoleData.ADMIN)
    @DeleteMapping("/location/{id}")
    public LocationProfile deleteLocation(@PathVariable Long id) {
        return locationService.deleteLocationById(id);
    }
}

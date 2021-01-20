package com.softserve.teachua.controller;

import com.softserve.teachua.entity.Coordinates;
import com.softserve.teachua.repository.GoogleMapRepository;
import com.softserve.teachua.service.GoogleMapService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class GoogleMapController {
    private GoogleMapService googleMapService;
    private Environment environment;

    @Autowired
    public GoogleMapController(GoogleMapService googleMapService, Environment environment) {
        this.googleMapService = googleMapService;
        this.environment = environment;
    }

    @GetMapping("/map/show-club")
    public String getMap(@RequestParam("clubName") String clubName, @RequestParam("clubId") Integer clubId,
                         @RequestParam("clubAddress") String address, Model model) {

        Coordinates coordinates = googleMapService.getClubCoordinatesByAddress(address);

        model.addAttribute("googleMapKey", environment.getProperty("googleMapKey"));
        model.addAttribute("latitude", coordinates.getLatitude());
        model.addAttribute("longitude", coordinates.getLongitude());
        model.addAttribute("clubName", clubName);
        return "map-page";
    }
}

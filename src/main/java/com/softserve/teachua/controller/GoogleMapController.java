package com.softserve.teachua.controller;

import com.softserve.teachua.entity.Coordinates;
import com.softserve.teachua.repository.GoogleMapRepository;
import com.softserve.teachua.service.GoogleMapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class GoogleMapController {
    private GoogleMapService googleMapService;

    @Autowired
    public GoogleMapController(GoogleMapService googleMapService) {
        this.googleMapService = googleMapService;
    }

    @GetMapping("/map/show-club")
    public String getMap(@RequestParam("clubName") String clubName, @RequestParam("clubId") Integer clubId,
                         @RequestParam("clubAddress") String address, Model model) {

        Coordinates coordinates = googleMapService.getClubCoordinatesByAddress(address);

        model.addAttribute("latitude", coordinates.getLatitude());
        model.addAttribute("longitude", coordinates.getLongitude());
        model.addAttribute("clubName", clubName);
        return "map-page";
    }
}

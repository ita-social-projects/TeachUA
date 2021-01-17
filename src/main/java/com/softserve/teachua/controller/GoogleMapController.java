package com.softserve.teachua.controller;

import com.softserve.teachua.entity.Coordinates;
import com.softserve.teachua.repository.GoogleMapRepository;
import com.softserve.teachua.service.GoogleMapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class GoogleMapController {
    private GoogleMapService googleMapService;

    @Autowired
    public GoogleMapController(GoogleMapService googleMapService) {
        this.googleMapService = googleMapService;
    }

    @GetMapping("/map/{clubName}/{clubId}")
    public String getMap(@PathVariable("clubName") String clubName, @PathVariable("clubId") Integer clubId, Model model) {
        Coordinates coordinates = googleMapService.getClubCoordinatesById(clubId);
        model.addAttribute("clubName", clubName);
        model.addAttribute("latitude", coordinates.getLatitude());
        model.addAttribute("longitude", coordinates.getLongitude());
        return "map-page";
    }
}

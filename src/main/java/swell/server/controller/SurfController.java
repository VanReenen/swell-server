package swell.server.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import swell.server.constant.SurfSwellConstants;
import swell.server.model.LocationDTO;
import swell.server.model.OceanicDataDTO;
import swell.server.service.SurfSwellService;

/**
 * Controller for external clients to be used to get summarised data for surf
 * forecasting.
 */
@RestController
public class SurfController {

    private final SurfSwellService surfSwellService;

    public SurfController(SurfSwellService surfSwellService) {
        this.surfSwellService = surfSwellService;
    }

    /**
     * Used to search for known locations and to get their latitudes and longitudes.
     * 
     * @param name the place's name
     * @return a {@link List} of locations that match the name
     */
    @GetMapping(SurfSwellConstants.Paths.SEARCH_LOCATION)
    public List<LocationDTO> searchLocations(String name) {
        return surfSwellService.searchLocations(name);
    }

    /**
     * 
     * @param latitude  of the desired location.
     * @param longitude of the desired location.
     * 
     * @return the {@link OceanicDataDTO} of the specified location.
     */
    @GetMapping(SurfSwellConstants.Paths.SURF_DATA)
    public OceanicDataDTO getSurfData(@RequestParam(SurfSwellConstants.Parameters.LOCATION) LocationDTO location) {
        return surfSwellService.getOceanicData(location.latitude(), location.longitude());
    }

}

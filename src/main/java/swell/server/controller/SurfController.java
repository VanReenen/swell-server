package swell.server.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import swell.server.constant.SurfSwellConstants;
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
     * Used mostly to test data retrieval of front-end. Not for use other than
     * getting data for Muizies.
     */
    @GetMapping(SurfSwellConstants.Paths.MUIZENBERG_SURF_DATA)
    public OceanicDataDTO getMuizenbergSurfData() {
        return surfSwellService.getOceanicData(SurfSwellConstants.Locations.MUIZENBERG_LATITUDE,
                SurfSwellConstants.Locations.MUIZENBERG_LONGITUDE);
    }

    /**
     * 
     * @param latitude  of the desired location.
     * @param longitude of the desired location.
     * 
     * @return the {@link OceanicDataDTO} of the specified location.
     */
    @GetMapping(SurfSwellConstants.Paths.SURF_DATA)
    public OceanicDataDTO getSurfData(@RequestParam(SurfSwellConstants.Parameters.LATITUDE) String latitude,
            @RequestParam(SurfSwellConstants.Parameters.LONGITUDE) String longitude) {
        return surfSwellService.getOceanicData(latitude, longitude);
    }

}

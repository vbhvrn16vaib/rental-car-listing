package com.core.vehicle_listing.controller;

import com.core.vehicle_listing.model.VehicleDetails;
import com.core.vehicle_listing.service.VehicleListingService;
import java.util.List;
import java.util.Map;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class VehicleListingController {

  private final VehicleListingService vehicleListingService;

  public VehicleListingController(
      VehicleListingService vehicleListingService) {
    this.vehicleListingService = vehicleListingService;
  }

  @PostMapping(value = "/upload/{type}/{dealer_id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<?> uploadListings(@RequestParam("file") MultipartFile file,@PathVariable("type") String type, @PathVariable("dealer_id") String dealerId){
    return ResponseEntity.ok(vehicleListingService.saveFromFile(file, type, dealerId));
  }

  @PostMapping("/vendor_listings/{dealer_id}")
  public ResponseEntity<?> uploadListings(@RequestBody List<VehicleDetails> request, @PathVariable("dealer_id") String dealerId){
    return ResponseEntity.ok(vehicleListingService.save(request, dealerId));
  }

  @GetMapping("/vendor_listings/{dealer_id}")
  public ResponseEntity<?> getByDealersId(@PathVariable("dealer_id") String dealerId){
    return ResponseEntity.ok(vehicleListingService.getAllListingOfDealer(dealerId));
  }

  @GetMapping("/search")
  public ResponseEntity<?> search(@RequestParam(required = false) Map<String,String> allParams ){
    return ResponseEntity.ok(vehicleListingService.read(allParams));
  }
}

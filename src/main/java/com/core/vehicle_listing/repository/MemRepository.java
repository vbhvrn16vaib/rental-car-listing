package com.core.vehicle_listing.repository;

import com.core.vehicle_listing.model.VehicleDetails;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class MemRepository {
  private Map<String, Map<String, VehicleDetails>> vendorToListings = new HashMap<>();

  public boolean save(String dealerId, List<VehicleDetails> vehicleDetails){
    return vehicleDetails
        .stream().allMatch(x -> save(dealerId, x));
  }

  public boolean save(String dealerId, VehicleDetails vehicleDetails){
    try{
      vendorToListings.computeIfAbsent(dealerId, (k) -> new HashMap<>())
          .put(vehicleDetails.getCode(),vehicleDetails);
      return true;
    }catch (Exception e){
      log.error("Failed to save to database ");
    }
    return false;
  }

  public List<VehicleDetails> getById(String dealerId){
    return new ArrayList<>(vendorToListings.getOrDefault(dealerId, Map.of()).values());
  }

  public List<VehicleDetails> getAll(){
    return vendorToListings.values()
        .stream()
        .filter(Objects::nonNull)
        .flatMap(x -> x.values().stream())
        .collect(Collectors.toList());
  }
}

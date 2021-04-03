package com.core.vehicle_listing.service;

import com.core.vehicle_listing.filter.FilterService;
import com.core.vehicle_listing.mapper.FileToObjectFactory;
import com.core.vehicle_listing.model.ListingResponse;
import com.core.vehicle_listing.model.VehicleDetails;
import com.core.vehicle_listing.repository.MemRepository;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class VehicleListingService {

  private final FileToObjectFactory fileToObjectService;
  private final FilterService filterService;
  private final MemRepository repository;

  public VehicleListingService(FileToObjectFactory fileToObjectService,
      FilterService filterService,
      MemRepository repository) {
    this.fileToObjectService = fileToObjectService;
    this.filterService = filterService;
    this.repository = repository;
  }

  public boolean saveFromFile(MultipartFile request,
      String type,
      String dealerId) {
    List<VehicleDetails> vehicleDetails = fileToObjectService.getVehicleDetails(type, request);
    return repository.save(dealerId, vehicleDetails);
  }

  public boolean save(List<VehicleDetails> request,
      String dealerId) {
    return repository.save(dealerId, request);
  }

  public ListingResponse read(Map<String, String> filters) {
    List<VehicleDetails> allResponse = repository.getAll();
    List<VehicleDetails> filteredResponse = allResponse
        .stream()
        .filter(d -> Objects.isNull(filters) || filters.isEmpty() || checkFilter(filters).test(d))
        .collect(Collectors.toList());
    return ListingResponse.builder().response(filteredResponse).build();
  }

  public ListingResponse getAllListingOfDealer(String id){
    return ListingResponse.builder().response(repository.getById(id)).build();
  }

  private Predicate<VehicleDetails> checkFilter(Map<String, String> allParams) {
    return filterService.getFilters(allParams);
  }
}

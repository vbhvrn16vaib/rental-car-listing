package com.core.vehicle_listing.mapper;

import com.core.vehicle_listing.model.VehicleDetails;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileToObjectFactory {

  private final Map<String, FileToObjectService> typeToServiceMap;

  public FileToObjectFactory(
      Map<String, FileToObjectService> typeToServiceMap) {
    this.typeToServiceMap = typeToServiceMap;
  }

  public List<VehicleDetails> getVehicleDetails(String type, MultipartFile file) {
    if(!typeToServiceMap.containsKey(type)){
      throw new RuntimeException("This file type is not supported !!");
    }
    FileToObjectService service = typeToServiceMap.get(type);
    return service.map(file);
  }

}

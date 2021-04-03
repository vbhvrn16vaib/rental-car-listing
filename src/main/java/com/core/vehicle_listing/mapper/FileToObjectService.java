package com.core.vehicle_listing.mapper;

import com.core.vehicle_listing.model.VehicleDetails;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface FileToObjectService {
  List<VehicleDetails> map(MultipartFile file);
}

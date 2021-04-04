package com.core.vehicle_listing.mapper;

import com.core.vehicle_listing.model.VehicleDetails;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Component("csv")
public class CSVFileToVehicleDetails implements FileToObjectService {

  private static final Map<String, Integer> FIELD_TO_COLOUMN_MAP = new HashMap<>();

  static {
    FIELD_TO_COLOUMN_MAP.put("code", 0);
    FIELD_TO_COLOUMN_MAP.put("make_model", 1);
    FIELD_TO_COLOUMN_MAP.put("power", 2);
    FIELD_TO_COLOUMN_MAP.put("year", 3);
    FIELD_TO_COLOUMN_MAP.put("color", 4);
    FIELD_TO_COLOUMN_MAP.put("price", 5);
  }

  @Override
  public List<VehicleDetails> map(MultipartFile file) {
    try {
      String[] splitWithLine = new String(file.getBytes()).split("\n");
      return Arrays.stream(splitWithLine)
          .skip(1)
          .map(x -> {
            String[] split = x.split(",");
            String[] make_models = split[FIELD_TO_COLOUMN_MAP.get("make_model")].split("/");
            return VehicleDetails.builder()
                .code(split[FIELD_TO_COLOUMN_MAP.get("code")])
                .make(make_models[0])
                .model(make_models[1])
                .kW(Long.valueOf(split[FIELD_TO_COLOUMN_MAP.get("power")]))
                .color(split[FIELD_TO_COLOUMN_MAP.get("color")])
                .price(Double.valueOf(split[FIELD_TO_COLOUMN_MAP.get("price")]))
                .year(Long.valueOf(split[FIELD_TO_COLOUMN_MAP.get("year")]))
                .build();
          }).collect(Collectors.toList());
    } catch (IOException e) {
      log.error("Failed to load from csv file !!");
    }
    return List.of();
  }
}

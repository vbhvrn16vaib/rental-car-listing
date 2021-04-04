package com.core.vehicle_listing.mapper;

import static org.junit.jupiter.api.Assertions.*;

import com.core.vehicle_listing.model.VehicleDetails;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

class CSVFileToVehicleDetailsTest {

  private CSVFileToVehicleDetails csvFileToVehicleDetails = new CSVFileToVehicleDetails();

  @Test
  void shouldNotFailForEmptyMultipartFile(){
    MockMultipartFile file = new MockMultipartFile("listing.csv", "".getBytes(StandardCharsets.UTF_8));
    List<VehicleDetails> map = csvFileToVehicleDetails.map(file);
    Assertions.assertEquals(map, List.of());
  }

  @Test
  void shouldParseMultipartFile(){
    MockMultipartFile file = new MockMultipartFile("listing.csv", readFile("listing.csv"));
    List<VehicleDetails> map = csvFileToVehicleDetails.map(file);
    Assertions.assertNotNull(map);
    Assertions.assertFalse(map.isEmpty());
    Assertions.assertEquals(4, map.size());
  }

  private byte[] readFile(String fileName) {
    try {
      return Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(fileName)).readAllBytes();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

}
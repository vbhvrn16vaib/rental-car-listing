package com.core.vehicle_listing.filter;

import static org.junit.jupiter.api.Assertions.*;

import com.core.vehicle_listing.model.VehicleDetails;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class FilterServiceTest {

  private FilterService filterService = new FilterService();
  private static List<VehicleDetails> vehicleDetails = new ArrayList<>();

  @BeforeAll
  static void setUp() {
    vehicleDetails.add(mockVehicleDetails("a", "audi", 2014l, "blue", "audi13"));
    vehicleDetails.add(mockVehicleDetails("a", "bmw", 2018l, "green", "bmw12"));
    vehicleDetails.add(mockVehicleDetails("a", "bmw", 2020l, "red", "bmw15"));
  }

  @Test
  void shouldBeAbleToFilterWithMake() {
    Map<String, String> allParams = Map.of("make", "audi");
    Predicate<VehicleDetails> filters = filterService.getFilters(allParams);
    vehicleDetails.stream().filter(filters)
        .forEach(d -> Assertions.assertSame("audi", d.getMake()));
  }

  @Test
  void shouldBeAbleToFilterWithColor() {
    Map<String, String> allParams = Map.of("color", "green");
    Predicate<VehicleDetails> filters = filterService.getFilters(allParams);
    vehicleDetails.stream().filter(filters)
        .forEach(d -> Assertions.assertSame("green", d.getColor()));
  }

  @Test
  void shouldBeAbleToFilterWithYear() {
    Map<String, String> allParams = Map.of("year", "2018");
    Predicate<VehicleDetails> filters = filterService.getFilters(allParams);
    vehicleDetails.stream().filter(filters)
        .forEach(d -> assertEquals(2018L, (long) d.getYear()));
  }

  @Test
  void shouldBeAbleToFilterWithModel() {
    Map<String, String> allParams = Map.of("model", "bmw12");
    Predicate<VehicleDetails> filters = filterService.getFilters(allParams);
    vehicleDetails.stream().filter(filters)
        .forEach(d -> Assertions.assertSame("bmw12", d.getModel()));
  }

  @Test
  void shouldBeAbleToFilterWithYearToAndFrom() {
    Map<String, String> allParams = Map.of("to", "2018", "from", "2014");
    Predicate<VehicleDetails> filters = filterService.getFilters(allParams);
    vehicleDetails.stream().filter(filters)
        .forEach(d -> Assertions.assertTrue(d.getYear() <= 2018 && d.getYear() >= 2014));
  }

  private static VehicleDetails mockVehicleDetails(String code, String make, long year,
      String color,
      String model) {
    return VehicleDetails.builder().code(code).make(make).year(year).color(color).model(model)
        .build();
  }

}
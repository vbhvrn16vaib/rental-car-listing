package com.core.vehicle_listing.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleDetails {
  private String code;
  private String make;
  private String model;
  @JsonProperty("kW")
  private Long kW;
  private Long year;
  private String color;
  private Double price;
}

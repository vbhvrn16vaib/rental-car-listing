package com.core.vehicle_listing.IT;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.core.vehicle_listing.controller.VehicleListingController;
import com.core.vehicle_listing.filter.FilterService;
import com.core.vehicle_listing.mapper.CSVFileToVehicleDetails;
import com.core.vehicle_listing.mapper.FileToObjectFactory;
import com.core.vehicle_listing.mapper.FileToObjectService;
import com.core.vehicle_listing.repository.MemRepository;
import com.core.vehicle_listing.service.VehicleListingService;
import java.io.IOException;
import java.util.Objects;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {VehicleListingController.class, VehicleListingService.class,
    CSVFileToVehicleDetails.class,
    FileToObjectFactory.class,
    FileToObjectService.class, FilterService.class, MemRepository.class})
@WebMvcTest
class VehicleListingIT {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void shouldBeAbleToInsertDataThroughCsv() throws Exception {
    String fileName = "listing.csv";
    MockMultipartFile file
        = new MockMultipartFile(
        "file",
        fileName,
        MediaType.TEXT_PLAIN_VALUE,
        readFile(fileName)
    );

    mockMvc
        .perform(MockMvcRequestBuilders.multipart("/upload/csv/fx").file(file))
        .andExpect(d -> {
          Assertions.assertNotNull(d.getResponse());
          Assertions.assertTrue(Boolean.parseBoolean(d.getResponse().getContentAsString()));
        });

    mockMvc
        .perform(MockMvcRequestBuilders.get("/vendor_listings/fx"))
        .andExpect(status().isOk())
        .andExpect(d -> {
          Assertions.assertNotNull(d.getResponse());
          content().string("{\"response\":[{\"code\":\"1\",\"make\":\"mercedes\",\"model\":\"a 180\",\"year\":2014,\"color\":\"black\",\"price\":15950.0,\"kW\":123},{\"code\":\"2\",\"make\":\"audi\",\"model\":\"a3\",\"year\":2016,\"color\":\"white\",\"price\":17210.0,\"kW\":111},{\"code\":\"3\",\"make\":\"vw\",\"model\":\"golf\",\"year\":2018,\"color\":\"green\",\"price\":14980.0,\"kW\":86},{\"code\":\"4\",\"make\":\"skoda\",\"model\":\"octavia\",\"year\":2018,\"color\":\"blue\",\"price\":16990.0,\"kW\":86}]}")
          .match(d);
        });
  }

  @Test
  void shouldBeAbleToInsertDataThroughJson() throws Exception {
    mockMvc
        .perform(MockMvcRequestBuilders.post("/vendor_listings/dx")
        .content("[\n"
            + "    {\n"
            + "        \"code\": \"acb\",\n"
            + "        \"make\": \"renault\",\n"
            + "        \"model\": \"megane\",\n"
            + "        \"kW\": 134,\n"
            + "        \"year\": 2014,\n"
            + "        \"color\": \"green\",\n"
            + "        \"price\": 13990\n"
            + "    }\n"
            + "]")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().string("true"));

    mockMvc
        .perform(MockMvcRequestBuilders.get("/vendor_listings/dx"))
        .andExpect(status().isOk())
        .andExpect(d -> {
          Assertions.assertNotNull(d.getResponse());
         MockMvcResultMatchers.content().json("{\"response\":[{\"code\":\"acb\",\"make\":\"renault\",\"model\":\"megane\",\"year\":2014,\"color\":\"green\",\"price\":13990.0,\"kW\":134}]}")
             .match(d);
        });
  }

  @Test
  void shouldBeAbleToSearchWithQueryParams() throws Exception {
    mockMvc
        .perform(MockMvcRequestBuilders.post("/vendor_listings/ddx")
            .content("[\n"
                + "    {\n"
                + "        \"code\": \"acb\",\n"
                + "        \"make\": \"renault\",\n"
                + "        \"model\": \"megane\",\n"
                + "        \"kW\": 134,\n"
                + "        \"year\": 2014,\n"
                + "        \"color\": \"green\",\n"
                + "        \"price\": 13990\n"
                + "    },\n"
                + "    {\n"
                + "        \"code\": \"acb2\",\n"
                + "        \"make\": \"kia\",\n"
                + "        \"model\": \"kia\",\n"
                + "        \"kW\": 134,\n"
                + "        \"year\": 2018,\n"
                + "        \"color\": \"blue\",\n"
                + "        \"price\": 13990\n"
                + "    }\n"
                + "]")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().string("true"));

    mockMvc
        .perform(MockMvcRequestBuilders.get("/search?color=green"))
        .andExpect(status().isOk())
        .andExpect(d -> {
          Assertions.assertNotNull(d.getResponse());
          MockMvcResultMatchers.content().json("{\"response\":[{\"code\":\"acb\",\"make\":\"renault\",\"model\":\"megane\",\"year\":2014,\"color\":\"green\",\"price\":13990.0,\"kW\":134}]}")
              .match(d);
        });
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
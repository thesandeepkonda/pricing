package com.spacezant.pricing.dto.easyship;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
@Data
public class EasyshipDutyRequest {

    @JsonProperty("origin_address")
    private Address originAddress;

    @JsonProperty("destination_address")
    private Address destinationAddress;

    private List<Parcel> parcels;

    @Data
    public static class Address {
        @JsonProperty("country_alpha2")
        private String countryAlpha2;
    }

    @Data
    public static class Parcel {

        @JsonProperty("placeholder")
        private String placeholder = "default";
    }
}
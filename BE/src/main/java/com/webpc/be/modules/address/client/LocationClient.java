package com.webpc.be.modules.address.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.webpc.be.common.config.properties.LocationApiProperties;
import com.webpc.be.modules.address.dto.response.LocationDataResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class LocationClient {

    private final LocationApiProperties properties;

    public List<LocationDataResponse> getProvinces() {
        return getLocationData("/1/0.htm");
    }

    public List<LocationDataResponse> getDistricts(String provinceId) {
        return getLocationData("/2/" + provinceId + ".htm");
    }

    public List<LocationDataResponse> getWards(String districtId) {
        return getLocationData("/3/" + districtId + ".htm");
    }

    public AddressNames getAddressNames(String provinceId, String districtId, String wardId) {
        LocationDataResponse province = getProvinces().stream()
            .filter(item -> item.id().equals(provinceId))
            .findFirst()
            .orElse(null);
        if (province == null) {
            return new AddressNames(null, null, null);
        }

        LocationDataResponse district = getDistricts(provinceId).stream()
            .filter(item -> item.id().equals(districtId))
            .findFirst()
            .orElse(null);
        if (district == null) {
            return new AddressNames(province.name(), null, null);
        }

        LocationDataResponse ward = getWards(districtId).stream()
            .filter(item -> item.id().equals(wardId))
            .findFirst()
            .orElse(null);
        if (ward == null) {
            return new AddressNames(province.name(), district.name(), null);
        }

        return new AddressNames(province.name(), district.name(), ward.name());
    }

    private List<LocationDataResponse> getLocationData(String path) {
        RestClient client = RestClient.builder()
            .baseUrl(properties.getBaseUrl())
            .build();
        LocationApiResponse response = client.get()
            .uri(path)
            .retrieve()
            .body(LocationApiResponse.class);

        if (response == null || response.error() != 0 || response.data() == null) {
            return List.of();
        }
        return response.data();
    }

    private record LocationApiResponse(
        int error,
        @JsonProperty("error_text") String errorText,
        List<LocationDataResponse> data
    ) {
    }

    public record AddressNames(String tinh, String huyen, String xa) {
    }
}

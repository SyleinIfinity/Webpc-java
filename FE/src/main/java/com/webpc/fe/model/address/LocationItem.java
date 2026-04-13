package com.webpc.fe.model.address;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class LocationItem {

    @JsonProperty("Id")
    @JsonAlias({"id", "Id"})
    private String id;

    @JsonProperty("Name")
    @JsonAlias({"name", "Name"})
    private String name;
}

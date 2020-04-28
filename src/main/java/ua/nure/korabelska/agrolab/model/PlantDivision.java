package ua.nure.korabelska.agrolab.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

@JsonFormat
public enum  PlantDivision {
    MAGNOLIOPHYTA("MAGNOLIOPHYTA"),
    PINOPHYTA("PINOPHYTA"),
    CYCADOPHYTA("CYCADOPHYTA"),
    POLYPODIOPHYTA("POLYPODIOPHYTA");

    private String val;

    PlantDivision(String val) {
        this.val = val;
    }

    @JsonValue
    public String getValue() {
        return val;
    }

    @JsonCreator
    public static PlantDivision fromValue(String value) {
        return PlantDivision.valueOf(value);
    }

}

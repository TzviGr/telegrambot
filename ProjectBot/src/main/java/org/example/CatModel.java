package org.example;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CatModel {
    public String getFact() {
        return fact;
    }

    private String fact;
}

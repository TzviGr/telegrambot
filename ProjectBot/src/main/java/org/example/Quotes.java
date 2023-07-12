package org.example;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Quotes {
    private String author;
    private String content;

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

}

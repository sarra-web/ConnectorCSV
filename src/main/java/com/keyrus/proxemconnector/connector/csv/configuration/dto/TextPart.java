package com.keyrus.proxemconnector.connector.csv.configuration.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class TextPart {
    @JsonProperty("Name")
    private String Name;
    @JsonProperty("Content")
    private String Content;

    public TextPart(String name, String content) {
        Name = name;
        Content = content;
    }

    public String getName() {
        return Name;
    }

    public String getContent() {
        return Content;
    }

    @Override
    public String toString() {
        return "mapTextPart{" +
                "Name='" + Name + '\'' +
                ", Content='" + Content + '\'' +
                '}';
    }
}

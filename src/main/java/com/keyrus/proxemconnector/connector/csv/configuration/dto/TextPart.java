package com.keyrus.proxemconnector.connector.csv.configuration.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TextPart {
    @JsonProperty("Name")
    private String name;
    @JsonProperty("Content")
    private String content;



    @Override
    public String toString() {
        return "{" +
                "\"Name\":" +"\""+ name+"\""  +
                ", \"Content\":"+"\"" + content + "\"" +
                "}";
    }
}

package com.keyrus.proxemconnector.connector.csv.configuration.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Meta<T> {
    @JsonProperty("Name")
    private String Name;
    @JsonProperty("Value")
    private T Value;

    public Meta(String name, T value) {
        Name = name;
        Value = value;
    }


    @Override
    public String toString() {
        return "{" +
                "\"Name\":"+"\"" + Name + "\""  +
                ", \"Value\":" +"\""+ Value +"\""+
                "}";
    }
}

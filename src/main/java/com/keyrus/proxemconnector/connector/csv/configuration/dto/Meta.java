package com.keyrus.proxemconnector.connector.csv.configuration.dto;


import com.fasterxml.jackson.annotation.JsonProperty;


public class Meta<T> {
    @JsonProperty("Name")
    private String Name;
    @JsonProperty("Value")
    private T Value;

    public Meta(String name, T value) {
        Name = name;
        Value = value;
    }
    public Meta() {

    }


    @Override
    public String toString() {
        return "mapMeta{" +
                "Name='" + Name + '\'' +
                ", Value=" + Value +
                '}';
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public T getValue() {
        return Value;
    }

    public void setValue(T value) {
        Value = value;
    }

}

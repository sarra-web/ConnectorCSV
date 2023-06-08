package com.keyrus.proxemconnector.connector.csv.configuration.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProxemDto {
    @JsonProperty("CorpusId")
    private String CorpusId;
    @JsonProperty("ExternalId")
    private String ExternalId;
    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-ddTHH:mm:ssZ",
    //        timezone = "Africa/Tunis")
    @JsonProperty("DocUtcDate")
    private String DocUtcDate;
    @JsonProperty("Metas")
    private Collection<Meta> Metas;
    //  @JsonDeserialize(using = TextPartsDeserializer.class)
    @JsonProperty("TextParts")
    private Collection<TextPart> TextParts;
    @Override
    public String toString() {
        return "{" +
                "\"CorpusId\":"+"\"" + CorpusId +"\"" +
                ", \"ExternalId\":" +"\""+ ExternalId + "\""+
                ", \"DocUtcDate\":" +"\""+ DocUtcDate + "\"" +
                ", \"Metas\":" +Metas+
                ", \"TextParts\":" + TextParts +
                '}'+"\n";
    }
}
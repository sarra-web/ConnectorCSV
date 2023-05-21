package com.keyrus.proxemconnector.connector.csv.configuration.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collection;


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
                "\"corpusId\":"+"\"" + CorpusId +"\"" +
                ", \"externalId\":" +"\""+ ExternalId + "\""+
                ", \"docUtcDate\":" +"\""+ DocUtcDate + "\"" +
                ", \"metas\":" +Metas+
                ", \"textParts\":" + TextParts +
                '}'+"\n";
    }

    public String getCorpusId() {
        return CorpusId;
    }

    public void setCorpusId(String corpusId) {
        CorpusId = corpusId;
    }

    public String getExternalId() {
        return ExternalId;
    }

    public void setExternalId(String externalId) {
        ExternalId = externalId;
    }

    public String getDocUtcDate() {
        return DocUtcDate;
    }

    public void setDocUtcDate(String docUtcDate) {
        DocUtcDate = docUtcDate;
    }

    public Collection<Meta> getMetas() {
        return Metas;
    }

    public void setMetas(Collection<Meta> metas) {
        Metas = metas;
    }

    public Collection<TextPart> getTextParts() {
        return TextParts;
    }

    public void setTextParts(Collection<TextPart> textParts) {
        TextParts = textParts;
    }
}
/*
 class TextPartsDeserializer extends JsonDeserializer<List<mapTextPart>> {
    @Override
    public List<mapTextPart> deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        ObjectMapper mapper = (ObjectMapper) jp.getCodec();
        JsonNode node = mapper.readTree(jp);
        List<mapTextPart> textParts = new ArrayList<>();
        if (node.isObject()) {
            // Handle object-based format
            mapTextPart textPart = mapper.treeToValue(node, mapTextPart.class);
            textParts.add(textPart);
        } else if (node.isArray()) {
            // Handle array-based format
            textParts = mapper.readValue(node.traverse(), new TypeReference<List<mapTextPart>>() {});
        }
        return textParts;
    }
}*/

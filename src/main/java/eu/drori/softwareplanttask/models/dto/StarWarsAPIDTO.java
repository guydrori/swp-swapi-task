package eu.drori.softwareplanttask.models.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import eu.drori.softwareplanttask.util.StarWarsAPIDTODeserializer;

@JsonDeserialize(using = StarWarsAPIDTODeserializer.class)
public abstract class StarWarsAPIDTO {
}

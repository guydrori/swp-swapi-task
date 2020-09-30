package eu.drori.softwareplanttask.models.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotEmpty;

public class ReportCreateOrUpdateDTO {
    @NotEmpty(message = "Character phrase must be provided!")
    @JsonProperty("query_criteria_character_phrase")
    private String characterPhrase;
    @NotEmpty(message = "Planet name must be provided!")
    @JsonProperty("query_criteria_planet_name")
    private String planetName;

    public String getCharacterPhrase() {
        return characterPhrase;
    }

    public void setCharacterPhrase(String characterPhrase) {
        this.characterPhrase = characterPhrase;
    }

    public String getPlanetName() {
        return planetName;
    }

    public void setPlanetName(String planetName) {
        this.planetName = planetName;
    }
}

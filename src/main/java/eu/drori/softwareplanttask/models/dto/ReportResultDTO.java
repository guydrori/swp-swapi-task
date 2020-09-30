package eu.drori.softwareplanttask.models.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import eu.drori.softwareplanttask.models.ReportResult;
import org.apache.logging.log4j.util.Strings;

public class ReportResultDTO {

    @JsonProperty("film_id")
    private Long filmId;

    @JsonProperty("film_name")
    private String filmName;

    @JsonProperty("character_id")
    private Long characterId;

    @JsonProperty("character_name")
    private String characterName;

    @JsonProperty("planet_id")
    private Long planetId;

    @JsonProperty("planet_name")
    private String planetName;

    public ReportResultDTO() {
    }

    public ReportResultDTO(ReportResult reportResult) {
        this.setFilmId(reportResult.getFilmId());
        this.setFilmName(reportResult.getFilmName());
        this.setCharacterId(reportResult.getCharacterId());
        this.setCharacterName(reportResult.getCharacterName());
        this.setPlanetId(reportResult.getPlanetId());
        this.setPlanetName(reportResult.getPlanetName());
    }

    public Long getFilmId() {
        return filmId;
    }

    public void setFilmId(Long filmId) {
        if (filmId == null || filmId < 0) {
            throw new IllegalArgumentException("Film ID required");
        }
        this.filmId = filmId;
    }

    public String getFilmName() {
        return filmName;
    }

    public void setFilmName(String filmName) {
        if (Strings.isBlank(filmName)) {
            throw new IllegalArgumentException("Film name required");
        }
        this.filmName = filmName;
    }

    public Long getCharacterId() {
        return characterId;
    }

    public void setCharacterId(Long characterId) {
        if (characterId == null || characterId < 0) {
            throw new IllegalArgumentException("Character ID required");
        }
        this.characterId = characterId;
    }

    public String getCharacterName() {
        return characterName;
    }

    public void setCharacterName(String characterName) {
        if (Strings.isBlank(characterName)) {
            throw new IllegalArgumentException("Character name required");
        }
        this.characterName = characterName;
    }

    public Long getPlanetId() {
        return planetId;
    }

    public void setPlanetId(Long planetId) {
        if (planetId == null || planetId < 0) {
            throw new IllegalArgumentException("Planet ID required");
        }
        this.planetId = planetId;
    }

    public String getPlanetName() {
        return planetName;
    }

    public void setPlanetName(String planetName) {
        if (Strings.isBlank(planetName)) {
            throw new IllegalArgumentException("Planet name required");
        }
        this.planetName = planetName;
    }
}

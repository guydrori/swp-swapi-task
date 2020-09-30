package eu.drori.softwareplanttask.models;

import org.apache.logging.log4j.util.Strings;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
public class ReportResult {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(optional = false)
    private Report report;

    @Column(nullable = false)
    private Long filmId;

    @Column(nullable = false)
    @NotEmpty
    private String filmName;

    @Column(nullable = false)
    private Long characterId;

    @Column(nullable = false)
    private String characterName;

    @Column(nullable = false)
    private Long planetId;

    @Column(nullable = false)
    private String planetName;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
    }

    public Long getFilmId() {
        return filmId;
    }

    public void setFilmId(Long filmId) {
        if (filmId == null || filmId <= 0) {
            throw new IllegalArgumentException("Invalid film ID");
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
        if (characterId == null || characterId <= 0) {
            throw new IllegalArgumentException("Invalid character ID");
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
        if (planetId == null || planetId <= 0) {
            throw new IllegalArgumentException("Invalid planet ID");
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

package eu.drori.softwareplanttask.models;

import org.apache.logging.log4j.util.Strings;

import javax.persistence.*;
import java.util.List;

@Entity
public class Report {
    @Id
    private String id;

    @Column(nullable = false)
    private String characterPhrase;

    @Column(nullable = false)
    private String planetName;

    @OneToMany(mappedBy = "report",cascade = {CascadeType.ALL})
    private List<ReportResult> results;

    public Report() {
    }

    public Report(String id) {
        this.setId(id);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        if (Strings.isBlank(id)) {
            throw new IllegalArgumentException("Report ID is required!");
        }
        this.id = id;
    }

    public String getCharacterPhrase() {
        return characterPhrase;
    }

    public void setCharacterPhrase(String characterPhrase) {
        if (Strings.isBlank(characterPhrase)) {
            throw new IllegalArgumentException("Character phrase must be provided!");
        }
        this.characterPhrase = characterPhrase;
    }

    public String getPlanetName() {
        return planetName;
    }

    public void setPlanetName(String planetName) {
        if (Strings.isBlank(planetName)) {
            throw new IllegalArgumentException("Planet name must be provided!");
        }
        this.planetName = planetName;
    }

    public List<ReportResult> getResults() {
        return results;
    }

    public void setResults(List<ReportResult> results) {
        this.results = results;
    }
}

package eu.drori.softwareplanttask.models.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import eu.drori.softwareplanttask.models.Report;
import org.apache.logging.log4j.util.Strings;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class ReportDTO {
    @JsonProperty("report_id")
    public String reportId;
    @JsonProperty("query_criteria_character_phrase")
    public String characterPhrase;
    @JsonProperty("query_criteria_planet_name")
    public String planetName;
    public List<ReportResultDTO> result;

    public ReportDTO() {

    }

    public ReportDTO(Report report) {
        this.setReportId(report.getId());
        this.setCharacterPhrase(report.getCharacterPhrase());
        this.setPlanetName(report.getPlanetName());
        this.result = report.getResults().stream().map(ReportResultDTO::new).collect(Collectors.toList());
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        if (Strings.isBlank(reportId)) {
            throw new IllegalArgumentException("ID required");
        }
        this.reportId = reportId;
    }

    public String getCharacterPhrase() {
        return characterPhrase;
    }

    public void setCharacterPhrase(String characterPhrase) {
        if (Strings.isBlank(characterPhrase)) {
            throw new IllegalArgumentException("Character phrase required");
        }
        this.characterPhrase = characterPhrase;
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

    public List<ReportResultDTO> getResult() {
        return result;
    }

    public void setResult(List<ReportResultDTO> result) {
        this.result = result;
    }
}

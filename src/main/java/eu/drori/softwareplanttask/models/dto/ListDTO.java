package eu.drori.softwareplanttask.models.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ListDTO {
    private Long count;
    private String next;
    private List<StarWarsAPIDTO> results;

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public List<StarWarsAPIDTO> getResults() {
        return results;
    }

    public void setResults(List<StarWarsAPIDTO> results) {
        this.results = results;
    }
}

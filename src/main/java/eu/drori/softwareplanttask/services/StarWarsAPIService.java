package eu.drori.softwareplanttask.services;

import eu.drori.softwareplanttask.models.Report;
import eu.drori.softwareplanttask.models.ReportResult;
import eu.drori.softwareplanttask.models.dto.FilmDTO;
import eu.drori.softwareplanttask.models.dto.ListDTO;
import eu.drori.softwareplanttask.models.dto.PersonDTO;
import eu.drori.softwareplanttask.models.dto.PlanetDTO;
import eu.drori.softwareplanttask.repositories.ReportRepository;
import eu.drori.softwareplanttask.repositories.ReportResultRepository;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class StarWarsAPIService {
    private final RestTemplate restTemplate;
    private final ReportRepository reportRepository;
    private final ReportResultRepository reportResultRepository;
    private final Logger log;
    private final String starWarsApiURL;
    private static final Pattern ID_PATTERN = Pattern.compile("^https?://.+/api/.+/(?<id>\\d+)/?$");

    @Autowired
    public StarWarsAPIService(RestTemplateBuilder restTemplateBuilder, @Value("${app.swapi.url}") String starWarsApiURL, ReportRepository reportRepository, ReportResultRepository reportResultRepository) {
        this.restTemplate = restTemplateBuilder.build();
        if (starWarsApiURL.endsWith("/")) {
            this.starWarsApiURL = starWarsApiURL;
        } else {
            this.starWarsApiURL = starWarsApiURL + "/";
        }
        this.reportRepository = reportRepository;
        this.reportResultRepository = reportResultRepository;
        this.log = LoggerFactory.getLogger(StarWarsAPIService.class);
    }

    public FilmDTO getFilm(long id) {
        return this.restTemplate.getForObject(starWarsApiURL + "api/films/{id}",FilmDTO.class, Map.of("id",id));
    }

    public ListDTO getPlanets(String searchPhrase) {
        if (Strings.isBlank(searchPhrase)) {
            return this.restTemplate.getForObject(starWarsApiURL+ "api/planets/",ListDTO.class);
        } else {
            return this.restTemplate.getForObject(starWarsApiURL+ "api/planets/?search={search_phrase}",ListDTO.class,Map.of("search_phrase",searchPhrase));
        }
    }

    public ListDTO getCharacters(String searchPhrase) {
        if (Strings.isBlank(searchPhrase)) {
            return this.restTemplate.getForObject(starWarsApiURL+ "api/people/",ListDTO.class);
        } else {
            return this.restTemplate.getForObject(starWarsApiURL+ "api/people/?search={search_phrase}",ListDTO.class,Map.of("search_phrase",searchPhrase));
        }
    }

    public ListDTO getListByURL(String url) {
        if (Strings.isBlank(url)) {
            throw new IllegalArgumentException("URL must be provided");
        }
        return this.restTemplate.getForObject(url,ListDTO.class);
    }

    public static long getIDFromURL(String url) {
        if (Strings.isBlank(url)) {
            throw new IllegalArgumentException("URL must be provided");
        }
        Matcher matcher = ID_PATTERN.matcher(url);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid URL");
        }
        return Long.parseLong(matcher.group("id"));
    }

    @Async
    @Transactional(rollbackFor = Exception.class)
    public void populateReportResults(String reportId) {
        Optional<Report> reportQueryResult = reportRepository.findById(reportId);
        if (reportQueryResult.isEmpty()) {
            log.error("Report does not exist");
            return;
        }
        Report report = reportQueryResult.get();
        ListDTO planetSearch = getPlanets(report.getPlanetName());
        if (planetSearch.getCount() == 0) {
            log.error("No planet found!");
            return;
        }
        List<PlanetDTO> planets = getAllPlanets(planetSearch);
        Optional<PlanetDTO> planetNameSearchResult =  planets.stream().filter(planetDTO->planetDTO.getName().toLowerCase().equals(report.getPlanetName().toLowerCase())).findFirst();
        if (planetNameSearchResult.isEmpty()) {
            log.error("No planet found!");
            return;
        }
        PlanetDTO planetDTO = planetNameSearchResult.get();
        ListDTO characterSearch = getCharacters(report.getCharacterPhrase());
        if (characterSearch.getCount() == 0) {
            log.error("No characters found!");
            return;
        }
        List<PersonDTO> peopleInPlanet = getAllPeopleInPlanet(characterSearch,planetDTO);
        if (peopleInPlanet.size() == 0) {
            log.error("No characters found!");
            return;
        }
        reportResultRepository.deleteAllByReport(report);
        List<String> filmUrls = peopleInPlanet.stream().flatMap(personDTO -> personDTO.getFilms().stream()).collect(Collectors.toList());
        Map<Long,String> filmIdTitleMap = getFilmIdTitleMap(filmUrls);
        List<ReportResult> reportResultList = getReportResults(peopleInPlanet,filmIdTitleMap,
                getIDFromURL(planetDTO.getUrl()),planetDTO.getName(),report);
        reportResultRepository.saveAll(reportResultList);
    }

    private List<ReportResult> getReportResults(List<PersonDTO> peopleInPlanet, Map<Long,String> filmMap, Long planetId, String planetName, Report report) {
        List<ReportResult> reportResultList = new LinkedList<>();
        for (PersonDTO person: peopleInPlanet) {
            Long personId = getIDFromURL(person.getUrl());
            String personName = person.getName();
            person.getFilms().forEach(filmUrl-> {
                Long filmId = getIDFromURL(filmUrl);
                String title = filmMap.get(filmId);
                ReportResult reportResult = new ReportResult();
                reportResult.setCharacterId(personId);
                reportResult.setCharacterName(personName);
                reportResult.setFilmId(filmId);
                reportResult.setFilmName(title);
                reportResult.setPlanetId(planetId);
                reportResult.setPlanetName(planetName);
                reportResult.setReport(report);
                reportResultList.add(reportResult);
            });
        }
        return reportResultList;
    }

    private List<PlanetDTO> getAllPlanets(ListDTO planetSearch) {
        List<PlanetDTO> planets = new LinkedList<>(planetSearch.getResults().stream().map(starWarsAPIDTO -> (PlanetDTO) starWarsAPIDTO).collect(Collectors.toList()));
        while (Strings.isNotBlank(planetSearch.getNext())) {
            planetSearch = getListByURL(planetSearch.getNext());
            planets.addAll(planetSearch.getResults().stream().map(starWarsAPIDTO -> (PlanetDTO) starWarsAPIDTO).collect(Collectors.toList()));
        }
        return planets;
    }

    private List<PersonDTO> getAllPeopleInPlanet(ListDTO personSearch, PlanetDTO planetDTO) {
        List<PersonDTO> people = new LinkedList<>(personSearch.getResults().stream().map(starWarsAPIDTO -> (PersonDTO) starWarsAPIDTO).collect(Collectors.toList()));
        while (Strings.isNotBlank(personSearch.getNext())) {
            personSearch = getListByURL(personSearch.getNext());
            people.addAll(personSearch.getResults().stream().map(starWarsAPIDTO -> (PersonDTO) starWarsAPIDTO).collect(Collectors.toList()));
        }
        return people.stream().filter(personDTO -> personDTO.getHomeWorld().equals(planetDTO.getUrl())).collect(Collectors.toList());
    }

    private Map<Long,String> getFilmIdTitleMap(List<String> filmUrls) {
        Map<Long,String> filmIdTitleMap = new TreeMap<>();
        for (String filmUrl: filmUrls) {
            long id = getIDFromURL(filmUrl);
            FilmDTO filmDTO = getFilm(id);
            filmIdTitleMap.put(id,filmDTO.getTitle());
        }
        return filmIdTitleMap;
    }
}

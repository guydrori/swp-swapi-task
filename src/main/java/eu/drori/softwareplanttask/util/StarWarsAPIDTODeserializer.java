package eu.drori.softwareplanttask.util;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import eu.drori.softwareplanttask.models.dto.FilmDTO;
import eu.drori.softwareplanttask.models.dto.PersonDTO;
import eu.drori.softwareplanttask.models.dto.PlanetDTO;
import eu.drori.softwareplanttask.models.dto.StarWarsAPIDTO;
import org.apache.logging.log4j.util.Strings;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class StarWarsAPIDTODeserializer extends JsonDeserializer<StarWarsAPIDTO> {

    @Override
    public StarWarsAPIDTO deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        JsonNode root = jsonParser.readValueAsTree();
        if (!root.has("url")) {
            throw new JsonParseException(jsonParser,"Unsupported Star Wars API type");
        }
        String url = root.get("url").textValue();
        if (Strings.isBlank(url)) {
            throw new JsonParseException(jsonParser,"Unsupported Star Wars API type");
        }
        if (url.contains("film")) {
            return deserializeFilmDTO(root);
        } else if (url.contains("people")) {
            return deserializePersonDTO(root);
        } else if (url.contains("planet")) {
            return deserializePlanetDTO(root);
        } else {
            throw new JsonParseException(jsonParser,"Unsupported Star Wars API type");
        }
    }

    private FilmDTO deserializeFilmDTO(JsonNode root) {
        FilmDTO filmDTO = new FilmDTO();
        filmDTO.setTitle(root.get("title").textValue());
        filmDTO.setUrl(root.get("url").textValue());
        return filmDTO;
    }

    private PersonDTO deserializePersonDTO(JsonNode root) {
        PersonDTO personDTO = new PersonDTO();
        personDTO.setName(root.get("name").textValue());
        personDTO.setHomeWorld(root.get("homeworld").textValue());
        List<String> films = new LinkedList<>();
        JsonNode filmsNode = root.get("films");
        filmsNode.iterator().forEachRemaining(film-> films.add(film.textValue()));
        personDTO.setFilms(films);
        personDTO.setUrl(root.get("url").textValue());
        return personDTO;
    }

    private PlanetDTO deserializePlanetDTO(JsonNode root) {
        PlanetDTO planetDTO = new PlanetDTO();
        planetDTO.setName(root.get("name").textValue());
        planetDTO.setUrl(root.get("url").textValue());
        return planetDTO;
    }

}

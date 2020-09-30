package eu.drori.softwareplanttask.repositories;

import eu.drori.softwareplanttask.models.Report;
import org.springframework.data.repository.CrudRepository;

public interface ReportRepository extends CrudRepository<Report,String> {
}

package eu.drori.softwareplanttask.repositories;

import eu.drori.softwareplanttask.models.Report;
import eu.drori.softwareplanttask.models.ReportResult;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;

public interface ReportResultRepository extends CrudRepository<ReportResult, Long> {
    void deleteAllByReport(@NonNull Report report);
}

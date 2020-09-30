package eu.drori.softwareplanttask.controllers;

import eu.drori.softwareplanttask.models.Report;
import eu.drori.softwareplanttask.models.dto.ReportCreateOrUpdateDTO;
import eu.drori.softwareplanttask.models.dto.ReportDTO;
import eu.drori.softwareplanttask.repositories.ReportRepository;
import eu.drori.softwareplanttask.services.StarWarsAPIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/reports")
public class ReportsController {
    private final ReportRepository reportRepository;
    private final StarWarsAPIService starWarsAPIService;
    private final TransactionTemplate transactionTemplate;

    @Autowired
    public ReportsController(ReportRepository reportRepository, StarWarsAPIService starWarsAPIService, TransactionTemplate transactionTemplate) {
        this.reportRepository = reportRepository;
        this.starWarsAPIService = starWarsAPIService;
        this.transactionTemplate = transactionTemplate;
    }

    @DeleteMapping("/{report_id}")
    @Transactional(rollbackFor = Exception.class)
    @CrossOrigin
    public ResponseEntity<Void> deleteSingle(@PathVariable("report_id") String reportId) {
        Optional<Report> reportSearchResult = reportRepository.findById(reportId);
        if (reportSearchResult.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } else {
            reportRepository.delete(reportSearchResult.get());
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }

    @DeleteMapping("/")
    @Transactional(rollbackFor = Exception.class)
    @CrossOrigin
    public ResponseEntity<Void> deleteAll() {
        reportRepository.deleteAll();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{report_id}")
    @CrossOrigin
    public ResponseEntity<Void> createOrUpdate(@PathVariable("report_id") String reportId, @RequestBody @Validated ReportCreateOrUpdateDTO data) {
        this.transactionTemplate.executeWithoutResult(transactionStatus-> {
            try {
                Optional<Report> reportSearchResult = reportRepository.findById(reportId);
                Report report;
                if (reportSearchResult.isEmpty()) {
                    report = new Report(reportId);
                } else {
                    report = reportSearchResult.get();
                }
                report.setCharacterPhrase(data.getCharacterPhrase());
                report.setPlanetName(data.getPlanetName());
                reportRepository.save(report);
            } catch (Exception e) {
                transactionStatus.setRollbackOnly();
                throw e;
            }
        });
        starWarsAPIService.populateReportResults(reportId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping
    @CrossOrigin
    public Iterable<ReportDTO> getAllReports() {
        return StreamSupport.stream(reportRepository.findAll().spliterator(),false).map(ReportDTO::new).collect(Collectors.toUnmodifiableList());
    }

    @GetMapping("/{report_id}")
    @CrossOrigin
    public ResponseEntity<ReportDTO> getSingleReport(@PathVariable("report_id") String reportId) {
        Optional<Report> reportSearchResult = reportRepository.findById(reportId);
        if (reportSearchResult.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } else {
            return ResponseEntity.ok(new ReportDTO(reportSearchResult.get()));
        }
    }
}

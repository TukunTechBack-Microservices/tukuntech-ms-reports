package com.upc.tukuntechmsreports.reports.application.commands.handlers;

import com.upc.tukuntechmsreports.reports.application.client.MonitoringClient;
import com.upc.tukuntechmsreports.reports.domain.entity.DailyReport;
import com.upc.tukuntechmsreports.reports.domain.repositories.DailyReportRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class GenerateDailyReportCommandHandler {

    private final MonitoringClient monitoringClient;
    private final DailyReportRepository reportRepo;

    public GenerateDailyReportCommandHandler(
            MonitoringClient monitoringClient,
            DailyReportRepository reportRepo
    ) {
        this.monitoringClient = monitoringClient;
        this.reportRepo = reportRepo;
    }

    public DailyReport handle(Long patientId, LocalDate date) {

        var records = monitoringClient.getMeasurementsByPatient(patientId).stream()
                .filter(r -> r.timestamp().toLocalDate().equals(date))
                .toList();

        if (records.isEmpty()) {
            var emptyReport = DailyReport.create(patientId, date, null, null, null, 0);
            return reportRepo.save(emptyReport);
        }

        double avgHR = records.stream().mapToInt(r -> r.heartRate()).average().orElse(0);
        double avgSpO2 = records.stream().mapToInt(r -> r.oxygenLevel()).average().orElse(0);
        double avgTemp = records.stream().mapToDouble(r -> r.temperature()).average().orElse(0);

        int alertCount = (int) monitoringClient.getAlertsByPatient(patientId).stream()
                .filter(a -> a.createdAt().toLocalDate().equals(date))
                .count();

        var report = DailyReport.create(patientId, date, avgHR, avgSpO2, avgTemp, alertCount);
        return reportRepo.save(report);
    }
}

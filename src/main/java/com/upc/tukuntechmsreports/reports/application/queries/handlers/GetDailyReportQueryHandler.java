package com.upc.tukuntechmsreports.reports.application.queries.handlers;

import com.upc.tukuntechmsreports.reports.application.client.MonitoringClient;
import com.upc.tukuntechmsreports.reports.application.dto.DailyReportResponse;
import com.upc.tukuntechmsreports.reports.application.queries.GetDailyReportQuery;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
public class GetDailyReportQueryHandler {

    private final MonitoringClient monitoringClient;

    public GetDailyReportQueryHandler(MonitoringClient monitoringClient) {
        this.monitoringClient = monitoringClient;
    }

    public DailyReportResponse handle(GetDailyReportQuery query) {
        var date = query.date();
        var startOfDay = date.atStartOfDay();
        var endOfDay = date.atTime(LocalTime.MAX);

        var records = monitoringClient.getMeasurementsByPatient(query.patientId()).stream()
                .filter(r -> !r.timestamp().isBefore(startOfDay) && !r.timestamp().isAfter(endOfDay))
                .toList();

        if (records.isEmpty()) {
            return new DailyReportResponse(query.patientId(), date, null, null, null, 0);
        }

        double avgHR = records.stream().mapToInt(r -> r.heartRate()).average().orElse(0);
        double avgSpO2 = records.stream().mapToInt(r -> r.oxygenLevel()).average().orElse(0);
        double avgTemp = records.stream().mapToDouble(r -> r.temperature()).average().orElse(0);

        int alertCount = (int) monitoringClient.getAlertsByPatient(query.patientId()).stream()
                .filter(a -> a.createdAt().toLocalDate().equals(date))
                .count();

        return new DailyReportResponse(query.patientId(), date, avgHR, avgSpO2, avgTemp, alertCount);
    }
}

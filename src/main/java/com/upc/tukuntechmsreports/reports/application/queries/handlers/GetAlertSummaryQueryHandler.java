package com.upc.tukuntechmsreports.reports.application.queries.handlers;

import com.upc.tukuntechmsreports.reports.application.client.MonitoringClient;
import com.upc.tukuntechmsreports.reports.application.dto.AlertSummaryResponse;
import com.upc.tukuntechmsreports.reports.application.queries.GetAlertSummaryQuery;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class GetAlertSummaryQueryHandler {

    private final MonitoringClient monitoringClient;

    public GetAlertSummaryQueryHandler(MonitoringClient monitoringClient) {
        this.monitoringClient = monitoringClient;
    }

    public AlertSummaryResponse handle(GetAlertSummaryQuery query) {

        var alerts = monitoringClient.getAlertsByPatient(query.patientId()).stream()
                .filter(a -> {
                    var date = a.createdAt().toLocalDate();
                    return !date.isBefore(query.startDate()) && !date.isAfter(query.endDate());
                })
                .toList();

        var byType = alerts.stream()
                .collect(Collectors.groupingBy(a -> a.type(), Collectors.counting()));

        var bySeverity = alerts.stream()
                .collect(Collectors.groupingBy(a -> a.severity(), Collectors.counting()));

        return new AlertSummaryResponse(
                query.patientId(),
                query.startDate(),
                query.endDate(),
                byType,
                bySeverity,
                alerts.size()
        );
    }
}

package com.upc.tukuntechmsreports.reports.application.client;

import com.upc.tukuntechmsreports.reports.application.dto.remote.AlertRemoteResponse;
import com.upc.tukuntechmsreports.reports.application.dto.remote.VitalSignRemoteResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Component
public class MonitoringClient {

    private final RestTemplate restTemplate;
    private final String baseUrl;
    private final HttpServletRequest request; // 👈 para leer el Authorization del request original

    public MonitoringClient(
            RestTemplate restTemplate,
            @Value("${monitoring.service.url}") String baseUrl,
            HttpServletRequest request
    ) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
        this.request = request;
    }

    private HttpHeaders createHeadersWithAuth() {
        HttpHeaders headers = new HttpHeaders();
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && !authHeader.isBlank()) {
            headers.set("Authorization", authHeader);
        }
        return headers;
    }

    public List<VitalSignRemoteResponse> getMeasurementsByPatient(Long patientId) {
        String url = baseUrl + "/monitoring/patients/" + patientId + "/measurements";

        HttpEntity<Void> entity = new HttpEntity<>(createHeadersWithAuth());

        ResponseEntity<VitalSignRemoteResponse[]> response =
                restTemplate.exchange(url, HttpMethod.GET, entity, VitalSignRemoteResponse[].class);

        VitalSignRemoteResponse[] body = response.getBody();
        return body != null ? Arrays.asList(body) : List.of();
    }

    public List<AlertRemoteResponse> getAlertsByPatient(Long patientId) {
        String url = baseUrl + "/monitoring/patients/" + patientId + "/alerts";

        HttpEntity<Void> entity = new HttpEntity<>(createHeadersWithAuth());

        ResponseEntity<AlertRemoteResponse[]> response =
                restTemplate.exchange(url, HttpMethod.GET, entity, AlertRemoteResponse[].class);

        AlertRemoteResponse[] body = response.getBody();
        return body != null ? Arrays.asList(body) : List.of();
    }
}

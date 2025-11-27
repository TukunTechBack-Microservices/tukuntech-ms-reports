package com.upc.tukuntechmsreports.reports.application.dto.remote;


import java.time.LocalDateTime;

public record VitalSignRemoteResponse(
        Long id,
        Long patientId,
        Long deviceId,
        Integer heartRate,
        Integer oxygenLevel,
        Double temperature,
        LocalDateTime timestamp
) {}
package com.upc.tukuntechmsreports.reports.application.dto.remote;

import java.time.LocalDateTime;

public record AlertRemoteResponse(
        Long id,
        Long patientId,
        Long deviceId,
        String type,
        String severity,
        String message,
        LocalDateTime createdAt
) {}
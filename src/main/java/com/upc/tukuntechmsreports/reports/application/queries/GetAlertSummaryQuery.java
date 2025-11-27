package com.upc.tukuntechmsreports.reports.application.queries;

import java.time.LocalDate;

public record GetAlertSummaryQuery(Long patientId, LocalDate startDate, LocalDate endDate) {}

package com.upc.tukuntechmsreports.reports.application.queries;

import java.time.LocalDate;

public record GetDailyReportQuery(Long patientId, LocalDate date) {}

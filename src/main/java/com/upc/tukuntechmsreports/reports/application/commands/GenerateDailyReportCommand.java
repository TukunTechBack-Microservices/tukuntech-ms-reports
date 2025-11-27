package com.upc.tukuntechmsreports.reports.application.commands;

import java.time.LocalDate;

public record GenerateDailyReportCommand(Long patientId, LocalDate date) {}

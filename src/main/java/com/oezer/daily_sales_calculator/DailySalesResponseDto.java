package com.oezer.daily_sales_calculator;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"datum", "dailyTotals", "records"})
public class DailySalesResponseDto {
    private LocalDate datum;
    private List<SalesRecord> records;
    private DailyTotalsDto dailyTotals;

    public DailySalesResponseDto(LocalDate datum, List<SalesRecord> records, DailyTotalsDto dailyTotals) {
        this.records = records;
        this.dailyTotals = dailyTotals;
        this.datum = datum;
    }

    public LocalDate getDatum() {
        return datum;
    }

    public void setDatum(LocalDate datum) {
        this.datum = datum;
    }

    public List<SalesRecord> getRecords() {
        return records;
    }

    public void setRecords(List<SalesRecord> records) {
        this.records = records;
    }

    public DailyTotalsDto getDailyTotals() {
        return dailyTotals;
    }

    public void setDailyTotals(DailyTotalsDto dailyTotals) {
        this.dailyTotals = dailyTotals;
    }
}

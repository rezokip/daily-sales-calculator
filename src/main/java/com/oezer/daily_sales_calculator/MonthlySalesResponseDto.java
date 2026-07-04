package com.oezer.daily_sales_calculator;

import java.time.YearMonth;
import java.util.List;

public class MonthlySalesResponseDto {
    private YearMonth month;
    private List<DailySalesResponseDto> dailySales;
    private MonthlyTotalsDto monthlyTotals;

    public MonthlySalesResponseDto(YearMonth month,
                           List<DailySalesResponseDto> dailySales,
                           MonthlyTotalsDto monthlyTotals) {
        this.month = month;
        this.dailySales = dailySales;
        this.monthlyTotals = monthlyTotals;
    }

    public YearMonth getMonth() {
        return month;
    }

    public void setMonth(YearMonth month) {
        this.month = month;
    }

    public List<DailySalesResponseDto> getDailySales() {
        return dailySales;
    }

    public void setDailySales(List<DailySalesResponseDto> dailySales) {
        this.dailySales = dailySales;
    }

    public MonthlyTotalsDto getMonthlyTotals() {
        return monthlyTotals;
    }

    public void setMonthlyTotals(MonthlyTotalsDto monthlyTotals) {
        this.monthlyTotals = monthlyTotals;
    }
}

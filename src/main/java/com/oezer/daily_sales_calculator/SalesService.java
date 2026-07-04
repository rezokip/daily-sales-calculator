package com.oezer.daily_sales_calculator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SalesService {

    @Autowired
    MdbService mdbService;

    public List<MonthlySalesResponseDto> getMonthlySales(List<DailySalesResponseDto> dailyData) {

        Map<YearMonth, List<DailySalesResponseDto>> grouped =
                dailyData.stream()
                        .collect(Collectors.groupingBy(d -> YearMonth.from(d.getDatum())));

        return grouped.entrySet().stream()
                .map(entry -> {

                    YearMonth month = entry.getKey();
                    List<DailySalesResponseDto> days = entry.getValue();

                    MonthlyTotalsDto totals = calculateMonthlyTotals(days);

                    return new MonthlySalesResponseDto(month, days, totals);
                })
                .sorted(Comparator.comparing(MonthlySalesResponseDto::getMonth))
                .toList();
    }

    private MonthlyTotalsDto calculateMonthlyTotals(List<DailySalesResponseDto> days) {

        double umsatz = days.stream()
                .flatMap(d -> d.getRecords().stream())
                .mapToDouble(SalesRecord::getGesamtUmsatz)
                .sum();

        double brot = days.stream()
                .flatMap(d -> d.getRecords().stream())
                .mapToDouble(SalesRecord::getBrotmenge)
                .sum();

        double cig = days.stream()
                .flatMap(d -> d.getRecords().stream())
                .mapToDouble(SalesRecord::getCigkoftemengeInGram)
                .sum();

        return new MonthlyTotalsDto(umsatz, brot, cig);
    }

    public List<MonthlySalesResponseDto> getSales(LocalDate startDatum, LocalDate endDatum, String artikelnummer) throws SQLException {

   //     LocalDate filterStartDatum = startDatum != null ? LocalDate.parse(startDatum) : null;
   //     LocalDate filterEndDatum = endDatum != null ? LocalDate.parse(endDatum) : null;


        List<SalesRecord> allRecords = mdbService.getSalesRecords(startDatum, endDatum);

        List<SalesRecord> filteredRecords = allRecords.stream()
                .filter(r -> artikelnummer == null || r.getArtNr().equalsIgnoreCase(artikelnummer))
                .toList();

        Map<LocalDate, List<SalesRecord>> recordsByDate = filteredRecords.stream()
                .collect(Collectors.groupingBy(SalesRecord::getVerkaufsdatum));

        List<DailySalesResponseDto> dailyResponses = recordsByDate.entrySet().stream()
                .map(entry -> {
                    LocalDate tag = entry.getKey();
                    List<SalesRecord> records = entry.getValue();
                    DailyTotalsDto totals = calculateDailyTotals(records);
                    return new DailySalesResponseDto(tag, records, totals);
                })
                .sorted((d1, d2) -> d2.getDatum().compareTo(d1.getDatum())) // absteigend sortiert
                .toList();


        return getMonthlySales(dailyResponses);
    }

    public DailyTotalsDto calculateDailyTotals(List<SalesRecord> records) {
        double gesamtUmsatz = records.stream().mapToDouble(SalesRecord::getGesamtUmsatz).sum();
        double gesamtBrot = records.stream().mapToDouble(SalesRecord::getBrotmenge).sum();
        double gesamtCigkoftemenge = records.stream().mapToDouble(SalesRecord::getCigkoftemengeInGram).sum();

        return new DailyTotalsDto(gesamtUmsatz, gesamtBrot, gesamtCigkoftemenge);
    }

}

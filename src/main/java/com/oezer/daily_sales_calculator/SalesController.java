package com.oezer.daily_sales_calculator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@RestController
public class SalesController {

    private final SalesService salesService;

    @Autowired
    private ExcelExportService excelExportService;

    public SalesController(SalesService salesService) {
        this.salesService = salesService;
    }

    @GetMapping("/sales")
    public List<MonthlySalesResponseDto> getSales(
            @RequestParam(required = false) LocalDate startDatum,
            @RequestParam(required = false) LocalDate endDatum,
            @RequestParam(required = false) String artikelnummer
    ) throws SQLException, IOException {


        List<MonthlySalesResponseDto> allSales = salesService.getSales(startDatum, endDatum, artikelnummer);
        return allSales;
    }

    @GetMapping("/sales/last{numberOfDays}")
    public List<MonthlySalesResponseDto> getSalesLastNumberOfDays(
            @RequestParam(required = false) String artikelnummer,
            @PathVariable Long numberOfDays
    ) throws SQLException, IOException {

        LocalDate endDatum = LocalDate.now();
        LocalDate startDatum = endDatum.minusDays(numberOfDays);

        List<MonthlySalesResponseDto> allSales = salesService.getSales(startDatum, endDatum, artikelnummer);
        return allSales;
    }

    @GetMapping("/sales/excel")
    public ResponseEntity<byte[]> getSalesExcel(
            @RequestParam(required = false) LocalDate startDatum,
            @RequestParam(required = false) LocalDate endDatum,
            @RequestParam(required = false) String artikelnummer
    ) throws SQLException, IOException {
        List<MonthlySalesResponseDto> allSales = salesService.getSales(startDatum, endDatum, artikelnummer);
        byte[] excelBytes = excelExportService.exportSalesToExcel(allSales);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"sales_report.xlsx\"")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(excelBytes);
    }

    @GetMapping("/sales/last{numberOfDays}/excel")
    public ResponseEntity<byte[]> getSalesLastNumberOfDaysExcel(
            @RequestParam(required = false) String artikelnummer,
            @PathVariable Long numberOfDays
    ) throws SQLException, IOException {
        LocalDate endDatum = LocalDate.now();
        LocalDate startDatum = endDatum.minusDays(numberOfDays);

        List<MonthlySalesResponseDto> allSales = salesService.getSales(startDatum, endDatum, artikelnummer);
        byte[] excelBytes = excelExportService.exportSalesToExcel(allSales);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"sales_report_last_" + numberOfDays + "_days.xlsx\"")                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(excelBytes);
    }
}
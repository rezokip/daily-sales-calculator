package com.oezer.daily_sales_calculator;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;
@Service
public class ExcelExportService {

    public byte[] exportSalesToExcel(List<MonthlySalesResponseDto> months) throws IOException {

        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            String[] columns = {
                    "Datum", "Tagesumsatz Gesamt", "Brotmenge Gesamt", "Çiğköfte Gesamt (g)",
                    "",
                    "Art-Nr", "Artikeltext", "Menge Verkauft", "Umsatz Artikel",
                    "Einzelpreis", "Durchschnittspreis", "Brotmenge Artikel", "Çiğköfte Artikel (g)"
            };

            // =========================
            // STYLES
            // =========================
            Font monthFont = workbook.createFont();
            monthFont.setBold(true);
            monthFont.setFontHeightInPoints((short) 16);
            monthFont.setColor(IndexedColors.WHITE.getIndex());

            CellStyle monthHeaderStyle = workbook.createCellStyle();
            monthHeaderStyle.setFont(monthFont);
            monthHeaderStyle.setFillForegroundColor(IndexedColors.OLIVE_GREEN.getIndex());
            monthHeaderStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            CellStyle monthValueStyle = workbook.createCellStyle();
            monthValueStyle.setFont(monthFont);
            monthValueStyle.setFillForegroundColor(IndexedColors.SEA_GREEN.getIndex());
            monthValueStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 12);

            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            Font dayFont = workbook.createFont();
            dayFont.setBold(true);

            CellStyle dayStyle = workbook.createCellStyle();
            dayStyle.setFont(dayFont);

            CellStyle normalStyle = workbook.createCellStyle();

            CellStyle dateStyle = workbook.createCellStyle();
            dateStyle.setDataFormat(
                    workbook.getCreationHelper().createDataFormat().getFormat("dd.MM.yyyy")
            );

            for (MonthlySalesResponseDto monthData : months) {

                Sheet sheet = workbook.createSheet(
                        monthData.getMonth().getMonth().getDisplayName(TextStyle.FULL, Locale.GERMAN)
                                + " " + monthData.getMonth().getYear()
                );

                int rowIdx = 0;

                // =========================
                // 🔥 MONTH HEADER
                // =========================
                Row summaryHeader = sheet.createRow(rowIdx++);

                create(summaryHeader, 0, "Monat", monthHeaderStyle);
                create(summaryHeader, 1, "Umsatz Gesamt", monthHeaderStyle);
                create(summaryHeader, 2, "Brot Gesamt", monthHeaderStyle);
                create(summaryHeader, 3, "Çiğköfte Gesamt", monthHeaderStyle);

                // =========================
                // 📊 MONTHLY TOTALS (SUMMARY ROW)
                // =========================
                Row summaryRow = sheet.createRow(rowIdx++);

                create(summaryRow, 0, monthData.getMonth().format(DateTimeFormatter.ofPattern("MMMM yyyy", Locale.GERMAN)), monthValueStyle);
                create(summaryRow, 1, monthData.getMonthlyTotals().getGesamtUmsatz(), monthValueStyle);
                create(summaryRow, 2, monthData.getMonthlyTotals().getGesamtBrotmenge(), monthValueStyle);
                create(summaryRow, 3, monthData.getMonthlyTotals().getGesamtCigkoftemenge(), monthValueStyle);

                rowIdx++; // spacing

                // =========================
                // DAILY DATA
                // =========================
                for (DailySalesResponseDto day : monthData.getDailySales()) {

                    Row headerRow = sheet.createRow(rowIdx++);

                    for (int i = 0; i < columns.length; i++) {
                        Cell c = headerRow.createCell(i);
                        c.setCellValue(columns[i]);
                        c.setCellStyle(headerStyle);
                    }

                    boolean first = true;

                    for (SalesRecord r : day.getRecords()) {

                        Row row = sheet.createRow(rowIdx++);

                        if (first) {
                            Cell dateCell = row.createCell(0);
                            dateCell.setCellValue(java.sql.Date.valueOf(day.getDatum()));
                            dateCell.setCellStyle(dateStyle);

                            create(row, 1, day.getDailyTotals().getGesamtUmsatz(), dayStyle);
                            create(row, 2, day.getDailyTotals().getGesamtBrotmenge(), dayStyle);
                            create(row, 3, day.getDailyTotals().getGesamtCigkoftemenge(), dayStyle);

                            first = false;
                        } else {
                            for (int i = 0; i <= 3; i++) {
                                create(row, i, "", normalStyle);
                            }
                        }

                        create(row, 4, "", normalStyle);
                        create(row, 5, r.getArtNr(), normalStyle);
                        create(row, 6, r.getArtikeltext(), normalStyle);
                        create(row, 7, r.getAnzahlVerkauft(), normalStyle);
                        create(row, 8, r.getGesamtUmsatz(), normalStyle);
                        create(row, 9, r.getEinzelpreis(), normalStyle);
                        create(row, 10, r.getDurchschnittspreis(), normalStyle);
                        create(row, 11, r.getBrotmenge(), normalStyle);
                        create(row, 12, r.getCigkoftemengeInGram(), normalStyle);
                    }

                    rowIdx++;
                }

                for (int i = 0; i < columns.length; i++) {
                    sheet.autoSizeColumn(i);
                }
            }

            workbook.write(out);
            return out.toByteArray();
        }
    }

    // =========================
    // HELPERS
    // =========================
    private void create(Row row, int col, String val, CellStyle style) {
        Cell c = row.createCell(col);
        c.setCellValue(val);
        c.setCellStyle(style);
    }

    private void create(Row row, int col, double val, CellStyle style) {
        Cell c = row.createCell(col);
        c.setCellValue(val);
        c.setCellStyle(style);
    }

    private void create(Row row, int col, int val, CellStyle style) {
        Cell c = row.createCell(col);
        c.setCellValue(val);
        c.setCellStyle(style);
    }
}
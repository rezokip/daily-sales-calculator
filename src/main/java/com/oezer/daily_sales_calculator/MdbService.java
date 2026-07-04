package com.oezer.daily_sales_calculator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MdbService {

    private static final Logger log = LoggerFactory.getLogger(MdbService.class);
    private final String filePath = "C:/Users/ozerkip/OneDrive/PosBackup/db_source.mdb";
    private final String password = "38$Cnak!6184";
    private final String url = "jdbc:ucanaccess://" + filePath + ";password=" + password + ";skipCustomFunctions=true";

    public List<SalesRecord> getSalesRecords(LocalDate startDatum, LocalDate endDatum) throws SQLException {
        List<SalesRecord> records = new ArrayList<>();

        StringBuilder query = new StringBuilder(
                "SELECT " +
                        "SUBSTRING(Z_ERSTELLUNG, 1, 10) AS verkaufsdatum, " +
                        "ART_NR, " +
                        "MAX(ARTIKELTEXT) AS artikeltext, " +
                        "SUM(MENGE) AS anzahl_verkauft, " +
                        "SUM(POS_BRUTTO) AS gesamtumsatz, " +
                        "MAX(STK_BR) AS einzelpreis, " +
                        "CASE " +
                        "   WHEN SUM(MENGE) <> 0 " +
                        "   THEN SUM(POS_BRUTTO) / SUM(MENGE) " +
                        "   ELSE 0 " +
                        "END AS durchschnittspreis " +
                        "FROM BONPOS " +
                        "WHERE P_STORNO <> '1' " +
                        "AND ART_NR <> 'TRG' " +
                        "AND ART_NR <> '' "

        );

        // 👉 Filter NUR hier hinzufügen
       /* if (datum != null) {
            query.append("AND Z_ERSTELLUNG >= ? AND Z_ERSTELLUNG < ? ");
        }
*/
        // Datum-Filter
        if (startDatum != null && endDatum != null) {
            query.append("AND Z_ERSTELLUNG >= ? AND Z_ERSTELLUNG < ? ");
        }

        // 👉 GROUP BY + ORDER BY nur EINMAL am Ende
        query.append(
                "GROUP BY SUBSTRING(Z_ERSTELLUNG, 1, 10), ART_NR " +
                        "ORDER BY verkaufsdatum DESC, ART_NR"
        );

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement ps = conn.prepareStatement(query.toString())) {

            // 👉 Parameter setzen
        /*    if (datum != null) {
                ps.setString(1, datum.toString());
                ps.setString(2, datum.plusDays(1).toString());
          '*/

            int paramIndex = 1;


            if (startDatum != null && endDatum != null) {
                ps.setString(paramIndex++, startDatum.toString());
                ps.setString(paramIndex++, endDatum.plusDays(1).toString());
            }




            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {

                    LocalDate date = LocalDate.parse(rs.getString("verkaufsdatum"));

                    records.add(new SalesRecord(
                            date,
                            rs.getString("ART_NR"),
                            rs.getString("artikeltext"),
                            rs.getInt("anzahl_verkauft"),
                            rs.getDouble("gesamtumsatz"),
                            rs.getDouble("einzelpreis"),
                            rs.getDouble("durchschnittspreis")
                    ));
                }
            }
        }

        return records;
    }

}
package com.oezer.daily_sales_calculator;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class SalesRecord {

    private LocalDate verkaufsdatum;
    private String artNr;
    private String artikeltext;
    private int anzahlVerkauft;
    private double gesamtUmsatz;
    private double einzelpreis;
    private double durchschnittspreis;
    private double einzelBrotMenge;
    private double brotmenge;
    private double einzelCigkofteMenge;
    private double cigkoftemenge;


    public SalesRecord(LocalDate verkaufsdatum, String artNr, String artikeltext, int anzahlVerkauft, double gesamtUmsatz, double einzelpreis, double durchschnittspreis) {
        this.verkaufsdatum = verkaufsdatum;
        this.artNr = artNr;
        this.artikeltext = artikeltext;
        this.anzahlVerkauft = anzahlVerkauft;
        this.gesamtUmsatz = gesamtUmsatz;
        this.einzelpreis = einzelpreis;
        this.durchschnittspreis = durchschnittspreis;
        Produkt produkt = Produkt.fromCode(artNr);
        if (produkt != null) {
            this.einzelBrotMenge = produkt.getBrotMenge();
            this.brotmenge = anzahlVerkauft * produkt.getBrotMenge();
            this.cigkoftemenge = anzahlVerkauft * produkt.getCigkoefteMenge();
            this.einzelCigkofteMenge = produkt.getCigkoefteMenge();
        } else {
            this.einzelBrotMenge = 0.0;
            this.brotmenge = 0.0;
            this.einzelCigkofteMenge = 0.0;
            this.cigkoftemenge = 0.0;
        }
    }



    public LocalDate getVerkaufsdatum() {
        return verkaufsdatum;
    }

    public void setVerkaufsdatum(LocalDate verkaufsdatum) {
        this.verkaufsdatum = verkaufsdatum;
    }

    public String getArtNr() {
        return artNr;
    }

    public void setArtNr(String artNr) {
        this.artNr = artNr;
    }

    public String getArtikeltext() {
        return artikeltext;
    }

    public void setArtikeltext(String artikeltext) {
        this.artikeltext = artikeltext;
    }

    public int getAnzahlVerkauft() {
        return anzahlVerkauft;
    }

    public void setAnzahlVerkauft(int anzahlVerkauft) {
        this.anzahlVerkauft = anzahlVerkauft;
    }

    public double getGesamtUmsatz() {
        return gesamtUmsatz;
    }

    public void setGesamtUmsatz(double gesamtUmsatz) {
        this.gesamtUmsatz = gesamtUmsatz;
    }

    public double getEinzelpreis() {
        return einzelpreis;
    }

    public void setEinzelpreis(double einzelpreis) {
        this.einzelpreis = einzelpreis;
    }

    public double getDurchschnittspreis() {
        return durchschnittspreis;
    }

    public void setDurchschnittspreis(double durchschnittspreis) {
        this.durchschnittspreis = durchschnittspreis;
    }

    public double getEinzelBrotMenge() {
        return einzelBrotMenge;
    }

    public void setEinzelBrotMenge(double einzelBrotMenge) {
        this.einzelBrotMenge = einzelBrotMenge;
    }

    public double getEinzelCigkofteMenge() {
        return einzelCigkofteMenge;
    }

    public void setEinzelCigkofteMenge(double einzelCigkofteMenge) {
        this.einzelCigkofteMenge = einzelCigkofteMenge;
    }

    public double getBrotmenge() {
        return brotmenge;
    }

    public double getCigkoftemengeInGram() {
        return cigkoftemenge;
    }


    public enum Produkt {

        CIGKOEFTE_DUERUEM("001", 1.0, 125.0),
        CIGKOEFTE_BIG_DUERUEM("011", 2.0, 150.0),
        CIGKOEFTE_BIG_MENU("012", 2.0, 150.0),
        FALAFEL_DUERUEM("031", 1.0, 0.0),
        MIX_EAT_DUERUEM("038", 1.0, 75.0),

        CIGKOEFTE_PAKET_S("002", 2.0, 250.0),
        CIGKOEFTE_PAKET_M("004", 4.0, 500.0),
        CIGKOEFTE_PAKET_M_FREITAG("1408", 4.0, 500.0),
        CIGKOEFTE_JOKER("003", 4.0, 750.0),
        CIGKOEFTE_JOKER_FREITAG("1407", 5.0, 750),
        CIGKOEFTE_FAMILIE("005", 8.0, 1000.0),

        MIX_EAT_TELLER("034", 1.0, 75.0),
        TELLER_S("102", 1.0, 200.0),
        TELLER_M("103", 2.0, 400.0),
        TELLER_L("104", 3.0, 600.0),
        PARTY_S("207", 8.0, 1250.0),
        PARTY_M("208", 12.0, 1875.0),
        PARTY_L("209", 16.0, 2500.0),
        EXTRA_LAVAS("015", 1.0, 0.0),
        EXTRA_LAVAS_5("515", 5.0, 0.0);

        private final String code;
        private final double brotMenge;
        private final double cigkoefteMenge;

        Produkt(String code, double brotMenge, double cigkoefteMenge) {
            this.code = code;
            this.brotMenge = brotMenge;
            this.cigkoefteMenge = cigkoefteMenge;
        }

        public String getCode() {
            return code;
        }

        public double getBrotMenge() {
            return brotMenge;
        }

        public double getCigkoefteMenge() {
            return cigkoefteMenge;
        }

        private static final Map<String, Produkt> BY_CODE =
                Arrays.stream(Produkt.values())
                        .collect(Collectors.toMap(Produkt::getCode, p -> p));

        public static Produkt fromCode(String code) {
            return BY_CODE.get(code);
        }
    }
}
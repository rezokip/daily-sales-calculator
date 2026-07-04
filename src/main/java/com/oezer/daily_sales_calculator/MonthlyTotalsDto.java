package com.oezer.daily_sales_calculator;

public class MonthlyTotalsDto {

    private double gesamtUmsatz;
    private double gesamtBrotmenge;
    private double gesamtCigkoftemenge;

    public MonthlyTotalsDto(double gesamtUmsatz,
                            double gesamtBrotmenge,
                            double gesamtCigkoftemenge) {
        this.gesamtUmsatz = gesamtUmsatz;
        this.gesamtBrotmenge = gesamtBrotmenge;
        this.gesamtCigkoftemenge = gesamtCigkoftemenge;
    }

    public double getGesamtUmsatz() {
        return gesamtUmsatz;
    }

    public void setGesamtUmsatz(double gesamtUmsatz) {
        this.gesamtUmsatz = gesamtUmsatz;
    }

    public double getGesamtBrotmenge() {
        return gesamtBrotmenge;
    }

    public void setGesamtBrotmenge(double gesamtBrotmenge) {
        this.gesamtBrotmenge = gesamtBrotmenge;
    }

    public double getGesamtCigkoftemenge() {
        return gesamtCigkoftemenge;
    }

    public void setGesamtCigkoftemenge(double gesamtCigkoftemenge) {
        this.gesamtCigkoftemenge = gesamtCigkoftemenge;
    }
}
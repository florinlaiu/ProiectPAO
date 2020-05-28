package Individuals;

public class Conversion {
    private final Integer from, to;
    private final double rate;
    public Conversion(Integer from, Integer to, Double rate) {
        this.from = from;
        this.to = to;
        this.rate = rate;
    }
    public Integer getFrom() {
        return from;
    }
    public Integer getTo() {
        return to;
    }
    public double getRate() {
        return rate;
    }

    public String detailedString() {
        return "Conversie de la: " + from + " la " + to + " cu costul de " + rate + "\n";
    }
    @Override
    public String toString() {
        return from + "," + to + "," + rate;
    }
}

package Individuals;

public class Currency {
    private final String CurrencyName;
    private final Integer CurrencyId;
    public Currency(String CurrencyName, Integer CurrencyId) {
        this.CurrencyName = CurrencyName;
        this.CurrencyId = CurrencyId;
    }

    public String getName() {
        return CurrencyName;
    }

    public Integer getId() {
        return CurrencyId;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && getClass() == obj.getClass()) {
            Currency that = (Currency) obj;
            return CurrencyId == that.CurrencyId;
        }
        return false;
    }


    public String detailedString() {
        return "Currency-ul cu numele" + " " + CurrencyName + " are id-ul " + CurrencyId;
    }
    @Override
    public String toString() {
        return CurrencyName;
    }
}

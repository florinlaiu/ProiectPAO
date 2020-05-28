package Individuals;

public class Account {
    private final Integer currId;
    private final Integer belongs;
    private Double amount;
    public Account(Integer currId, Integer belongs, Double amount) {
        this.belongs = belongs;
        this.currId = currId;
        this.amount = amount;
    }


    public Integer belongsTo() {
        return belongs;
    }
    public void increaseBy(Double by) {
        amount += by;
    }
    public void setAmount(Double to) {
        amount = to;
    }
    public Double getAmount() {
        return amount;
    }
    public Integer getCurrencyId() {
        return currId;
    }


    public String detailedString() {
        return "Currency: " + currId + " ClientId: " + belongs + " Amount: " + amount;
    }

    @Override
    public String toString() {
        return currId + "," + belongs + "," + amount + "\n";
    }
}

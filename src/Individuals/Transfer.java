package Individuals;

import Individuals.Conversion;

public class Transfer {
    private Integer clientId;
    private Integer ac1, ac2;
    private final Conversion did;
    private final Double amount;
    public Transfer(Integer ac1, Integer ac2, Integer ClientId, Conversion did, Double amount) {
        this.ac1 = ac1;
        this.ac2 = ac2;
        this.clientId = ClientId;
        this.did = did;
        this.amount = amount;
    }

}

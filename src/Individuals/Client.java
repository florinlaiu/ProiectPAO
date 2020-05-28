package Individuals;

// client ce are card la banca

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import Service.BankCsv;

public class Client {
    protected final String Name;
    protected Integer age;
    protected final Integer id;
    private final List<Account> accs;
    private final List<Transfer>Transfers;


    public Client(String Name, Integer age, Integer id) {
        this.Name = Name;
        this.age = age;
        this.id = id;
        accs = new ArrayList<>();
        Transfers = new ArrayList<>();
    }

    public String getName() {
        return Name;
    }
    public Integer getAge() {
        return age;
    }

    public void addTransfer(Integer ac1, Integer ac2, Integer ClientId, Conversion did, Double amount) {
        Transfer T = new Transfer(ac1, ac2, ClientId, did, amount);
        Transfers.add(T);
    }

    public List<Account>getAllAccounts() {
        return accs;
    }

    public void deleteAllAcc() {
        while(!accs.isEmpty()) {
            accs.remove(0);
        }
    }

    public Integer getId() {
        return id;
    }
    public void addAccount(Account now) {
        accs.add(now);
    }
    public void deleteAccount(int idx) throws IndexOutOfBoundsException{
        if(idx < 0 || idx >= accs.size())
            throw new IndexOutOfBoundsException();
        accs.remove(idx);
    }
    public void deleteAccount(Account now) {
        Iterator<Account> itr = accs.iterator();
        while (itr.hasNext()) {
            Account nxt = itr.next();
            if (nxt.equals(now)) {
                itr.remove();
                return;
            }
        }
    }

    public String move_money(Integer from, Integer to, Double value) throws IndexOutOfBoundsException {
        if (from < 0 || to < 0 || from >= accs.size() || to >= accs.size())
            throw new IndexOutOfBoundsException();
        Account AccFrom = accs.get(from);
        if(value > AccFrom.getAmount()) {
            return "Tranzactie esuata! Suma e prea mare\n";
        }
        Account AccTo = accs.get(to);
        Integer C1 = AccFrom.getCurrencyId();
        Integer C2 = AccTo.getCurrencyId();
        BankCsv ActBank = BankCsv.getInstance();
        Double rate = ActBank.convRateBetween(C1, C2);
        AccFrom.increaseBy(-value);
        AccTo.increaseBy(value * rate);
        accs.set(from, AccFrom);
        accs.set(to, AccTo);
        addTransfer(from, to, id, new Conversion(C1, C2, rate), value);
        return "Tranzactie reusita!";
    }

    public String detailedString() {
        String s = "Name: " + Name + " "+  age + " ID " + id + "\n";
        s += "Numarul de conturi deschise: " + accs.size() + "\n";
        Iterator<Account> it = accs.iterator();
        while(it.hasNext()) {
            Account now = it.next();
            s += now;
            s += "\n";
        }
        return s;
    }
    @Override
    public String toString() {
        String s = Name + "," + age + "," + id;
        s += "\n";
        return s;
    }
    public Integer nrAccs() {
        return accs.size();
    }
    public Double getTotal(Integer currId) {
        Double sum = 0.0;
        Iterator<Account>itr = accs.iterator();
        while(itr.hasNext()) {
            Account nxt = itr.next();
            if(nxt.getCurrencyId().equals(currId)) {
                sum += nxt.getAmount();
            }
        }
        return sum;
    }


}
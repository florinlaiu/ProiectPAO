package Service;

import GenericRead.ReaderCSV;
import GenericRead.WriterCSV;
import Individuals.*;

import java.util.*;

import Extra.PairInt;
import Individuals.Currency;

public class BankCsv implements BankInterface {
    private List<Currency> Currencies;
    private List<Client> Clients;
    private List<Account> Accounts;
    private SortedMap<PairInt, Double> ConvRate;
    private static BankCsv single_instance = null;



    protected BankCsv() {
        // luam curenciurile si conversiile , le incarcam(pt ca le avem def de la inceput)
        Currencies = ReaderCSV.Reader.loadCurrencies();
        List<Conversion>ConvRateArr = ReaderCSV.Reader.loadConversions();
        ConvRate = new TreeMap<>();
        for (Conversion c : ConvRateArr) {
            PairInt p = new PairInt(c.getFrom(), c.getTo());
            ConvRate.put(p, c.getRate());
        }
        Accounts = new ArrayList<>();
    }

    public SortedMap<PairInt, Double>getConversions() {
        return new TreeMap<>(ConvRate);
    }

    public List<Client>getAllClients() {
        return Clients;
    }

    public List<Account>getAllAccountsForClientId(Integer id) {
        Integer pos = findPosClient(id);
        try {
            System.out.println(Clients.get(pos).getTotal(1));
            Client c = Clients.get(pos);
            return c.getAllAccounts();
        }
        catch(IndexOutOfBoundsException e) {
            e.printStackTrace();
            return null;
        }
    }



    public static BankCsv getInstance() {
        if (single_instance == null) {
            single_instance = new BankCsv();
        }
        return single_instance;
    }

    public List<Currency>getCurrencies() {
        return Currencies;
    }
    public void loadClients() {
        Clients =  ReaderCSV.Reader.loadClients();
        Accounts = new ArrayList<>();
        List<Account>AccountsAux = new ArrayList<>(ReaderCSV.Reader.loadAccounts());
        Iterator<Account>it = AccountsAux.iterator();
        while(it.hasNext()) {
            Account nxt = it.next();
            addAccount(nxt);
        }
    }

    @Override
    public Boolean addClient(Client c) {
        WriterCSV.Writer.printAudit("addClient");
        if (findPosClient(c.getId()) != -1)
            return false;
        Clients.add(c);
        WriterCSV.Writer.printClients(Clients);
        return true;
    }

    public Integer findPosClient(Integer id) {
        Integer i = 0;
        Iterator<Client>it = Clients.iterator();
        while(it.hasNext()) {
            Client nxt = it.next();
            if(nxt.getId().equals(id)) {
                return i;
            }
            ++i;
        }
        return -1;
    }

    @Override
    public void removeClient(Integer id){
        Iterator<Client> it = Clients.iterator();WriterCSV.Writer.printAudit("removeClient");
        while (it.hasNext()) {
            Integer idNow = ((Client) it.next()).getId();
            if (id.equals(idNow)) {
                deleteAllAccsForClientId(id);
                it.remove();
                WriterCSV.Writer.printClients(Clients);
                return;
            }
        }
    }

    @Override
    public void addAccount(Account a) {
        WriterCSV.Writer.printAudit("addAccount");
        Integer id = a.belongsTo();
        Integer pos = findPosClient(id);
        System.out.println(pos);
        try {
            Client c = Clients.get(pos);
            c.addAccount(a);
            Clients.set(pos, c);
            Accounts.add(a);
            WriterCSV.Writer.printAccounts(Accounts);
        }
        catch(IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    public Integer findPosAccount(Account a) {
        WriterCSV.Writer.printAudit("findPosAccount");
        Integer id = a.belongsTo();
        Iterator<Account>it = Accounts.iterator();
        Integer i = 0;
        while(it.hasNext()) {
            Account nxt = it.next();
            if(nxt.equals(a))
                return i;
            ++i;
        }
        return -1;
    }

    //sterge contul din lista mea,sterge contul din lista clientului
    @Override
    public void removeAccount(Account a) {
        WriterCSV.Writer.printAudit("removeAccount");
        Integer id = a.belongsTo();
        Integer pos = findPosClient(id);

        System.out.println(a);
        //sterg din lista clientului !

        try {
            Client c = Clients.get(pos);
            c.deleteAccount(a);
            Clients.set(pos, c);
            WriterCSV.Writer.printAccounts(Accounts);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }

        // sterg din lista mea ..
        //Integer posAccInBank = findPosAccount(a);
       // System.out.println(posAccInBank);
       // System.out.println(Accounts);
        try {
            Accounts.remove(a);
            System.out.println(Accounts);
        }
        catch(IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteAllAccsForClientId(Integer id) {
        WriterCSV.Writer.printAudit("deleteAllAccsForClientId");
        Integer pos = findPosClient(id);
        if(pos == -1) {
            System.out.println("Nu exista contul!");
            return;
        }
        Client c = Clients.get(pos);
        List<Account> cAccs = new ArrayList<>(c.getAllAccounts());
        System.out.println("Vreau sa sterg: ");
        System.out.println(cAccs);
        System.out.println(Accounts);
        for (Account now : cAccs) { removeAccount(now);
            }
        WriterCSV.Writer.printAccounts(Accounts);

    }

    @Override
    public void makeTransfer(Integer id, Integer from, Integer to, Double amount) {
        Integer pos = findPosClient(id);
        WriterCSV.Writer.printAudit("makeTransfer");
        try {
            Client c = Clients.get(pos);
            c.move_money(from, to, amount);
            Clients.set(pos, c);
            WriterCSV.Writer.printAccounts(Accounts);
        }
        catch(IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Double convRateBetween(Integer id1, Integer id2) {
        WriterCSV.Writer.printAudit("Seeking Conversion Rate");
        if(id1 == id2)
            return 1.0;
        PairInt p = new PairInt(id1, id2);
        if(ConvRate.containsKey(p))
            return ConvRate.get(p);
        return 0.0;
    }

    @Override
    public void AddConversion(Integer id1, Integer id2, Double rate) {
        WriterCSV.Writer.printAudit("AddConversion");
        PairInt p = new PairInt(id1, id2);
        ConvRate.put(p, rate);
    }

    @Override
    public void DeleteConversion(Integer id1, Integer id2) {
        WriterCSV.Writer.printAudit("Delete Conversion");
        PairInt p = new PairInt(id1, id2);
        if(ConvRate.containsKey(p))
            ConvRate.remove(p);
    }

    @Override
    public void AddCurrency(String name, Integer id) {
        WriterCSV.Writer.printAudit("Adding Currency");
        Currencies.add(new Currency(name, id));
    }

    @Override
    public Integer findCurrency(Integer id) {
        Iterator<Currency>it = Currencies.iterator();
        Integer i = 0;
        while(it.hasNext()) {
            Currency nxt = it.next();
            if(nxt.getId() == id) {
                return i;
            }
            ++i;
        }
        return -1;
    }

    @Override
    public void deleteCurrency(Integer id) {
        Integer pos = findCurrency(id);
        Currencies.remove(pos);
        SortedMap<PairInt, Double>Aux = new TreeMap();
        for(Map.Entry<PairInt, Double> entry : ConvRate.entrySet()) {
            PairInt key = entry.getKey();
            Double value = entry.getValue();
            if(key.get_first() != id && key.get_second() != id)
                Aux.put(key, value);
        }
        ConvRate = Aux;

    }

    @Override
    public void UpdateConversion(Integer id1, Integer id2, Double rate) {
        AddConversion(id1, id2, rate);

    }
}

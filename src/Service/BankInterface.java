package Service;

import Extra.PairInt;
import Individuals.*;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;

public interface BankInterface {
  List<Client>getAllClients();
  public Boolean addClient(Client c);
   public void removeClient(Integer id);
    public void addAccount(Account a);
    public void removeAccount(Account a);
    public void deleteAllAccsForClientId(Integer id);
    public void makeTransfer(Integer id, Integer from, Integer to, Double amount);

    public SortedMap<PairInt, Double>getConversions();
    public void AddCurrency(String name, Integer id);
    public Integer findCurrency(Integer id);
    public void deleteCurrency(Integer id);
    public Double convRateBetween(Integer id1, Integer id2);

    public void DeleteConversion(Integer id1, Integer id2);
    public void AddConversion(Integer id1, Integer id2, Double rate);
    public void UpdateConversion(Integer id1, Integer id2, Double rate);
    public List<Currency>getCurrencies();

    static BankInterface build(BankInterface.Type type) {
     if(type == Type.DB)
       return new BankDB();
     return new BankCsv();
    }
    enum Type {
     DB, FILE;
    }
}

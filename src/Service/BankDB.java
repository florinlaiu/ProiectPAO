package Service;

import java.sql.*;
import java.util.*;

import Extra.PairInt;
import GenericRead.WriterCSV;
import Individuals.Account;
import Individuals.Client;
import Individuals.Currency;
import Managers.*;

import static java.lang.Math.min;

public class BankDB implements BankInterface {
    @Override
    public Boolean addClient(Client c) {
        WriterCSV.Writer.printAudit("addClient");
        String sql = "INSERT INTO clients VALUES(?, ?, ?)";

        try (
                Connection con = managers.DBConnectionManager.getInstance().createConnection();
                PreparedStatement statement = con.prepareStatement(sql)
        ) {
            statement.setString(1, c.getName());
            statement.setInt(2, c.getAge());
            statement.setInt(3, c.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public List<Account> getAccounts() {
        WriterCSV.Writer.printAudit("loadAccounts");
        String sql = "select * from accounts";

        try (
                Connection con = managers.DBConnectionManager.getInstance().createConnection();
                PreparedStatement statement = con.prepareStatement(sql)
        ) {
            List<Account>Accounts = new ArrayList<>();

            ResultSet set = statement.executeQuery();

            while (set.next()) {
                Integer currId = set.getInt("currId");
                Integer belongs = set.getInt("belongs");
                Double amount = set.getDouble("amount");
                Accounts.add(new Account(currId, belongs, amount));
            }

            return Accounts;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Currency> getCurrencies() {

        WriterCSV.Writer.printAudit("getCurrencies");
        String sql = "select * from currencies";

        try (
                Connection con = managers.DBConnectionManager.getInstance().createConnection();
                PreparedStatement statement = con.prepareStatement(sql)
        ) {
            List<Currency>Currencies= new ArrayList<>();

            ResultSet set = statement.executeQuery();

            while (set.next()) {
                String Name = set.getString("Name");
                Integer id = set.getInt("Id");
                Currencies.add(new Currency(Name, id));
            }

            return Currencies;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Client> getAllClients() {
        WriterCSV.Writer.printAudit("getClients");
        String sql = "select * from clients";

        try (
                Connection con = managers.DBConnectionManager.getInstance().createConnection();
                PreparedStatement statement = con.prepareStatement(sql)
        ) {
            List<Client>Clients = new ArrayList<>();

            ResultSet set = statement.executeQuery();

            while (set.next()) {
                String Name = set.getString("Name");
                Integer Age = set.getInt("Age");
                Integer id = set.getInt("Id");
                Clients.add(new Client(Name, Age, id));
            }

            return Clients;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void removeClient(Integer id) {
        WriterCSV.Writer.printAudit("RemoveClient");
        String sql = "DELETE FROM clients Where id = ?";

        try (
                Connection con = managers.DBConnectionManager.getInstance().createConnection();
                PreparedStatement statement = con.prepareStatement(sql)
        ) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addAccount(Account a) {
        WriterCSV.Writer.printAudit("addAccount");
        String sql = "INSERT INTO accounts VALUES(?, ?, ?)";

        try (
                Connection con = managers.DBConnectionManager.getInstance().createConnection();
                PreparedStatement statement = con.prepareStatement(sql)
        ) {
            statement.setInt(1, a.getCurrencyId());
            statement.setInt(2, a.belongsTo());
            statement.setDouble(3, a.getAmount());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void removeAccount(Account a) {
        WriterCSV.Writer.printAudit("removeAccount");
        String sql = "DELETE FROM accounts Where currId = ? and belongs = ? and amount = ?";
        try (
                Connection con = managers.DBConnectionManager.getInstance().createConnection();
                PreparedStatement statement = con.prepareStatement(sql)
        ) {
            statement.setInt(1, a.getCurrencyId());
            statement.setInt(2, a.belongsTo());
            statement.setDouble(3, a.getAmount());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteAllAccsForClientId(Integer id) {
        List<Account>accs = getAccounts();
        WriterCSV.Writer.printAudit("deleteAllAccsForClientId");
        Iterator<Account> it = accs.iterator();
        while(it.hasNext()) {
            Account nxt = it.next();
            removeAccount(nxt);
        }

    }

    public Account getKthAccount(Integer clientId, Integer k) {
        List<Account>accs = getAccounts();
        Iterator<Account>it = accs.iterator();
        Integer cnt = 0;
        while(it.hasNext()) {
            Account nxt = it.next();
            if(nxt.belongsTo().equals(clientId))
                ++cnt;
            if(cnt == k)
                return nxt;
        }
        return null;
    }

    @Override
    public void makeTransfer(Integer id, Integer from, Integer to, Double amount) {
        Account a1 = getKthAccount(id, from);
        Account a2 = getKthAccount(id, to);
        if(a1.equals(null) || a2.equals(null)) {
            System.out.println("Transfer invalid !");
            return;
        }
        Double rate = convRateBetween(from, to);
        amount = min(amount, a1.getAmount());
        removeAccount(a1);
        removeAccount(a2);
        a1.increaseBy(-amount);
        a2.increaseBy(amount * rate);
        addAccount(a1);
        addAccount(a2);

    }

    @Override
    public SortedMap<PairInt, Double> getConversions() {
        SortedMap<PairInt, Double>S = new TreeMap<>();
        String sql  = "SELECT * from conversions";

        try (
                Connection con = managers.DBConnectionManager.getInstance().createConnection();
                PreparedStatement statement = con.prepareStatement(sql)
        ) {
            ResultSet set = statement.executeQuery();
            while(set.next()) {
                S.put(new PairInt(set.getInt("fromId"), set.getInt("toId")), set.getDouble("rate"));

            }
            return S;
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return null;

    }



    @Override
    public void AddCurrency(String name, Integer id) {
        String sql  = "INSERT INTO currencies VALUES(?, ?)";

        try (
                Connection con = managers.DBConnectionManager.getInstance().createConnection();
                PreparedStatement statement = con.prepareStatement(sql)
        ) {
            statement.setString(1, name);
            statement.setInt(2, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();

        }
    }



    @Override
    public void deleteCurrency(Integer id) {
        WriterCSV.Writer.printAudit("DeleteConversion");
        String sql = "DELETE FROM currencies Where Id = ?";
        try (
                Connection con = managers.DBConnectionManager.getInstance().createConnection();
                PreparedStatement statement = con.prepareStatement(sql)
        ) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        SortedMap<PairInt, Double>S = getConversions();
        for (SortedMap.Entry<PairInt, Double> entry : S.entrySet()) {
            PairInt key = entry.getKey();
            Double value = entry.getValue();
            if(key.get_first().equals(id) || key.get_second().equals(id))
                DeleteConversion(key.get_first(), key.get_second());
        }

    }

    @Override
    public Double convRateBetween(Integer id1, Integer id2) {

        WriterCSV.Writer.printAudit("convRateBetween");
        String sql = "SELECT rate FROM conversions where fromId = ? and toId = ?";
        try (
                Connection con = managers.DBConnectionManager.getInstance().createConnection();
                PreparedStatement statement = con.prepareStatement(sql)
        ) {
            statement.setInt(1, id1);
            statement.setInt(2, id2);

            ResultSet set = statement.executeQuery();
            if (set.next())
                return set.getDouble("rate");
            return -1.0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1.0;
    }

    @Override
    public void DeleteConversion(Integer id1, Integer id2) {

        WriterCSV.Writer.printAudit("DeleteConversion");
        String sql = "DELETE FROM conversions Where fromId = ? and toId = ?";
        try (
                Connection con = managers.DBConnectionManager.getInstance().createConnection();
                PreparedStatement statement = con.prepareStatement(sql)
        ) {
            statement.setInt(1, id1);
            statement.setInt(2, id2);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void UpdateConversion(Integer id1, Integer id2, Double rate) {
        WriterCSV.Writer.printAudit("updateConversion");
        String sql = "UPDATE conversions Set rate = ? WHERE fromId = ? AND toId = ?";
        try (
                Connection con = managers.DBConnectionManager.getInstance().createConnection();
                PreparedStatement statement = con.prepareStatement(sql)
        ) {
            statement.setDouble(1, rate);
            statement.setInt(2, id1);
            statement.setInt(3, id2);


            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void AddConversion(Integer id1, Integer id2, Double rate) {
        if(convRateBetween(id1, id2) != -1.0) {
            UpdateConversion(id1, id2, rate);
            return;
        }
        String sql  = "INSERT INTO conversions VALUES(?, ?, ?)";

        try (
                Connection con = managers.DBConnectionManager.getInstance().createConnection();
                PreparedStatement statement = con.prepareStatement(sql)
        ) {
            statement.setInt(1, id1);
            statement.setInt(2, id2);
            statement.setDouble(3, rate);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
    @Override
    public Integer findCurrency(Integer id) {
        return null;
    }
}

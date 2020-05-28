package GenericRead;

import Individuals.Conversion;
import Individuals.Currency;
import Individuals.Account;
import Individuals.Client;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

interface GenericParser<T>{
    public T get(String[] components);
}

public class
ReaderCSV {
    public static final ReaderCSV Reader = new ReaderCSV();
    private ReaderCSV() {}
    public ReaderCSV getInstance() {
        return Reader;
    }
    private<T> List<T> read(String filename, GenericParser<T>Parser) {
        ArrayList<T> list = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line = reader.readLine();
            line = reader.readLine();
            while (line != null) {
                var components = line.split(",");
                list.add(Parser.get(components));
                line = reader.readLine();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IndexOutOfBoundsException e){
            System.out.println("Bad format");
            e.printStackTrace();
        }

        return list;
    }

    public List<Currency> loadCurrencies() {
        return read("src/CsvFiles/Currencies.csv", components -> {
            return new Currency(components[0],
                    Integer.parseInt(components[1]));
        });
    }
    public List<Client> loadClients() {
        return read("src/CsvFiles/Clients.csv", components -> {
            return new Client(components[0],
                    Integer.parseInt(components[1]),
                    Integer.parseInt(components[2]));
        });
    }
    public List<Account> loadAccounts() {
        return read("src/CsvFiles/Accounts.csv", components -> {
            return new Account(Integer.parseInt(components[0]),
                    Integer.parseInt(components[1]),
                    Double.parseDouble(components[2]));
        });
    }
    public List<Conversion> loadConversions() {
        return read("src/CsvFiles/Conversions.csv", components -> {
            return new Conversion(Integer.parseInt(components[0]),
                    Integer.parseInt(components[1]),
                    Double.parseDouble(components[2]));
        });
    }
}

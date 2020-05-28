package GenericRead;

import Individuals.Conversion;
import Individuals.Account;
import Individuals.Client;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class WriterCSV {
    public static final WriterCSV Writer = new WriterCSV();

    private WriterCSV() {
    }

    public WriterCSV getInstance() {
        return Writer;
    }

    private <T> void write(String format, List<T> lista, String filename) {
        try {
            FileWriter writer = new FileWriter(filename);
            writer.append(format);


            for (T now : lista) {
                writer.append(now.toString());

            }

            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void printAccounts(List<Account>Accounts) {
        write("CURRENCY ID, BELONGS TO, AMOUNT\n", Accounts, "src/CsvFiles/Accounts.csv");
    }
    public void printClients(List<Client>Clients) {
        write("NAME,AGE,ID,ACCOUNTS\n", Clients, "src/CsvFiles/Clients.csv");
    }
    public void printConversions(List<Conversion>Conversions) {
        write("IdFrom,IdTo,Rate\n", Conversions, "src/CsvFiles/Conversions.csv");
    }

    public void printAudit(String nume_actiune){
        try {
            FileWriter fileWriter = new FileWriter("src/CsvFiles/Audit.csv", true);
            PrintWriter out = new PrintWriter(fileWriter);
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            long threadId = Thread.currentThread().getId();
            out.println(nume_actiune + "," + formatter.format(new Date()) +
                    "," + threadId);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
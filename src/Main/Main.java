package Main;

import Service.BankCsv;
import Individuals.*;
import Service.BankDB;
import Service.BankInterface;

import java.sql.Connection;

enum Type {
    DB, File;
}
public class Main {
    public static void main(String[] arg) {
        BankDB B = (BankDB) BankInterface.build(BankInterface.Type.DB);

        // sau BankCSV B = (BankCSV)BankInterface.build(BankInterface.Type.File) pt CSV
        Client c = new Client("AD", 23, 2);
        
        // executare operatii dorite

        for (int i = 0; i <= 2; ++i) {
            for (int j = 0; j <= 2; ++j) {

                if(i != j) B.AddConversion(i, j, (i + j + 0.2) * 1.3);
            }}
        B.AddConversion(0, 5, 3.8);
        System.out.println(B.convRateBetween(0, 2));
        B.UpdateConversion(0, 2, 2333.3);
    }
}
package xyz.shoesheets.shoesheets;

import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class DataParser {
    private String itemDate, itemType, itemSite, itemName, currentLine;
    private boolean predictedSale;
    private double itemPrice, totalPurchases, totalSales, margins;
    private File file, sales, purchases;

    public DataParser(File f, File p, File s) {
        file = f;
        purchases = p;
        sales = s;
        itemDate = "";
        itemType = "";
        itemSite = "";
        itemName = "";
        currentLine = "";
        itemPrice = 0;
        totalPurchases = 0;
        totalSales = 0;
        margins = 0;
        predictedSale = false;
    }

    public String getItemDate(){
        return itemDate;
    }

    public String getItemType(){
        return itemType;
    }

    public String getItemSite(){
        return itemSite;
    }

    public String getItemName(){
        return itemName;
    }

    public String getCurrentLine(){
        return currentLine;
    }

    public boolean getPredictedSale(){
        return predictedSale;
    }

    public double getItemPrice(){
        return itemPrice;
    }

    private void parsePurchase(String line) {
        currentLine = line;
        int i = 1;
        while (currentLine.length() > 0) {
            switch (i) {
                case 1:
                    itemDate = currentLine.substring(0, currentLine.indexOf("|"));
                    break;
                case 2:
                    itemType = currentLine.substring(1, currentLine.indexOf("|"));
                    break;
                case 3:
                    itemSite = currentLine.substring(1, currentLine.indexOf("|"));
                    break;
                case 4:
                    itemName = currentLine.substring(1, currentLine.indexOf("|"));
                    break;
                case 5:
                    itemPrice = Double.valueOf(currentLine.substring(1, currentLine.indexOf("?")));
                    break;
                default:
                    currentLine = "";
                    break;
            }
            i++;
        }
    }

    private void parseSale(String line, boolean wantPredictedSales) {
        currentLine = line;
        int i = 1;
        while (currentLine.length() > 0) {
            if(!(currentLine.substring(0, 1).equals("P") && wantPredictedSales))
                currentLine = currentLine.substring(1);
            switch (i) {
                case 1:
                    itemDate = currentLine.substring(0, currentLine.indexOf("|"));
                    break;
                case 2:
                    itemType = currentLine.substring(1, currentLine.indexOf("|"));
                    break;
                case 3:
                    itemSite = currentLine.substring(1, currentLine.indexOf("|"));
                    break;
                case 4:
                    itemName = currentLine.substring(1, currentLine.indexOf("|"));
                    break;
                case 5:
                    itemPrice = Double.valueOf(currentLine.substring(1, currentLine.indexOf("?")));
                    break;
                default:
                    currentLine = "";
                    break;
            }
            i++;
        }
    }

    public double calculateMargins(double p, double s, File m) {
        Scanner purchasesInput;
        try {
            purchasesInput = new Scanner(sales);
            while (purchasesInput.hasNextLine()) {
                currentLine = purchasesInput.nextLine();
                if (currentLine.substring(0, 1).equals("P"))
                    continue;
                totalPurchases += Double.parseDouble(currentLine.substring(currentLine.lastIndexOf("|") + 1, currentLine.indexOf("?")));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Scanner salesInput;
        try {
            salesInput = new Scanner(sales);
            while (salesInput.hasNextLine()) {
                currentLine = salesInput.nextLine();
                if (currentLine.substring(0, 1).equals("P"))
                    continue;
                totalSales += Double.parseDouble(currentLine.substring(currentLine.lastIndexOf("|") + 1, currentLine.indexOf("?")));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        margins = totalSales - totalPurchases;
        return margins;
    }

    public double calculatePredictedMargins(double p, double s, File m) {
        Scanner purchasesInput;
        try {
            purchasesInput = new Scanner(sales);
            while (purchasesInput.hasNextLine()) {
                currentLine = purchasesInput.nextLine();
                if (currentLine.substring(0, 1).equals("P"))
                    continue;
                totalPurchases += Double.parseDouble(currentLine.substring(currentLine.lastIndexOf("|") + 1, currentLine.indexOf("?")));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Scanner salesInput;
        try {
            salesInput = new Scanner(sales);
            while (salesInput.hasNextLine()) {
                currentLine = salesInput.nextLine();
                if (currentLine.substring(0, 1).equals("P"))
                    continue;
                totalSales += Double.parseDouble(currentLine.substring(currentLine.lastIndexOf("|") + 1, currentLine.indexOf("?")));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        margins = totalSales - totalPurchases;
        return margins;
    }
}
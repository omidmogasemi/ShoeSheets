package xyz.shoesheets.shoesheets;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    public static File margins,
            purchaseDate, purchaseType, purchaseItemName, purchaseSite, purchasePrice,
            saleDate, saleType, saleItemName, saleSite, salePrice, predictedSales;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // creates a static sharedpreferences so variables stored in phone memory can be accessed by all other classes
        // can be accessed by MainActivity.SharedPreferences
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean firstStart = prefs.getBoolean("firstStart", true);
        SharedPreferences.Editor editor = prefs.edit();

        // check if the app has been opened before
        if (firstStart) {
            createStartVariables();
            editor.putBoolean("firstStart", false);
            editor.apply();
        }

        initializeStorageFiles();
    }

    private void createStartVariables() {
        // test to ensure the first start check is currently working
        new AlertDialog.Builder(this).setTitle("Test").setMessage("only once").setPositiveButton("ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        })
                .create().show();
    }

    // Opens up the files from their proper directory (or creates them if they do not exist)
    private void initializeStorageFiles() {
        Context ctx = getApplicationContext();
        margins = new File(ctx.getFilesDir(), "margins");
        purchaseDate = new File(ctx.getFilesDir(), "purchaseDate");
        purchaseType = new File(ctx.getFilesDir(), "purchaseType");
        purchaseItemName = new File(ctx.getFilesDir(), "purchaseItemName");
        purchaseSite = new File(ctx.getFilesDir(), "purchaseSite");
        purchasePrice = new File(ctx.getFilesDir(), "purchasePrice");
        saleDate = new File(ctx.getFilesDir(), "saleDate");
        saleType = new File(ctx.getFilesDir(), "saleDate");
        saleItemName = new File(ctx.getFilesDir(), "saleItemName");
        saleSite = new File(ctx.getFilesDir(), "saleSite");
        salePrice = new File(ctx.getFilesDir(), "salePrice");
    }
}

/*
HOW TO PROPERLY OPEN, WRITE INTO, AND READ FROM A FILE ON THE ANDROID DEVICE'S INTERNAL STORAGE
        Context ctx = getApplicationContext();
        margins = new File(ctx.getFilesDir(), "margins");
        String fileContents = "Hello World!";
        FileOutputStream outputStream;
        try{
            BufferedWriter out = new BufferedWriter(
                    new FileWriter(margins, true));
            out.write(fileContents);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Scanner input = null;
        try {
            input = new Scanner(margins);
            while (input.hasNextLine()){
                Log.d("IT_WORKS", input.nextLine());
            }
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        }
 */

/*
In order to open a file, just do it normally like as if you were creating a file.
Then to append to it, call Buffered Writer like shown above and make sure that it's set onto true.
 */
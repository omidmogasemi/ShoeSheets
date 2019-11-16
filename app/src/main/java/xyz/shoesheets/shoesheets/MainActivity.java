package xyz.shoesheets.shoesheets;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    public static File margins, sales, purchases;
    static final int PICK_REQUEST = 0;

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
        initializeSalesInfo();
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
        purchases = new File(ctx.getFilesDir(), "purchases");
        sales = new File(ctx.getFilesDir(), "sales");
        // File destination: /storage/emulated/0/purchases
    }

    public void initializeSalesInfo() {
        TextView paView = (TextView)findViewById(R.id.purchasesAmount);
        TextView asaView = (TextView)findViewById(R.id.actualSalesAmount);
        TextView pmView = (TextView)findViewById(R.id.profitMarginAmount);
        DataParser parser = new DataParser();
        if (purchases.length() == 0) {
            paView.setText("$" + 0.00);
            asaView.setText("$" + 0.00);
            pmView.setText("$" + 0.00);
            return;
        }
        if (sales.length() == 0) {
            asaView.setText("$" + 0.00);
            pmView.setText("$" + 0.00);
        }
        paView.setText("$" + parser.calculatePurchases());
        asaView.setText("$" + parser.calculateSales(false));
        pmView.setText("$" + parser.calculateMargins(false));
    }

    public void onLogSalesClick(View v) {
        Intent myIntent = new Intent(getBaseContext(), LogSales.class);
        // myIntent.putExtra(String name, int value); - allows you to pass extra information into the new activity
        startActivityForResult(myIntent, 0);
    }

    public void onLogPurchasesClick(View v) {
        Intent myIntent = new Intent(getBaseContext(), LogPurchases.class);
        startActivityForResult(myIntent, 0);
    }

    public void onAnalyticsClick(View v) {
        Intent myIntent = new Intent(getBaseContext(), Analytics.class);
        startActivity(myIntent);
    }

    /*
     * callback method when LogSales Activity finishes and closes
     * might need to be edited - proper format has a check for a bad resultCode
     * would not work with that check so i just eliminated it and now it works, but
     * something bad could be happening (in memory maybe?) as a result of this
     * COME BACK TO ADJUST LATER
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_REQUEST) {
            // updates the profit margin info at the top of the MainActivity
            initializeSalesInfo();
        }
    }
}
/*
HOW TO PROPERLY OPEN, WRITE INTO, AND READ FROM A FILE ON THE ANDROID DEVICE'S INTERNAL STORAGE
        // Read in the user's inputted price
        EditText priceEdit;
        priceEdit = (EditText)findViewById(R.id.priceInput);
        String fileContents = priceEdit.getText().toString();

        // writes the user's input to the specified file
        try{
            BufferedWriter out = new BufferedWriter(
                    new FileWriter(MainActivity.purchasePrice, true));
            out.write(fileContents);
            out.write("\n");
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // test to ensure that the file is being properly written to
        Scanner input = null;
        try {
            input = new Scanner(MainActivity.purchasePrice);
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
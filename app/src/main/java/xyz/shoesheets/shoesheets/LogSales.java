package xyz.shoesheets.shoesheets;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.Scanner;

public class LogSales extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private int errorCode;
    private String saleTypeContents = "", saleSiteContents = "", saleBrandContents = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_sales);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        openFiles();
        initializeSpinners();
        checkForErrorCodes();
        EditText dateEdit = (EditText)findViewById(R.id.dateInput);
        dateEdit.setHint("YY-MM-DD");
    }

    private void openFiles(){
        Context ctx = getApplicationContext();
        MainActivity.sales = new File(ctx.getFilesDir(), "sales");
    }

    private void initializeSpinners(){
        Spinner typeSpinner = findViewById(R.id.typeSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.types_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(adapter);
        typeSpinner.setOnItemSelectedListener(this);

        Spinner siteSpinner = findViewById(R.id.sitesSpinner);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.sites_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        siteSpinner.setAdapter(adapter2);
        siteSpinner.setOnItemSelectedListener(this);

        Spinner brandSpinner = findViewById(R.id.brandSpinner);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this, R.array.brands_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        brandSpinner.setAdapter(adapter3);
        brandSpinner.setOnItemSelectedListener(this);
    }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long l) {
            Spinner spinner = (Spinner) parent;
            if (spinner.getId() == R.id.typeSpinner)
                saleTypeContents = parent.getItemAtPosition(pos).toString();
            else if (spinner.getId() == R.id.sitesSpinner)
                saleSiteContents = parent.getItemAtPosition(pos).toString();
            else
                saleBrandContents = parent.getItemAtPosition(pos).toString();
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }

    /*
     * Checks to see if this Intent is the result of a previous error
     * If it was, then return the proper error code to the user for why this error occurred
     */
    private void checkForErrorCodes() {
        int receivedErrorCode;
        EditText dateEdit = (EditText)findViewById(R.id.dateInput);
        EditText itemNameEdit = (EditText)findViewById(R.id.itemNameInput);
        EditText priceEdit = (EditText)findViewById(R.id.priceInput);
        Intent intent = getIntent();
        // fetch the extra intent info passed that contains the error code
        receivedErrorCode = intent.getIntExtra("ERROR_CODE", 0);

        // runs based off of the same error code assignment criteria from validateInput method
        switch (receivedErrorCode) {
            case 1:
                dateEdit.setError("Your date entry MUST be 8 letters following the YY-MM-DD format");
                break;
            case 2:
                itemNameEdit.setError("Your entry MUST contain something.");
                break;
            case 3:
                priceEdit.setError("Your entry MUST contain something.");
                break;
            default:
                return;
        }
    }

    /*
     * Checks the input in the three text boxes
     * If the text input fails the following checks it will automatically go to the checkForErrorCodes()
     * method via a new Intent:
     * The date input MUST be 8 characters AND in a YY-MM-DD format
     * The item name MUST be greater than 0 characters
     * The item price MUST be greater than 0 characters and contain ONLY numbers
     */
    private boolean validateInput(EditText d, EditText n, EditText p) {
        // in the case of a bad date input - NEED TO ADD A YY-MM-DD FORMAT CHECK AS WEL
        if (d.getText().toString().length() != 8) {
            errorCode = 1;
            return false;
        }
        // in the case of a bad item name input
        if (n.getText().toString().length() == 0) {
            errorCode = 2;
            return false;
        }
        // in the case of a bad price input - NEED TO ADD A NUMBER ONLY CHECK
        if (p.getText().toString().length() == 0) {
            errorCode = 3;
            return false;
        }
        return true;
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void onSubmitClick(View v) {
        // Read in the user's inputted price
        EditText dateEdit = (EditText)findViewById(R.id.dateInput);
        EditText itemNameEdit = (EditText)findViewById(R.id.itemNameInput);
        EditText priceEdit = (EditText)findViewById(R.id.priceInput);
        /*
         * Validates the user's input in the three inputtable text boxes
         * If the input is not correct, a new Intent will be launched
        */
        if (!validateInput(dateEdit, itemNameEdit, priceEdit) ) {
            // creates a new intent
            Intent myIntent = new Intent(getBaseContext(), LogSales.class);
            // pass extra intent info that contains the error code
            myIntent.putExtra("ERROR_CODE", errorCode);
            startActivity(myIntent);
            // closes this intent after the newly created intent is done
            finish();
        }
        else {
            String dateContents = dateEdit.getText().toString();
            String itemNameContents = itemNameEdit.getText().toString();
            String priceContents = priceEdit.getText().toString();

            itemNameEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        hideKeyboard(v);
                    }
                }
            });

            priceEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        hideKeyboard(v);
                    }
                }
            });

            // writes the user's input to the specified file
            try {
                BufferedWriter out = new BufferedWriter(
                        new FileWriter(MainActivity.sales, true));
                out.write(dateContents + "|");
                out.write(saleTypeContents + "|");
                out.write(saleSiteContents + "|");
                out.write(saleBrandContents + "|");
                out.write(itemNameContents + "|");
                out.write(priceContents + "?\n");
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // test to ensure that the file is being properly written to
            Scanner input = null;
            try {
                input = new Scanner(MainActivity.sales);
                while (input.hasNextLine()) {
                    Log.d("IT_WORKS", input.nextLine());
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            // return to the MainActivity
            finish();
        }
    }
}
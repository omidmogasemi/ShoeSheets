package xyz.shoesheets.shoesheets;

import android.app.Activity;
import android.content.Context;
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

public class LogSales extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    public String itemTypeContents = "", itemSiteContents = "", itemBrandContents = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_sales);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        openFiles();
        initializeSpinners();
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
                itemTypeContents = parent.getItemAtPosition(pos).toString();
            else if (spinner.getId() == R.id.sitesSpinner)
                itemSiteContents = parent.getItemAtPosition(pos).toString();
            else
                itemBrandContents = parent.getItemAtPosition(pos).toString();
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void onSubmitClick(View v) {
        // Read in the user's inputted price
        EditText itemNameEdit = (EditText)findViewById(R.id.itemNameInput);
        EditText priceEdit = (EditText)findViewById(R.id.priceInput);
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
        try{
            BufferedWriter out = new BufferedWriter(
                    new FileWriter(MainActivity.sales, true));
            out.write(itemTypeContents + "|");
            out.write(itemSiteContents + "|");
            out.write(itemBrandContents + "|");
            out.write(itemNameContents + "|");
            out.write(priceContents);
            // out.write("\n");
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // test to ensure that the file is being properly written to
        Scanner input = null;
        try {
            input = new Scanner(MainActivity.sales);
            while (input.hasNextLine()){
                Log.d("IT_WORKS", input.nextLine());
            }
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        }
        // return to the MainActivity
        finish();
    }

}

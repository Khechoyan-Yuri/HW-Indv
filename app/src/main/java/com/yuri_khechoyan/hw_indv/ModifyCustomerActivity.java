package com.yuri_khechoyan.hw_indv;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class ModifyCustomerActivity extends Activity implements OnClickListener {

    //Declaring Variables
    private EditText nameText;
    private EditText numberText;
    private Button updateBtn, deleteBtn;

    private long _id;
    private String name;
    private String phonenum;

    //Declaring DBManager object
    private DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Setting title for the Modification window
        setTitle("Modify User Info");

        setContentView(R.layout.activity_modify_record);

        //Initializes DB object
        dbManager = new DBManager(this);
        //Opens the DB object
        dbManager.open();

        //Initialize variables & connect variables to layout elements
        nameText = (EditText) findViewById(R.id.name_edittext);
        numberText = (EditText) findViewById(R.id.phnumber_edittext);

        //Initialize Buttons & connects them to layout elements
        updateBtn = (Button) findViewById(R.id.btn_update);
        deleteBtn = (Button) findViewById(R.id.btn_delete);

        //Stores inputted data into Intent
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String name = intent.getStringExtra("name");
        String phnum = intent.getStringExtra("phnum");

        //Parses inputted data for id
        _id = Long.parseLong(id);

        //Sets text inside user info modification window
        nameText.setText(name);
        numberText.setText(phnum);

        //Create OnClickListeners for the buttons (update & delete)
        updateBtn.setOnClickListener(this);
        deleteBtn.setOnClickListener(this);
    }

    //Method called when 'Update' or 'Delete' buttons are pressed
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //Update button
            case R.id.btn_update:
                String name = nameText.getText().toString();
                String number = numberText.getText().toString();

                //Update information in ListView
                dbManager.update(_id, name, number);
                this.returnHome();
                break;

            //Delete button
            case R.id.btn_delete:
                dbManager.delete(_id);
                this.returnHome();
                break;
        }
    }

    //Method called when 'Update' or 'Delete' buttons are pressed
    //closes window and goes back to CustomerListActivity
    public void returnHome() {
        Intent home_intent = new Intent(getApplicationContext(), CustomerListActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(home_intent);
    }
}

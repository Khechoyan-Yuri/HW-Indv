package com.yuri_khechoyan.hw_indv;

//##########################################################################
/*	PROGRAM TITLE: HW-Indv (qu·eu·ed)
    AUTHOR: Yuri Khechoyan
    COURSE NUMBER: CIS 472
    COURSE SECTION NUMBER: 01
    INSTRUCTOR NAME: Dr. Tian
    PROJECT NAME: Individual PROJECT #1 (Final)
    PRODUCTION NUMBER: HW-Indv-1F
    DUE DATE: 05/5/2017
//##########################################################################

SUMMARY

    This program is designed to be a Queue Based Application.
    When app is launched, user will be able to add themselves to the Queue
    by inputting their name & phone number.
    The Phone number would (theoretically) would be used in order to
    When inside of the account, the end user has the ability to create tasks that
    would be completed by other users (called Dundies).
    The end-user that creates tasks, has the ability to:

        -Set a title for the task
        -Provide details (if title is not self-explanatory)
        -Set location where the task(s) need to take place (University campus)*
        -Set the payment amount for this task that needs to be completed.**


							------WHEN TASK IS SUBMITTED-----

    The Dundie(s) have the ability to either accept or decline the tasks that are posted
    App will also let the end-user know who is currently handling the task (if task is accepted) (future implementation)


        *  This app will mainly be used by University students. Therefore, typical location
           for tasks would be the University at which the student resides/attends. (line 25)

        ** The minimum required payment is $5. The end-user that is creating the task can
           set a higher payment amount (based on task difficulty & end-user's discretion).
           Tips may also be applied (if the end-user so chooses) (line 26) (future implementation?)


//##########################################################################

INPUT

        -Task Name
        -Task Details (if needed)
        -Task Location (Required)
        -Payment Amount for Task (minimum = $5; Tips are optional)

//##########################################################################

OUTPUT

    Application will post newly created tasks to the overall Task board (based on GPS location & radius)
    When Task is completed, it is removed

//##########################################################################

ASSUMPTIONS

- App has to have a stable WiFi or Cellular Connection (3G/4G/LTE, etc.)
- End-user has the ability to fill out the EditText fields properly
- The dundie has the ability to carry out & complete the tasks that they accept
*/
//##########################################################################

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

public class CustomerListActivity extends ActionBarActivity {

    //Initialize variables
    private DBManager dbManager;
    private ListView listView;
    private SimpleCursorAdapter adapter;
    //Value used to check if SEND_SMS Permission is Granted or Denied
    int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;

    //Create an Array of parameters that are needed for the output to the user
    //This is what the end user will see - ID number, First Name, Last Name
    final String[] from = new String[]{DatabaseHelper._ID,
            DatabaseHelper.NAME, DatabaseHelper.PHONENUM};

    //Connect each variable to their respected layout elements
    final int[] to = new int[]{R.id.id, R.id.name, R.id.number};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //Checks condition if the SEND_SMS Permission is Granted or Denied
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            //If Permission is denied, initialize a window that asks for Permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS},
                    MY_PERMISSIONS_REQUEST_SEND_SMS);


        }

        //Set default text when database is empty
        setContentView(R.layout.fragment_emp_list);

        //Creates new instance of dbManager so that app can retrieve info (info is null)
        dbManager = new DBManager(this);
        dbManager.open();
        Cursor cursor = dbManager.fetch();


        //Create ListView element & connect it to the ListView element inside XML
        listView = (ListView) findViewById(R.id.list_view);
        listView.setEmptyView(findViewById(R.id.empty));

        //Creates adapter that would auto update when new entry (Customer) is added
        adapter = new SimpleCursorAdapter(this, R.layout.activity_view_record, cursor, from, to, 0);
        //Update ListView
        adapter.notifyDataSetChanged();
        //Connect ListView to the Adapter
        listView.setAdapter(adapter);

        // OnCLickListener For List Items
        //This will be invoked when an Item in the ListView is selected
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long viewId) {

                //Connects variables to layout elements
                TextView idTextView = (TextView) view.findViewById(R.id.id);
                TextView nameTextView = (TextView) view.findViewById(R.id.name);
                TextView numberTextView = (TextView) view.findViewById(R.id.number);

                //Retrieves input from fields & converts to String
                String id = idTextView.getText().toString();
                String name = nameTextView.getText().toString();
                String number = numberTextView.getText().toString();

                //Launches the ModifyCountryActivity class through an Intent - so user can then edit info
                Intent modify_intent = new Intent(getApplicationContext(), ModifyCustomerActivity.class);
                modify_intent.putExtra("name", name);
                modify_intent.putExtra("number", number);
                modify_intent.putExtra("id", id);

                startActivity(modify_intent);
            }
        });
    }


    //Creates the '+' button in ActionBar for user to press
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    //When the '+' button is pressed, it invokes the AddCustomerActivity
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.add_record) {

            //Launches AddCustomerActivity through Intent
            Intent add_mem = new Intent(this, AddCustomerActivity.class);
            startActivity(add_mem);
        }
        return super.onOptionsItemSelected(item);
    }
}
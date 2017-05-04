package com.yuri_khechoyan.hw_indv;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

//import com.twilio.Twilio;
//import com.twilio.rest.api.v2010.account.Message;
//import com.twilio.type.PhoneNumber;

import static android.content.ContentValues.TAG;

public class AddCustomerActivity extends Activity implements OnClickListener {

    //Set-up: accessing the Twilio account that will be used
    //Find your Account SID and Token at twilio.com/user/account
    //private static final String ACCOUNT_SID = "AC25715ec0e85d62fdaf2f3f5d176f4f5a";
    //private static final String AUTH_TOKEN = "d711dc1eb4af9bf60e514ae231be8bfd";

    //Initialize Buttons & EditText Fields
    private Button addBtn;
    private EditText customerEditText;
    private EditText phNumEditText;
    private int position;

    //Create instance of DBManager Object
    private DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Initialize position variable
        position = 1;

        //Initializing SID & Authentication token for usage
        //Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        //Sets Title 'Register' for Registration window - '+' button in ActionBar is pressed
        setTitle("Register");

        //Sets the content View to add_activity_record
        setContentView(R.layout.activity_add_record);

        //Connects initialized variables to proper layout elements
        customerEditText = (EditText) findViewById(R.id.name_edittext);
        phNumEditText = (EditText) findViewById(R.id.phonenumber_edittext);
        addBtn = (Button) findViewById(R.id.add_record);

        //Creates a new instance of dbManager when 'Add Record' button is pressed
        dbManager = new DBManager(this);
        dbManager.open();
        addBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_record:

                //Connects variables to layout elements
                customerEditText = (EditText) findViewById(R.id.name_edittext);
                phNumEditText = (EditText) findViewById(R.id.phonenumber_edittext);

                Log.d(TAG, "onClick: " +customerEditText);

                //Extracts info from EditText fields
                final String name = customerEditText.getText().toString();
                final String phonenum = phNumEditText.getText().toString();

                //Adds newly given information & adds them to the the SQLite Database
                dbManager.insert(name, phonenum);


                /*

                //Twilio code in order to send confirmation text message
                //Create message for the user - sent to their mobile numbers' (unformatted)
                Message message = Message.creator(new PhoneNumber("+1" + phonenum),
                        //Number from Twilio - from number (unformatted)
                        new PhoneNumber("+13146268325"),
                        "Thank you for registering with Company, " +phonenum + "! You are "
                                + "currently (" + (position) + ")" + " in line. When you reach position (1) in line, "
                                + "please wait for your name to be called. "
                                + "Thank you for being patient with us!\n\n"
                                + " -Company").create();
                System.out.println(message.getSid());

                //Increments position - so that next person can
                //receive their current position in queue
                position++;

                */


                //Throw toast for verification
                Toast.makeText(this, "Submission Complete! Confirmation is being sent", Toast.LENGTH_LONG).show();

                //While verification toast is thrown, app goes back to the ListActivity View (main menu)
                Intent main = new Intent(AddCustomerActivity.this, CustomerListActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                startActivity(main);
                break;
        }
    }

}
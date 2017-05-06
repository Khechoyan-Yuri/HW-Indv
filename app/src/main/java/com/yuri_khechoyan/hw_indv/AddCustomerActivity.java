package com.yuri_khechoyan.hw_indv;

import android.Manifest;
import android.app.Activity;
import android.app.ListActivity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.telephony.cdma.*;
import android.telephony.SmsMessage;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

//import com.twilio.Twilio;
//import com.twilio.rest.api.v2010.account.Message;
//import com.twilio.type.PhoneNumber;

import static android.R.attr.phoneNumber;
import static android.R.id.message;
import static android.content.ContentValues.TAG;

public class AddCustomerActivity extends Activity implements OnClickListener {

    //Set-up: accessing the Twilio account that will be used
    //Find your Account SID and Token at twilio.com/user/account
    //private static final String ACCOUNT_SID = "AC25715ec0e85d62fdaf2f3f5d176f4f5a";
    //private static final String AUTH_TOKEN = "d711dc1eb4af9bf60e514ae231be8bfd";

    //Declare Variables
    private Button addBtn;
    private EditText customerEditText;
    private EditText phNumEditText;
    //Declare Receipt for checking if SMS has been Sent & Delivered
    String SENT = "SMS_SENT";
    String DELIVERED = "SMS-DELIVERED";
    //PendingIntents needed in order to Broadcast - Sent, Delivered
    PendingIntent sentPI, deliveredPI;
    //Create Broadcast Receiver
    BroadcastReceiver smsSentReceiver, smsDeliveredReceiver;


    //Create instance of DBManager Object
    private DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Initialize Pending Intent (SENT)
        sentPI = PendingIntent.getBroadcast(this, 0, new Intent(SENT), 0);
        //Initialize Pending Intent (DELIVERED)
        deliveredPI = PendingIntent.getBroadcast(this, 0, new Intent(DELIVERED), 0);

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
        //Connects variables to layout elements
        customerEditText = (EditText) findViewById(R.id.name_edittext);
        phNumEditText = (EditText) findViewById(R.id.phonenumber_edittext);
        //Extracts info from EditText fields
        final String name = customerEditText.getText().toString();
        final String phonenum = phNumEditText.getText().toString();

        //If Statements - Check contents of String Variables
        //Both Fields are empty
        if(name.equals("") && phonenum.equals("")){
            //Throw toast
            Toast.makeText(this, "BOTH FIELDS ARE EMPTY!", Toast.LENGTH_SHORT).show();
        }
        //Name Field is empty
        else if (name.equals("")){
            Toast.makeText(this, "NAME WAS NOT ENTERED!", Toast.LENGTH_SHORT).show();
        }
        //Phone number Field is empty
        else if (phonenum.equals("")){
            Toast.makeText(this, "PHONE NUMBER WAS NOT ENTERED!", Toast.LENGTH_SHORT).show();
        }
        //If Both Fields have content, proceed with adding Customer to DB
        else{
            switch (v.getId()) {
                case R.id.add_record:


                    Log.d(TAG, "onClick: " + customerEditText.getText());



                    //Adds newly given information & adds them to the the SQLite Database
                    dbManager.insert(name, phonenum);

                    //Calls Method to send SMS text Message Confirmation
                    sendSMSConfirm();



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

                */

                    //Throw toast for verification
                    Toast.makeText(this, "Submission Complete! Confirmation is being sent to " +name
                            +"'s phone: " + phonenum, Toast.LENGTH_LONG).show();

                    //While verification toast is thrown, app goes back to the ListActivity View (main menu)
                    Intent main = new Intent(AddCustomerActivity.this, CustomerListActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    startActivity(main);
                    break;
            }
        }
    }


    //Method for sending SMS Text message to user after registration is complete
    public void sendSMSConfirm(){


        //Extracts info from EditText fields
        final String name = customerEditText.getText().toString();
        final String phonenum = phNumEditText.getText().toString();

        //Create pre-defined message for SMS
        String message = "Confirmed! Thank you for registering with [Company], " +name +
                "! Please be patient with us. Your name will be called. \n\n - [Company]";


        //Log.d("AddCustomerActivity", message);
       /*
        //Checks condition if the SEND_SMS Permission is Granted or Denied
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED){

            //If Permission is denied, initialize a window that asks for Permission
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.SEND_SMS},
                    MY_PERMISSIONS_REQUEST_SEND_SMS);

            Log.d("AddCustomer", "Not Send SMS: " + message);
        }
        else{
*/

            //If Permission is Granted/Initially enabled, send the Confirm SMS
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phonenum, null, message, sentPI, deliveredPI);
            Log.d("AddCustomer", "Sent SMS: " + message);


    }

    @Override
    protected void onResume() {
        super.onResume();

        //Create new Instance of BroadcastReceiver - Sent
        smsSentReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                //Switch statement that checks for BroadcastReceiver activity (success)
                switch (getResultCode()){
                    //Verification Toast that BroadcastReceiver sent the SMS
                    case Activity.RESULT_OK:
                        Toast.makeText(AddCustomerActivity.this, "SMS was Sent!",
                                Toast.LENGTH_SHORT).show();
                        break;

                    //Verification Toast that BroadcastReceiver
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(AddCustomerActivity.this, "SMS Failed to Send!",
                                Toast.LENGTH_SHORT).show();
                        break;

                    //Case - No Cellular Service
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(AddCustomerActivity.this, "No Service Available!",
                                Toast.LENGTH_SHORT).show();
                        break;

                    //Case - NULL PDU
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(AddCustomerActivity.this, "NULL PDU!",
                                Toast.LENGTH_SHORT).show();
                        break;

                    //Case - Radio Off - Other possible Errors
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(AddCustomerActivity.this, "Radio OFF!",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };

        //Create new Instance of BroadcastReceiver - Delivered
        smsDeliveredReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                //Switch statement that checks for BroadcastReceiver activity (Failure)
                switch (getResultCode()){
                    //Verification Toast that BroadcastReceiver delivered the SMS
                    case Activity.RESULT_OK:
                        Toast.makeText(AddCustomerActivity.this, "SMS was Delivered!",
                                Toast.LENGTH_SHORT).show();
                        break;

                    //Verification Toast that BroadcastReceiver DID NOT deliver the SMS
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(AddCustomerActivity.this, "SMS NOT Delivered!",
                                Toast.LENGTH_SHORT).show();
                        break;
                }

            }
        };

        //Registering the BroadcastReceivers
        registerReceiver(smsSentReceiver, new IntentFilter(SENT));
        registerReceiver(smsDeliveredReceiver, new IntentFilter(DELIVERED));
    }

    @Override
    protected void onPause() {
        super.onPause();

        //Unregister the BroadcastReceivers
        unregisterReceiver(smsSentReceiver);
        unregisterReceiver(smsDeliveredReceiver);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();


    }


}












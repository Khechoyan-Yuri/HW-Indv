package com.yuri_khechoyan.hw_indv;

//
//import android.Manifest;
//import android.app.Activity;
//import android.app.ListActivity;
//import android.app.PendingIntent;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.content.pm.PackageManager;
//import android.database.sqlite.SQLiteDatabase;
//import android.os.Bundle;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.content.ContextCompat;
//import android.telephony.SmsManager;
//import android.telephony.cdma.*;
//import android.telephony.SmsMessage;
//
//import android.text.style.UpdateAppearance;
//import android.util.Log;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//import android.widget.Toast;
//
//
////import com.twilio.Twilio;
////import com.twilio.rest.api.v2010.account.Message;
////import com.twilio.type.PhoneNumber;
//
//import static android.R.attr.phoneNumber;
//import static android.R.id.message;
//import static android.content.ContentValues.TAG;
//
//public class SendSMSDeleteActivity extends Activity implements OnClickListener {
//
//
//
//    //Used for the place in queue
//    long UpdatePlace;
//
//    //Declare Receipt for checking if SMS has been Sent & Delivered
//    String SENT = "SMS_SENT";
//    String DELIVERED = "SMS-DELIVERED";
//    //PendingIntents needed in order to Broadcast - Sent, Delivered
//    PendingIntent sentPI, deliveredPI;
//    //Create Broadcast Receiver
//    BroadcastReceiver smsSentReceiver, smsDeliveredReceiver;
//
//
//    //Declare instance of DBManager Object
//    private DBManager dbManager;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        //Connecting UpdatePlace Variable to placeDel
//        //variable created in DBManager.delete() method
//        UpdatePlace = dbManager.placeDel;
//
//
//        //Initialize Pending Intent (SENT)
//        sentPI = PendingIntent.getBroadcast(this, 0, new Intent(SENT), 0);
//        //Initialize Pending Intent (DELIVERED)
//        deliveredPI = PendingIntent.getBroadcast(this, 0, new Intent(DELIVERED), 0);
//
//        //Creates a new instance of dbManager when 'Add Record' button is pressed
//        dbManager = new DBManager(this);
//        dbManager.open();
//    }
//
//    @Override
//    public void onClick(View v) {
//
//
//        if(dbManager.placeDel == 1){
//
//
//
//
//            //Throw toast
//            Toast.makeText(this, "Customer Deleted!", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    //Method for sending SMS Text message to user after registration is complete
//    public void sendSMSConfirm(){
//
//
//        //Create pre-defined message for SMS
//        String message = "Confirmed! Thank you for registering with [Company], " +name +
//                "! You are currently (" +UpdatePlace + ") in line. Please be patient with us." +
//                " Your name will be called. \n\n - [Company]";
//
//
//        //If Permission is Granted/Initially enabled, send the Confirm SMS
//        SmsManager smsManager = SmsManager.getDefault();
//        smsManager.sendTextMessage(phonenum, null, message, sentPI, deliveredPI);
//        Log.d("AddCustomer", "Sent SMS: " + message);
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        //Create new Instance of BroadcastReceiver - Sent
//        smsSentReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//
//                //Switch statement that checks for BroadcastReceiver activity (success)
//                switch (getResultCode()){
//                    //Verification Toast that BroadcastReceiver sent the SMS
//                    case Activity.RESULT_OK:
//                        Toast.makeText(SendSMSDeleteActivity.this, "SMS was Sent!",
//                                Toast.LENGTH_SHORT).show();
//                        break;
//
//                    //Verification Toast that BroadcastReceiver
//                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
//                        Toast.makeText(SendSMSDeleteActivity.this, "SMS Failed to Send!",
//                                Toast.LENGTH_SHORT).show();
//                        break;
//
//                    //Case - No Cellular Service
//                    case SmsManager.RESULT_ERROR_NO_SERVICE:
//                        Toast.makeText(SendSMSDeleteActivity.this, "No Service Available!",
//                                Toast.LENGTH_SHORT).show();
//                        break;
//
//                    //Case - NULL PDU
//                    case SmsManager.RESULT_ERROR_NULL_PDU:
//                        Toast.makeText(SendSMSDeleteActivity.this, "NULL PDU!",
//                                Toast.LENGTH_SHORT).show();
//                        break;
//
//                    //Case - Radio Off - Other possible Errors
//                    case SmsManager.RESULT_ERROR_RADIO_OFF:
//                        Toast.makeText(SendSMSDeleteActivity.this, "Radio OFF!",
//                                Toast.LENGTH_SHORT).show();
//                        break;
//                }
//            }
//        };
//
//        //Create new Instance of BroadcastReceiver - Delivered
//        smsDeliveredReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//
//                //Switch statement that checks for BroadcastReceiver activity (Failure)
//                switch (getResultCode()){
//                    //Verification Toast that BroadcastReceiver delivered the SMS
//                    case Activity.RESULT_OK:
//                        Toast.makeText(SendSMSDeleteActivity.this, "SMS was Delivered!",
//                                Toast.LENGTH_SHORT).show();
//                        break;
//
//                    //Verification Toast that BroadcastReceiver DID NOT deliver the SMS
//                    case Activity.RESULT_CANCELED:
//                        Toast.makeText(SendSMSDeleteActivity.this, "SMS NOT Delivered!",
//                                Toast.LENGTH_SHORT).show();
//                        break;
//                }
//            }
//        };
//
//        //Registering the BroadcastReceivers
//        registerReceiver(smsSentReceiver, new IntentFilter(SENT));
//        registerReceiver(smsDeliveredReceiver, new IntentFilter(DELIVERED));
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//
//        //Unregister the BroadcastReceivers
//        unregisterReceiver(smsSentReceiver);
//        unregisterReceiver(smsDeliveredReceiver);
//    }
//
//    @Override
//    protected void onDestroy(){
//        super.onDestroy();
//    }
//}
//

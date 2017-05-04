package com.yuri_khechoyan.hw_indv;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
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

    //Create an Array of parameters that are needed for the output to the user
    //This is what the end user will see - ID number, First Name, Last Name
    final String[] from = new String[] { DatabaseHelper._ID,
            DatabaseHelper.NAME, DatabaseHelper.PHONENUM };

    //Connect each variable to their respected layout elements
    final int[] to = new int[] { R.id.id, R.id.name, R.id.number };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
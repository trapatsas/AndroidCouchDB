package com.example.ptrapatsas.hellocouch;

import android.app.Activity;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.couchbase.lite.*;
import com.couchbase.lite.android.AndroidContext;
import com.couchbase.lite.util.Log;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;


public class MyActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        // My code

        WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        Log.d("wifiInfo", wifiInfo.toString());
        Log.d("SSID",wifiInfo.getSSID());

        final String TAG = "HelloWorld";
        Log.d(TAG, "Begin Hello World App");
        TextView mainTxtView = (TextView) findViewById(R.id.txtView01);
        mainTxtView.setMovementMethod(new ScrollingMovementMethod());

        mainTxtView.append("\n" +
                " -2. wifiInfo: " + wifiInfo.toString());
        mainTxtView.append("\n" +
                " -1. SSID: " + wifiInfo.getSSID());
        mainTxtView.append("\n 0. Hello your ip: is" + Utils.getIPAddress(true));
        mainTxtView.append("\n 1. Begin Hello World App");
        // create a manager
        Manager manager;
        try {
            manager = new Manager(new AndroidContext(this), Manager.DEFAULT_OPTIONS);
            Log.d (TAG, "Manager created");
            mainTxtView.append("\n" +
                    " 2. Manager created");

        } catch (IOException e) {
            Log.e(TAG, "Cannot create manager object");
            return;
        }
        // create a name for the database and make sure the name is legal
        String dbname = "hello";
        if (!Manager.isValidDatabaseName(dbname)) {
            Log.e(TAG, "Bad database name");
            return;
        }
        // create a new database
        Database database;
        try {
            database = manager.getDatabase(dbname);
            Log.d (TAG, "");
            mainTxtView.append("\n" +
                    " 3. Database created");

        } catch (CouchbaseLiteException e) {
            Log.e(TAG, "Cannot get database");
            return;
        }
        // get the current date and time
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        Calendar calendar = GregorianCalendar.getInstance();
        String currentTimeString = dateFormatter.format(calendar.getTime());
        // create an object that contains data for a document
        Map<String, Object> docContent = new HashMap<String, Object>();
        docContent.put("message", "Hello Couchbase Lite");
        docContent.put("creationDate", currentTimeString);
        // display the data for the new document
        Log.d(TAG, "docContent=" + String.valueOf(docContent));
        mainTxtView.append("\n" +
                " 4. docContent=" + String.valueOf(docContent));

        // create an empty document
        Document document = database.createDocument();
        // add content to document and write the document to the database
        try {
            document.putProperties(docContent);
            Log.d (TAG, "Document written to database named " + dbname + " with ID = " + document.getId());
            mainTxtView.append("\n" +
                    " 5. Document written to database named " + dbname + " with ID = " + document.getId());

        } catch (CouchbaseLiteException e) {
            Log.e(TAG, "Cannot write document to database", e);
        }
        // save the ID of the new document
        String docID = document.getId();
        // retrieve the document from the database
        Document retrievedDocument = database.getDocument(docID);
        // display the retrieved document
        Log.d(TAG, "retrievedDocument=" + String.valueOf(retrievedDocument.getProperties()));
        mainTxtView.append("\n" +
                " 6. retrievedDocument=" + String.valueOf(retrievedDocument.getProperties()));

        // update the document
        Map<String, Object> updatedProperties = new HashMap<String, Object>();
        updatedProperties.putAll(retrievedDocument.getProperties());
        updatedProperties.put ("message", "We're having a heat wave!");
        updatedProperties.put ("temperature", "95");
        try {
            retrievedDocument.putProperties(updatedProperties);
            Log.d(TAG, "updated retrievedDocument=" + String.valueOf(retrievedDocument.getProperties()));
            mainTxtView.append("\n" +
                    " 7. updated retrievedDocument=" + String.valueOf(retrievedDocument.getProperties()));

        } catch (CouchbaseLiteException e) {
            Log.e (TAG, "Cannot update document", e);
        }
        // delete the document
        try {
            retrievedDocument.delete();
            Log.d (TAG, "Deleted document, deletion status = " + retrievedDocument.isDeleted());
            mainTxtView.append("\n" +
                    " 8. Deleted document, deletion status = " + retrievedDocument.isDeleted());

        } catch (CouchbaseLiteException e) {
            Log.e (TAG, "Cannot delete document", e);
        }
        Log.d(TAG, "End Hello World App");
        mainTxtView.append("\n" +
                " 9. End Hello World App");

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }
}

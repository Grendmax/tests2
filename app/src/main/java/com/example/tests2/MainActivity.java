package com.example.tests2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.telephony.SmsManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final int REQ_CODE = 1;
    EditText input;
    Spinner spinner;
    Button button;

    Map<String, Integer> SIM_map;
    ArrayList<String> simcardNames;

    TelephonyManager telephonyManager;
    TelephonyManager.UssdResponseCallback ussdResponseCallback;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        input = findViewById(R.id.input);
        spinner = findViewById(R.id.spinner);
        button = findViewById(R.id.submit);

        SIM_map = new HashMap<>();
        simcardNames = new ArrayList<>();
        requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE},REQ_CODE);
        telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        ussdResponseCallback = new TelephonyManager.UssdResponseCallback() {
            @Override
            public void onReceiveUssdResponse(TelephonyManager telephonyManager, String request, CharSequence response) {

                Toast.makeText(MainActivity.this, response, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onReceiveUssdResponseFailed(TelephonyManager telephonyManager, String request, int failureCode) {

                Toast.makeText(MainActivity.this, failureCode, Toast.LENGTH_SHORT).show();

            }
        };

        final Handler handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg); //no need to change anything here
            }
        };

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CALL_PHONE},REQ_CODE);

                }
                String selected_SIM=spinner.getSelectedItem().toString();
                telephonyManager.createForSubscriptionId(SIM_map.get(selected_SIM)).sendUssdRequest(input.getText().toString()
                        ,ussdResponseCallback,handler);


            }
        });
    }

    private void sims_details() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE},REQ_CODE);
        }
        List<SubscriptionInfo> subscriptionInfos = SubscriptionManager.from(getApplicationContext()).getActiveSubscriptionInfoList();

        for(SubscriptionInfo subscriptionInfo:subscriptionInfos){

            SIM_map.put(subscriptionInfo.getCarrierName().toString(),subscriptionInfo.getSubscriptionId());
            simcardNames.add(subscriptionInfo.getCarrierName().toString());
            if (subscriptionInfo.getCarrierName().toString().equals("activ")){
                Toast.makeText(MainActivity.this, "activ", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(MainActivity.this, "altel", Toast.LENGTH_SHORT).show();
            }
        }

        ArrayAdapter arrayAdapter=new ArrayAdapter(MainActivity.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,simcardNames);
        spinner.setAdapter(arrayAdapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            sims_details();
        }
    }




}
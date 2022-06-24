package com.example.tests2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TextView myTextView;
    Button myButton;
    EditText txtMobile;
    EditText txtContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myTextView = (TextView) findViewById(R.id.textView);
        myButton = (Button) findViewById(R.id.button);
        txtMobile = (EditText) findViewById(R.id.editTextMobile);
        txtContent = (EditText) findViewById(R.id.editTextContent);

        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try{
                    SmsManager smgr = SmsManager.getDefault();
                    smgr.sendTextMessage(txtMobile.getText().toString(),null,txtContent.getText().toString(),null,null);
                    Toast.makeText(MainActivity.this, "SMS Sent Successfully", Toast.LENGTH_SHORT).show();
                }
                catch (Exception e){
                    Toast.makeText(MainActivity.this, "SMS Failed to Send, Please try again", Toast.LENGTH_SHORT).show();
                }
            }
        });

        inbox = (Button) findViewById(R.id.inbox);
        list = (ListView) findViewById(R.id.list);
        arlist = new ArrayList<String>();
        inbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri inboxUri = Uri.parse("content://sms/inbox");
                String[] reqCols = {"_id", "body", "address"};
                ContentResolver cr = getContentResolver();
                Cursor cursor = cr.query(inboxUri, reqCols, "address='+919456'", null, null);
                adapter = new SimpleCursorAdapter(getApplicationContext(), R.layout.msg_content_layout, cursor,
                        new String[]{"body", "address"}, new int[]{R.id.txt1, R.id.txt2});
                list.setAdapter(adapter);
            }
        });


    }


}
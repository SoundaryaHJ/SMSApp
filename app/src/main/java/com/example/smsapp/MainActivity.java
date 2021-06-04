package com.example.smsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText message, number;
    int MY_PERMISSION_REQUEST_SEND_SMS = 1;
    String SENT = "SENT_SMS";
    String DELIVERED = "SMS_DELIVERED";
    PendingIntent sentPI , deliveredPI;
    BroadcastReceiver smsSentReceiver , smsDeliveredReceiver;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        message = (EditText) findViewById(R.id.message);
        number = (EditText) findViewById(R.id.number);

        sentPI = PendingIntent.getBroadcast(this, 0, new Intent(SENT), 0);
        deliveredPI = PendingIntent.getBroadcast(this, 0, new Intent(DELIVERED), 0);

    }


    @Override
    protected void onResume() {
        super.onResume();

        smsSentReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(MainActivity.this, "SMS sent!" , Toast.LENGTH_SHORT).show();
                        break;

                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(MainActivity.this, "Generic Failure!" , Toast.LENGTH_SHORT).show();
                        break;

                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(MainActivity.this, "No Service!" , Toast.LENGTH_SHORT).show();
                        break;

                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(MainActivity.this, "Null PDU!" , Toast.LENGTH_SHORT).show();
                        break;

                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(MainActivity.this, "Radio off!" , Toast.LENGTH_SHORT).show();
                        break;
                }

            }
        };

        smsDeliveredReceiver =new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent)
            {

                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(MainActivity.this, "SMS Delivered!" , Toast.LENGTH_SHORT).show();
                        break;

                    case Activity.RESULT_CANCELED:
                        Toast.makeText(MainActivity.this, "SMS not Delivered!" , Toast.LENGTH_SHORT).show();
                        break;

                }

            }
        };

        registerReceiver(smsSentReceiver, new IntentFilter(SENT));
        registerReceiver(smsDeliveredReceiver, new IntentFilter(DELIVERED));
    }

    @Override
    protected void onPause() {
        super.onPause();

        unregisterReceiver(smsDeliveredReceiver);
        unregisterReceiver(smsSentReceiver);
    }

    public void send_sms(View view)
    {
        String mes= message.getText().toString();
        String num = number.getText().toString();

        if(num.isEmpty() && mes.isEmpty())
        {
            Toast.makeText(MainActivity.this, "Please Enter Number and Type Message", Toast.LENGTH_SHORT).show();
        }
        else

            if(num.isEmpty()) {
            Toast.makeText(MainActivity.this, "Please Enter Number", Toast.LENGTH_SHORT).show();
        }
            else if(mes.isEmpty())
        {
            Toast.makeText(MainActivity.this, "Please Type Message", Toast.LENGTH_SHORT).show();

        } else

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS},MY_PERMISSION_REQUEST_SEND_SMS);
        }
        else
        {
            SmsManager sms= SmsManager.getDefault();
            sms.sendTextMessage(num , null, mes, sentPI, deliveredPI);
        }
    }
}
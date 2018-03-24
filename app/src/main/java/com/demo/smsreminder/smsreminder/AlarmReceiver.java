package com.demo.smsreminder.smsreminder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by hetulpatel on 24/03/18.
 */

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        String text = "";
        String number = "";
        Log.i("Receiver", "Broadcast received: " + action);
        Toast.makeText(context, intent.getExtras().getString("text"), Toast.LENGTH_SHORT).show();

        if(action.equals("my.action.string")){
            text = intent.getExtras().getString("text");
            number = intent.getExtras().getString("number");
            String smsBody = text;

            SmsManager smsManager = SmsManager.getDefault();

            smsManager.sendTextMessage(number, null, smsBody, null, null);
        }


    }
}

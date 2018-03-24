### Resource Links

* [Runtime permission for newer android versions](https://www.youtube.com/watch?time_continue=165&v=C8lUdPVSzDk)

* [AlarmManager for scheduling sms](https://developer.android.com/reference/android/app/AlarmManager.html) 

### AlarmReceiver.java ( Broadcast Reciever )

```
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


```

### MainActivity.java

```

public class MainActivity extends AppCompatActivity {


    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;
    int REQUEST_CODE_1 = 1;
    Calendar cal;
    DatePicker datePicker;
    Button setbtn;
    EditText editText;
    EditText number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        intit();

        final Calendar now = Calendar.getInstance();

        datePicker.init(
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH),
                null);


        setbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cal = Calendar.getInstance();
                cal.set(datePicker.getYear(),
                        datePicker.getMonth(),
                        datePicker.getDayOfMonth()
                );


                if(cal.compareTo(now) <= 0){
                    //The set Date/Time already passed
                    Toast.makeText(getApplicationContext(),
                            "Invalid Date/Time",
                            Toast.LENGTH_LONG).show();
                }else{
                    //setAlarm(cal);
                    sendSMSMessage();
                }

            }
        });
    }



    private void intit() {

        datePicker = (DatePicker) findViewById(R.id.datepicker);
        setbtn = (Button) findViewById(R.id.set_btn);
        editText = (EditText) findViewById(R.id.message);
        number = (EditText) findViewById(R.id.number);

    }

    protected void sendSMSMessage() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED){
                setAlarm(cal);
            } else {

                if (shouldShowRequestPermissionRationale(Manifest.permission.SEND_SMS)){
                    Toast.makeText(getApplicationContext(),
                            "SEND SMS PERMISSION REQUIRED FOR REMINDER", Toast.LENGTH_LONG).show();
                }

                requestPermissions(new String[]{Manifest.permission.SEND_SMS,Manifest.permission.READ_PHONE_STATE}, MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
        }
    }

    private void setAlarm(Calendar targetCal){

        String msg = editText.getText().toString();
        String num = number.getText().toString();

        Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
        intent.setAction("my.action.string");
        intent.putExtra("number",num);
        intent.putExtra("text",msg);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), REQUEST_CODE_1++, intent, 0);
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), pendingIntent);

        Log.i("SET ALARM","TRUE");

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {

        if (requestCode == MY_PERMISSIONS_REQUEST_SEND_SMS) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setAlarm(cal);
            } else {
                Toast.makeText(getApplicationContext(),
                        "SMS faild, please try again.", Toast.LENGTH_LONG).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}

```


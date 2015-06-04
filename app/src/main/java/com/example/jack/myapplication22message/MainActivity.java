package com.example.jack.myapplication22message;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

    EditText txtMsgEditTExt,pNumEditText,messageEditText;

    Button sendButton;

    //all the message for show up in the activity
    static String messages = "";

    Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtMsgEditTExt = (EditText)findViewById(R.id.txtMsgEditText);
        pNumEditText = (EditText)findViewById(R.id.pNumEditText);
        messageEditText = (EditText)findViewById(R.id.messagesEditText);

        sendButton = (Button)findViewById(R.id.sendButton);

        // every five seconds
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){

                    try{

                        Thread.sleep(5000);

                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {

                                messageEditText.setText(messages);
                            }
                        });


                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void sendMessage(View view) {

        String phoneNum = pNumEditText.getText().toString();
        String message = txtMsgEditTExt.getText().toString();

        try{

            SmsManager smsManager = SmsManager.getDefault();

            //
            // Sends the text message
            // 2nd is for the service center address or null
            // 4th if not null broadcasts with a successful send
            // 5th if not null broadcasts with a successful delivery
            smsManager.sendTextMessage(phoneNum,null,message,null,null);

            Toast.makeText(this,"message sent out",Toast.LENGTH_SHORT).show();;


        }catch (IllegalArgumentException ex){

            Toast.makeText(this,"Enter a phone number or message t",Toast.LENGTH_SHORT).show();;

            ex.printStackTrace();


        }
        messages = messages + "You sent : " + message;
    }

    public static class SmsReceiver extends BroadcastReceiver {

        final SmsManager smsManager = SmsManager.getDefault();

        public SmsReceiver(){

        }


        @Override
        public void onReceive(Context context, Intent intent) {

            final Bundle bundle =intent.getExtras();

            try{

                if (bundle != null){

                    final Object[] pdusObj = (Object[])bundle.get("pdus");

                    for (int i =0;i<pdusObj.length;i++){

                        SmsMessage smsMessage =
                                SmsMessage.createFromPdu((byte[]) pdusObj[i]);

                        String phoneNum =  smsMessage.getDisplayOriginatingAddress();
                        String message = smsMessage.getDisplayMessageBody();

                        messages = messages +phoneNum + message + "\n";


                    }
                }


            }catch (Exception ex){

                Log.e("SmsReceiver","Exception SmsReceiver"+ex.getMessage());
            }


        }
    }


    public static class MMSReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            throw  new UnsupportedOperationException("not implement yet");
        }
    }
    public static class HeadlessSmsSendService extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            throw  new UnsupportedOperationException("not implement yet");
        }
    }

    //remove callbacks from runnables
    @Override
    protected void onDestroy() {

        mHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}

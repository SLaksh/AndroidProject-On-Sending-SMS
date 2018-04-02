package com.example.apple13.myapplicationforsms;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends Activity implements View.OnClickListener
{
    EditText txtPhone;
    EditText txtMessage;

    Button btnFillMessage;
    Button btnSend;
    Button btnSendMultipart;
    Button btnSendIntent;
    Button button1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
			/*
	         * Initialize Widgets
	         */

            txtPhone = (EditText) findViewById(R.id.txtPhone);

            txtMessage = (EditText) findViewById(R.id.txtMessage);

            btnFillMessage = (Button) findViewById(R.id.btnFillMessage);
            btnFillMessage.setOnClickListener(this);


            btnSend = (Button) findViewById(R.id.btnSend);
            btnSend.setOnClickListener(this);

            btnSendMultipart = (Button) findViewById(R.id.btnSendMultipart);
            btnSendMultipart.setOnClickListener(this);

            btnSendIntent = (Button) findViewById(R.id.btnSendDirectIntent);
            btnSendIntent.setOnClickListener(this);

            button1  = (Button) findViewById(R.id.btnSendIntentSmsTo);
            button1.setOnClickListener(this);

			/*
			 * Checking If Device Can Send SMS
			 */

            PackageManager pm = this.getPackageManager();

            if (!pm.hasSystemFeature(PackageManager.FEATURE_TELEPHONY) &&
                    !pm.hasSystemFeature(PackageManager.FEATURE_TELEPHONY_CDMA))
            {
                Toast.makeText(this, "Sorry, your device probably can't send SMS...", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception ex)
        {
            Toast.makeText(this,
                    "Error in MainActivity.onCreate: " + ex.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v)
    {
        String strPhone;
        String strMessage;
        SmsManager sms;

        switch (v.getId()) {
            case R.id.btnFillMessage:

			/* Just Fills txtMessage With 160+ Symbols */
			/* So long message can be send via sendMultipartTextMessage(),
			 * but can't be send via sendTextMessage()
			 */

                txtMessage.setText("");

			/* 16 x 10 = 160*/
                for (int i = 0; i < 16; i++)
                {
                    txtMessage.setText(txtMessage.getText() + "1234567890");
                }

			/* ... and 4 symbols more */
                txtMessage.setText(txtMessage.getText() + "TEST");
                break;

            case R.id.btnSend:

                strPhone = txtPhone.getText().toString();
                strMessage = txtMessage.getText().toString();

                sms = SmsManager.getDefault();

                sms.sendTextMessage(strPhone,
                        null, strMessage,
                        null, null);

                Toast.makeText(this, "Sent.", Toast.LENGTH_SHORT).show();

                break;
            case R.id.btnSendMultipart:

                strPhone = txtPhone.getText().toString();
                strMessage = txtMessage.getText().toString();

                sms = SmsManager.getDefault();

                ArrayList<String> messageParts = sms.divideMessage(strMessage);

                sms.sendMultipartTextMessage(strPhone,
                        null, messageParts,
                        null, null);

                Toast.makeText(this, "Sent.", Toast.LENGTH_SHORT).show();
                break;

            case R.id.btnSendDirectIntent:

                strPhone = txtPhone.getText().toString();
                strMessage = txtMessage.getText().toString();

                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                sendIntent.setType("vnd.android-dir/mms-sms");
                sendIntent.putExtra("address", strPhone);
                sendIntent.putExtra("sms_body", strMessage);
                startActivity(sendIntent);
                break;

            case R.id.btnSendIntentSmsTo:

                strPhone = txtPhone.getText().toString();
                strMessage = txtMessage.getText().toString();

                Uri sms_uri = Uri.parse("smsto:+" + strPhone);
                Intent sms_intent = new Intent(Intent.ACTION_SENDTO, sms_uri);
                sms_intent.putExtra("sms_body", strMessage);
                startActivity(sms_intent);
                break;
        }
    }
}

package bitcrownd.wallet.bitcrownd.transfer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import bitcrownd.wallet.bitcrownd.R;

public class SendEmailActivity extends Activity {

    Button buttonSend;
    EditText textTo;
    EditText textSubject;
    EditText textMessage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_email);

        buttonSend = (Button) findViewById(R.id.button1);
        textTo = (EditText) findViewById(R.id.editText1);
        //textSubject = (EditText) findViewById(R.id.editTextSubject);
        textMessage = (EditText) findViewById(R.id.editText4);


        String value1 = super.getIntent().getExtras().getString("value1");
        textMessage.setText(value1);

        buttonSend.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                String to = textTo.getText().toString();
                String subject = "Bitcrownd Adress Notification";
                String message = textMessage.getText().toString();

                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{ to});
                //email.putExtra(Intent.EXTRA_CC, new String[]{ to});
                //email.putExtra(Intent.EXTRA_BCC, new String[]{to});
                email.putExtra(Intent.EXTRA_SUBJECT, subject);
                email.putExtra(Intent.EXTRA_TEXT, message);

                //need this to prompts email client only
                email.setType("message/rfc822");

                startActivity(Intent.createChooser(email, "Choose an Email client :"));

            }
        });
    }
}
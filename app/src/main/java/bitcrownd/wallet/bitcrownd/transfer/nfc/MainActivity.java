package bitcrownd.wallet.bitcrownd.transfer.nfc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import bitcrownd.wallet.bitcrownd.R;

public class MainActivity extends FragmentActivity {

	//private NfcAdapter mNfcAdapter;
	private static final int MESSAGE_SENT = 1;
	private TextView receiveText;
	private TextView sendText;

	private SendDataNFC mSendDataNFC;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
  		 setContentView(R.layout.nfc_activity);
		 receiveText = (TextView) findViewById(R.id.ReceiveText);
		 sendText = (TextView)findViewById(R.id.SendText);
		 
		 sendText.setEnabled(true);
		 sendText.setFocusableInTouchMode(true);
		 sendText.setClickable(true);

		 mSendDataNFC = new SendDataNFC(this,sendText);

		 Intent intent = getIntent();

		 Bundle extras = intent.getExtras();
		 if (extras != null) {
		     if (extras.containsKey("str")) {
		    	 String str = getIntent().getStringExtra("str");
		    	 receiveText.setText(str);
		     }
		 }

		String value1 = super.getIntent().getExtras().getString("value1");
		sendText.setText(value1);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.wallet_overview, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}

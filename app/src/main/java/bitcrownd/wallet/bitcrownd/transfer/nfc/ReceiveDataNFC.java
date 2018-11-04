package bitcrownd.wallet.bitcrownd.transfer.nfc;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import bitcrownd.wallet.bitcrownd.R;


public class ReceiveDataNFC extends FragmentActivity {
	public static String data = "Hola";
    
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nfc_receive);
    }

	@Override
	public void onNewIntent(Intent intent) {
		// onResume gets called after this to handle the intent
		setIntent(intent);
	}
	
	
	/**
	* Parses the NDEF Message from the intent and prints to the TextView
	*/
	void processIntent(Intent intent) {

		Parcelable[] rawMsgs = intent.getParcelableArrayExtra(
		        NfcAdapter.EXTRA_NDEF_MESSAGES);
		
		// Only one message sent during the beam
		NdefMessage msg = (NdefMessage) rawMsgs[0];
		if (msg != null)
		{
			// record 0 contains the MIME type, record 1 is the AAR, if present
			String str = new String(msg.getRecords()[0].getPayload());

			Toast.makeText(getApplicationContext(), "Your message was received", Toast.LENGTH_LONG).show();

			Intent i = new Intent(this, MainActivity.class);
			i.putExtra("str", str);
	        startActivity(i);
			
		}else
		{
			Toast.makeText(getApplicationContext(), "Not message received", Toast.LENGTH_LONG).show();
		}
	}
	
	
    @Override
    protected void onResume() {
        super.onResume();
       	// Check to see that the Activity started due to an Android Beam
         if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
    	    processIntent(getIntent());
    	}
    }

}
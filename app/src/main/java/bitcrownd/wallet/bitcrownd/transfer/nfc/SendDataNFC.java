package bitcrownd.wallet.bitcrownd.transfer.nfc;

import android.app.Activity;
import android.content.Context;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.charset.Charset;

/**
 * Created by Alex on 14/06/2015.
 */
public class SendDataNFC implements NfcAdapter.CreateNdefMessageCallback,NfcAdapter.OnNdefPushCompleteCallback {

    private NfcAdapter mNfcAdapter;

    private static final int MESSAGE_SENT = 1;
    private Context mContext;
    private Activity mActivity;
    private TextView mSendText;

    //-----------------------------------------------------------------------------------------------------------------------------------
// Send Data by NFC
//-----------------------------------------------------------------------------------------------------------------------------------

    public SendDataNFC(Activity activity, TextView sendText){
        mActivity = activity;
        mContext = mActivity.getApplicationContext();

        mSendText = sendText;
        setupNFC();
    }

    public void setupNFC(){

        mNfcAdapter = NfcAdapter.getDefaultAdapter(mActivity);


        if (mNfcAdapter == null) {
           // Toast.makeText(this, "NFC is not available", Toast.LENGTH_LONG).show();
            return;
        }
        else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            // If Android Beam isn't available, don't continue.
           // Toast.makeText(this, "Android Beam is not available", Toast.LENGTH_LONG).show();

        } else
        {
            // Register callback to set NDEF message
            mNfcAdapter.setNdefPushMessageCallback(this, mActivity);
            // Register callback to listen for message-sent success
            mNfcAdapter.setOnNdefPushCompleteCallback(this, mActivity);

        }



    }


    /**
     * Implementation for the CreateNdefMessageCallback interface
     */

    @Override
    public NdefMessage createNdefMessage(NfcEvent event) {

        NdefMessage msg = null;

        String cardData = mSendText.getText().toString();

        msg = new NdefMessage(
                new NdefRecord[] { createMimeRecord(
                        "application/com.example.nfcchat", cardData.getBytes()) /**
                 * The Android Application Record (AAR) is commented out. When a device
                 * receives a push with an AAR in it, the application specified in the AAR
                 * is guaranteed to run. The AAR overrides the tag dispatch system.
                 * You can add it back in to guarantee that this
                 * activity starts when receiving a beamed message. For now, this code
                 * uses the tag dispatch system.
                 */
                        , NdefRecord.createApplicationRecord("com.example.nfcchat")
                });


        return msg;
    }

    /**
     * Implementation for the OnNdefPushCompleteCallback interface
     */

    @Override
    public void onNdefPushComplete(NfcEvent arg0) {
        // A handler is needed to send messages to the activity when this
        // callback occurs, because it happens from a binder thread
        mHandler.obtainMessage(MESSAGE_SENT).sendToTarget();
    }

    /** This handler receives a message from onNdefPushComplete */
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case MESSAGE_SENT:
                    //Toast.makeText(getApplicationContext(), "Card(s) sent!", Toast.LENGTH_LONG).show();
                    Toast.makeText(mContext, "Your message was sent", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

    /**
     * Creates a custom MIME type encapsulated in an NDEF record
     *
     * @param mimeType
     */

    public NdefRecord createMimeRecord(String mimeType, byte[] payload) {
        byte[] mimeBytes = mimeType.getBytes(Charset.forName("US-ASCII"));
        NdefRecord mimeRecord = new NdefRecord(
                NdefRecord.TNF_MIME_MEDIA, mimeBytes, new byte[0], payload);
        return mimeRecord;
    }


}

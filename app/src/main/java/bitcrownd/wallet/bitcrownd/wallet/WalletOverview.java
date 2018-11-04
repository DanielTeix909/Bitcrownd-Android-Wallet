package bitcrownd.wallet.bitcrownd.wallet;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.google.common.base.Joiner;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.bitcoinj.crypto.MnemonicCode;
import org.bitcoinj.crypto.MnemonicException;

import java.util.List;
import java.util.UUID;

import bitcrownd.wallet.bitcrownd.R;
import bitcrownd.wallet.bitcrownd.model.WalletState;
import bitcrownd.wallet.bitcrownd.transfer.SendEmailActivity;
import bitcrownd.wallet.bitcrownd.transfer.SendSMSActivity;
import bitcrownd.wallet.bitcrownd.transfer.bluetooth.MainActivity;
import bitcrownd.wallet.bitcrownd.wallet.adapter.TabsPagerAdapter;

import static bitcrownd.wallet.bitcrownd.R.id.address;


public class WalletOverview extends FragmentActivity implements ActionBar.TabListener
{
    private ViewPager viewPager;
    private ActionBar actionBar;
    private Button button1;
    public static final UUID MY_UUID = UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");
    public static final String NAME = "Bitcrownd";
    FloatingActionButton menu1,menu2,menu3,menu4 ;
    public boolean isFirstStart;
    Context mcontext;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_overview);

        menu1 = (FloatingActionButton)findViewById(R.id.subFloatingMenu1) ;
        menu2 = (FloatingActionButton)findViewById(R.id.subFloatingMenu2) ;
        menu3 = (FloatingActionButton)findViewById(R.id.subFloatingMenu3) ;
        menu4 = (FloatingActionButton)findViewById(R.id.subFloatingMenu4) ;


        menu1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final TextView addressText = (TextView) findViewById(address);
                String passingdata = addressText.getText().toString();
                Intent myIntent = new Intent(WalletOverview.this, SendEmailActivity.class);
                Bundle b = new Bundle();
                b.putString("value1", passingdata);
                myIntent.putExtras(b);
                startActivity(myIntent);
            }
        });

        menu4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            final TextView addressText = (TextView) findViewById(address);
                String passingdata = addressText.getText().toString();
                Intent myIntent = new Intent(WalletOverview.this, bitcrownd.wallet.bitcrownd.transfer.nfc.MainActivity.class);
                Bundle b = new Bundle();
                b.putString("value1", passingdata);
                myIntent.putExtras(b);

                startActivity(myIntent);


            }
        });
        menu2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final TextView addressText = (TextView) findViewById(address);
                String passingdata = addressText.getText().toString();
                Intent myIntent = new Intent(WalletOverview.this, SendSMSActivity.class);
                Bundle b = new Bundle();
                b.putString("value1", passingdata);
                myIntent.putExtras(b);

                startActivity(myIntent);


            }
        });

        menu3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final TextView addressText = (TextView) findViewById(address);
                String passingdata = addressText.getText().toString();
                Intent myIntent = new Intent(WalletOverview.this, MainActivity.class);
                Bundle b = new Bundle();
                b.putString("value1", passingdata);
                myIntent.putExtras(b);

                startActivity(myIntent);


            }
        });



        // Initialization
        viewPager = (ViewPager) findViewById(R.id.pager);
        actionBar = getActionBar();

        final TabsPagerAdapter tabsPagerAdapter = new TabsPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(tabsPagerAdapter);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        String[] tabs = new String[]
        {
            getResources().getString(R.string.tab_send),
            getResources().getString(R.string.tab_wallet),
            getResources().getString(R.string.tab_transactions)
        };

        // Adding Tabs
        for (String tab_name : tabs)
        {
            actionBar.addTab(actionBar.newTab().setText(tab_name).setTabListener(this));
        }

        // On swiping the viewpager make respective tab selected
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageSelected(int position)
            {
                // on changing the page
                // make respected tab selected
                actionBar.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2)
            {
            }

            @Override
            public void onPageScrollStateChanged(int arg0)
            {
            }
        });

        viewPager.setCurrentItem(1);

    }

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }
    @Override
    public void onStart()
    {
        super.onStart();

        WalletState.getState().triggerUpdate();

        if (WalletState.getState().getFirstLaunch())
        {
            // At first launch, calculate the mnemonic and display it to the user
            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

            try
            {
                final List<String> mnemonic = MnemonicCode.INSTANCE.toMnemonic(
                    WalletState.getState().getConfiguration().getSeed());
                final String fullMnemonic = Joiner.on(" ").join(mnemonic);

                alertDialog.setTitle(getString(R.string.general_first_launch_title));
                alertDialog.setMessage(getString(R.string.general_first_launch_message, fullMnemonic));

                alertDialog.setPositiveButton(
                    getString(R.string.general_first_launch_close),
                    new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            dialog.dismiss();
                            WalletState.getState().unsetFirstLaunch();
                        }
                    });

                alertDialog.show();
            }
            catch (MnemonicException.MnemonicLengthException exception)
            { }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.wallet_overview, menu);
        return true;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null)
        {
            WalletState.getState().getSendTab().setAddress(scanResult.getContents());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings)
        {
            Intent intent = new Intent(this, UserPreferences.class);
            startActivity(intent);
            return true;
        }
        else if (id == R.id.action_refresh)
        {
            // Refresh the balance
            WalletState.getState().getBalanceTab().triggerRefresh();
            // Refresh the list of transactions
            WalletState.getState().getTransactionsTab().triggerRefresh();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft)
    {
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft)
    {
        // on tab selected
        // show respected fragment view
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft)
    {
    }

}

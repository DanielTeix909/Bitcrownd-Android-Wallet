package bitcrownd.wallet.bitcrownd.model;

import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.crypto.MnemonicCode;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import bitcrownd.wallet.bitcrownd.R;
import bitcrownd.wallet.bitcrownd.utils.SecurePreferences;
import bitcrownd.wallet.bitcrownd.wallet.fragment.BalanceTab;
import bitcrownd.wallet.bitcrownd.wallet.fragment.SendTab;
import bitcrownd.wallet.bitcrownd.wallet.fragment.TransactionsTab;

public class WalletState
{
    public final static String seedKey = "wallet.seed";
    private static WalletState state;

    private final WalletConfiguration configuration;
    private final APIClient api;
    private Boolean firstLaunch;
    private AddressBalance walletData;
    private BalanceTab balanceTab;
    private SendTab sendTab;
    private TransactionsTab transactionsTab;

    public WalletState(WalletConfiguration configuration, APIClient api, Boolean firstLaunch)
    {
        this.configuration = configuration;
        this.api = api;
        this.firstLaunch = firstLaunch;

        Timer updateTimer = new Timer();
        TimerTask task = new TimerTask()
        {
            @Override
            public void run()
            {
                WalletState.this.triggerUpdate();
            }
        };

        updateTimer.schedule(task, 0, 60000);
    }

    /**
     * Gets the singleton instance.
     *
     * @return the singleton instance
     */
    public static WalletState getState()
    {
        if (state == null)
            state = initialize();

        return state;
    }

    private static WalletState initialize()
    {
        SecurePreferences preferences = new SecurePreferences(BalanceApplication.getContext());

        String seed = preferences.getString(seedKey, null);

        Boolean firstLaunch = false;
        WalletConfiguration wallet;
        if (seed == null)
        {
            seed = WalletConfiguration.createWallet();

            SecurePreferences.Editor editor = preferences.edit();
            editor.putString(seedKey, seed);
            editor.commit();

            firstLaunch = true;
        }

        wallet = new WalletConfiguration(
            seed, NetworkParameters.fromID(BalanceApplication.getContext().getString(R.string.network)));

        try
        {
            MnemonicCode.INSTANCE = new MnemonicCode(BalanceApplication.getContext().getAssets()
                .open("bip39-wordlist.txt"), null);
        }
        catch (IOException exception)
        { }

        return new WalletState(
            wallet,
            new APIClient(
                BalanceApplication.getContext().getString(R.string.api_base_url),
                wallet.getNetworkParameters()),
            firstLaunch);
    }

    public void triggerUpdate()
    {
        BalanceLoader loader = new BalanceLoader(this);
        loader.execute(configuration.getAddress());
    }

    public void updateData(AddressBalance data)
    {
        if (data != null)
            this.walletData = data;

        this.balanceTab.updateWallet();
        this.sendTab.updateWallet();
    }

    public AddressBalance getBalance()
    {
        return this.walletData;
    }

    public WalletConfiguration getConfiguration()
    {
        return this.configuration;
    }

    public APIClient getAPIClient()
    {
        return this.api;
    }

    public BalanceTab getBalanceTab()
    {
        return balanceTab;
    }

    public void setBalanceTab(BalanceTab balanceTab)
    {
        this.balanceTab = balanceTab;
    }

    public SendTab getSendTab()
    {
        return sendTab;
    }

    public void setSendTab(SendTab sendTab)
    {
        this.sendTab = sendTab;
    }

    public TransactionsTab getTransactionsTab()
    {
        return transactionsTab;
    }

    public void setTransactionsTab(TransactionsTab transactionsTab)
    {
        this.transactionsTab = transactionsTab;
    }
    public Boolean getFirstLaunch()
    {
        return firstLaunch;
    }

    public void unsetFirstLaunch()
    {
        this.firstLaunch = false;
    }
}

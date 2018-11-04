package bitcrownd.wallet.bitcrownd.model;

import android.os.AsyncTask;

import java.util.List;

import bitcrownd.wallet.bitcrownd.wallet.fragment.TransactionsTab;

public class TransactionsLoader extends AsyncTask<String, Integer, List<SingleAssetTransaction>>
{
    private AddressBalance addressBalance;
    private TransactionsTab parent;

    public TransactionsLoader(TransactionsTab parent)
    {
        this.parent = parent;
    }

    @Override
    protected List<SingleAssetTransaction> doInBackground(String... addresses)
    {
        try
        {
            return WalletState.getState().getAPIClient().getTransactions(addresses[0]);
        }
        catch (Exception e)
        {
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<SingleAssetTransaction> result)
    {
        super.onPostExecute(result);

        this.parent.updateTransactions(result);
    }
}

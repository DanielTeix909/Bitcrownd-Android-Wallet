package bitcrownd.wallet.bitcrownd.model;

import android.os.AsyncTask;

public class BalanceLoader extends AsyncTask<String, Integer, AddressBalance>
{
    private WalletState parent;

    public BalanceLoader(WalletState parent)
    {
        this.parent = parent;
    }

    @Override
    protected AddressBalance doInBackground(String... addresses)
    {
        try
        {
            return parent.getAPIClient().getAddressBalance(addresses[0]);
        }
        catch (Exception e)
        {
            return null;
        }
    }

    @Override
    protected void onPostExecute(AddressBalance result)
    {
        super.onPostExecute(result);

        this.parent.updateData(result);
    }
}

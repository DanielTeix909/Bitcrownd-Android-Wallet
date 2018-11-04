package bitcrownd.wallet.bitcrownd.model;

import java.util.List;


public class AddressBalance
{
    private final Long satoshiBalance;
    private final List<AssetBalance> assetBalances;

    public AddressBalance(Long satoshiBalance, List<AssetBalance> assetBalances)
    {
        this.satoshiBalance = satoshiBalance;
        this.assetBalances = assetBalances;
    }

    /**
     * Gets the balance in satoshis.
     *
     * @return the balance in satoshis
     */
    public Long getSatoshiBalance()
    {
        return this.satoshiBalance;
    }

    /**
     * Gets the balance for assets.
     *
     * @return the balance for assets
     */
    public List<AssetBalance> getAssetBalances()
    {
        return this.assetBalances;
    }
}

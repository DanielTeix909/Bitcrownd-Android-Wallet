package bitcrownd.wallet.bitcrownd.model;

import java.math.BigInteger;

/**
 * Represents the balance of an address with regard to an asset.
 */
public class AssetBalance
{
    private final AssetDefinition asset;
    private final BigInteger quantity;

    public AssetBalance(AssetDefinition asset, BigInteger quantity)
    {
        this.asset = asset;
        this.quantity = quantity;
    }

    /**
     * Gets the definition of the asset.
     *
     * @return the definition of the asset
     */
    public AssetDefinition getAsset()
    {
        return this.asset;
    }

    /**
     * Gets the number of units for that asset.
     * 
     * @return the number of units for that asset
     */
    public BigInteger getQuantity()
    {
        return this.quantity;
    }
}

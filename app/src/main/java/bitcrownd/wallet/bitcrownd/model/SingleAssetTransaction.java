package bitcrownd.wallet.bitcrownd.model;

import java.math.BigInteger;
import java.util.Date;

public class SingleAssetTransaction
{
    private final String transactionId;
    private final Date date;
    private final AssetDefinition asset;
    private final BigInteger quantity;

    public SingleAssetTransaction(String transactionId, Date date, AssetDefinition asset,
        BigInteger quantity)
    {
        this.transactionId = transactionId;
        this.date = date;
        this.asset = asset;
        this.quantity = quantity;
    }

    public String getTransactionId()
    {
        return transactionId;
    }

    public AssetDefinition getAsset()
    {
        return asset;
    }

    public BigInteger getQuantity()
    {
        return quantity;
    }

    public Date getDate()
    {
        return date;
    }
}

package bitcrownd.wallet.bitcrownd.model;

import android.app.Application;
import android.content.Context;

public class BalanceApplication extends Application
{
    private static BalanceApplication instance;

    public BalanceApplication()
    {
        instance = this;
    }

    public static Context getContext()
    {
        return instance;
    }
}

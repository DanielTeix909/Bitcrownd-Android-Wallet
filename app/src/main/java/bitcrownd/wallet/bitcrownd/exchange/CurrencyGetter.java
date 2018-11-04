package bitcrownd.wallet.bitcrownd.exchange;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class CurrencyGetter extends AsyncTask<String, Void, String> {
    private Exception exception;
    private ArrayList<String> currencyArrany;
    @Override
    protected String doInBackground(String... params) {
        try {
            // I SHOULD TRY TO MAKE IT WORK WITH N' PARAMS; NOT ONLY 1
            String yahooUrl = "http://finance.yahoo.com/d/quotes.csv?s=" + params[0] + "&f=a";
            // EUR to USD link --> http://finance.yahoo.com/d/quotes.csv?s=EURUSD%3DX&f=a
            URL url = new URL(yahooUrl);
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("GET");
            http.connect();
            InputStream is = http.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            //UNCLEAN CODE; I SHOULD TRY TO MAKE IT CLEANER; IT WORKS THOUGH
            return  br.readLine();

        } catch (Exception e) {
            this.exception = e;

            return null;
        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}

package bitcrownd.wallet.bitcrownd.authentication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import bitcrownd.wallet.bitcrownd.R;
import bitcrownd.wallet.bitcrownd.wallet.WalletOverview;

public class LoginActivity extends Activity {

    private EditText etUsername;
    private EditText etPassword;
    private Button btnLogin;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        etUsername = (EditText) findViewById(R.id.username_field);
        etPassword = (EditText) findViewById(R.id.password_field);
        btnLogin = (Button) findViewById(R.id.btnlogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String username = etUsername.getText().toString();
                final String password = etPassword.getText().toString();

                new AsyncLogin().execute(username,password);

            }
        });

        Button imageLogo = (Button)findViewById(R.id.register_button);
        imageLogo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String url = "http://www.bitcrownd.com/register.php";

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        Button fogoter = (Button)findViewById(R.id.forgot_button);
        fogoter.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String url = "http://www.bitcrownd.com/recovery.php";

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

    }

    private class AsyncLogin extends AsyncTask<String, String, String>
    {
        ProgressDialog pdLoading = new ProgressDialog(LoginActivity.this);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }
        @Override
        protected String doInBackground(String... params) {
            try {

                url = new URL("http://84.201.37.11/workspace/Bitcrownd/Bitcrownd.com/APP_login.php");

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "exception";
            }
            try {
                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection)url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(10000);
                conn.setRequestMethod("POST");

                // setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);

                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("UserEmail", params[0])
                        .appendQueryParameter("PIN_Code", params[1]);
                String query = builder.build().getEncodedQuery();

                // Open connection for sending data
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return "exception";
            }

            try {

                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    // Pass data to onPostExecute method
                    return(result.toString());

                }else{

                    return("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return "exception";
            } finally {
                conn.disconnect();
            }


        }

        @Override
        protected void onPostExecute(String result) {

            //this method will be running on UI thread

            pdLoading.dismiss();

            if(result.equalsIgnoreCase("true"))
            {
                Intent intent = new Intent(LoginActivity.this,WalletOverview.class);
                startActivity(intent);
                LoginActivity.this.finish();

            }else if (result.equalsIgnoreCase("false")){

                // If username and password does not match display a error message
                Toast.makeText(LoginActivity.this, "Invalid userename or password", Toast.LENGTH_LONG);

            } else if (result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) {

                Toast.makeText(LoginActivity.this, "OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG);

            }

            Button btn = (Button)findViewById(R.id.register_button);

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(LoginActivity.this, ForgotPass.class));
                }
            });
        }


    }
}

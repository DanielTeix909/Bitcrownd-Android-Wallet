package bitcrownd.wallet.bitcrownd.exchange;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import bitcrownd.wallet.bitcrownd.R;

public class MainActivity extends Activity {

    private String str_current_EURUSD;
    private String str_current_USDEUR;
    private String str_conversion;

    private float float_current_EURUSD;
    private float float_current_USDEUR;
    private float float_conversion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_converter);

        // Initialize the currency labels.
        TextView label_EURUSD = (TextView) findViewById(R.id.label_text_EURUSD);
        TextView label_USDEUR = (TextView) findViewById(R.id.label_text_USDEUR);

        // Try to load the currency from yahoo
        try {

            this.str_current_EURUSD = generateConversion("EURBTC%3DX");

            this.float_current_EURUSD = Float.valueOf(str_current_EURUSD);
            this.float_current_USDEUR = 1/float_current_EURUSD;
            this.str_current_USDEUR = String.valueOf(float_current_USDEUR);
            label_EURUSD.setText(str_current_EURUSD);
            label_USDEUR.setText(str_current_USDEUR);

            //Throw a toast informing that the currency is loaded.
            Context context = getApplicationContext();
            CharSequence text = "Current currency loaded";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        // In case that the connexion with yahoo fails -> use a default currency
        } catch (IOException|InterruptedException|ExecutionException e) {
            //Throw a toast informing that it failed to load.
            Context context = getApplicationContext();
            CharSequence text = "Unable to get the current rate! Using an outdated value of 1.05€ for 1.00$";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            //Use a fixed currency
            this.str_current_EURUSD = "1.05";
            this.float_current_EURUSD = Float.valueOf(str_current_EURUSD);
            this.float_current_USDEUR = 1/float_current_EURUSD;
            this.str_current_USDEUR = String.valueOf(float_current_USDEUR);
            label_EURUSD.setText(str_current_EURUSD);
            label_USDEUR.setText(str_current_USDEUR);
        }

    }

    public void currency_convert_event(View view){
        //When the button is pressed
        //Initialize the labels and EditText fields.
        TextView label_output = (TextView) findViewById(R.id.textView);
        EditText field_currency_imput = (EditText) findViewById(R.id.field_currency_imput);
        EditText field_comision = (EditText) findViewById(R.id.field_comission_imput);

        // If the amount is filled:
        if(!field_currency_imput.getText().toString().equals("")) {
            float float_currency_imput = Float.valueOf(field_currency_imput.getText().toString());
            RadioGroup rGroup_Currency_Getter = (RadioGroup) findViewById(R.id.radioGroup);
            //Load the fee
            float float_comision;
            if(!field_comision.getText().toString().equals("")){
                float_comision = Float.valueOf(field_comision.getText().toString()) * (float)0.01;
            } else {
                float_comision = 0;
            }

            //Find out what radio button has been pressed.
            switch (rGroup_Currency_Getter.getCheckedRadioButtonId()) {
                case R.id.rButton_EURUSD:
                    //Make the conversion
                    this.float_conversion = this.float_current_EURUSD * float_currency_imput;
                    //Apply the fee
                    float_comision = float_comision * this.float_conversion;
                    this.float_conversion -= float_comision;
                    //It's possible to improve this allowing different symbols.
                    this.str_conversion = String.valueOf(this.float_conversion) + " €";
                    break;
                case R.id.rButton_USDEUR:
                    //Make the converision
                    this.float_conversion = this.float_current_USDEUR * float_currency_imput;
                    //Apply the fee
                    float_comision = float_comision * this.float_conversion;
                    this.float_conversion -= float_comision;
                    //It can be improved - Generate the output String
                    this.str_conversion = String.valueOf(this.float_conversion) + " $";
                    break;
            }
            //Shows the output amount
            label_output.setText(this.str_conversion);
        }



    }

    public String generateConversion(String currency_yahoo) throws IOException, ExecutionException, InterruptedException {
        // Create and execute the asynchronous task
        CurrencyGetter tarea2 = new CurrencyGetter();
        // It needs the string with the currency that you want from yahoo - in this case we always use EUR-USD
        return tarea2.execute(currency_yahoo).get();
    }
}

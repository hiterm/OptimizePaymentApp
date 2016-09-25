package io.github.htlsne.optimizepayment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

public class SetWalletAmountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_wallet_amount);

        // ボタンの設定
        findViewById(R.id.button_set_wallet_amount).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editText = (EditText) findViewById(R.id.editText_input_wallet_amount);
                String walletAmountStr = editText.getText().toString();
                final LinearLayout layout = (LinearLayout) findViewById(R.id.root_layout_activity_set_wallet_amount);
                if ("".equals(walletAmountStr)) {
                    Snackbar.make(layout, R.string.no_input, Snackbar.LENGTH_LONG).show();
                } else {
                    int walletAmount = Integer.parseInt(walletAmountStr);

                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putInt("walletAmount", walletAmount);
                    editor.apply();
                    Snackbar.make(layout, R.string.finish_amount_set, Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }
}

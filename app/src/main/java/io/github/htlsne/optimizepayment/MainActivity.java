package io.github.htlsne.optimizepayment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import io.github.htlsne.optimizepayment.model.MoneySet;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        // ボタンの設定
        // button_calc_for_payment
        findViewById(R.id.button_calc_for_payment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                int walletAmount = prefs.getInt("walletAmount", -1);

                MoneySet moneySet = MoneySet.valueOf(walletAmount);
                EditText editText = (EditText) findViewById(R.id.editText_total_amount);
                String totalAmountStr = editText.getText().toString();

                if ("".equals(totalAmountStr)) {    // 入力がなかったとき
                    DialogFragment dialog = new NoInputDialogFragment();
                    dialog.show(getSupportFragmentManager(), "noInput");
                } else {                            // 入力があったとき
                    int totalAmount = Integer.parseInt(editText.getText().toString());
                    MoneySet paymentSet = moneySet.getSetForPayment(totalAmount);

                    TextView textViewPayment = (TextView) findViewById(R.id.textView_payment);
                    TextView textViewChange = (TextView) findViewById(R.id.textView_change);
                    if (paymentSet != null) {   // お金が足りたとき
                        int payment = paymentSet.getAmount();
                        textViewPayment.setText(getString(R.string.show_payment, payment));
                        textViewChange.setText(getString(R.string.show_change, payment - totalAmount));

                        DialogFragment dialog = new ConfirmDialogFragment();
                        Bundle bundle = new Bundle();
                        bundle.putInt("totalAmount", totalAmount);
                        bundle.putInt("payment", payment);
                        bundle.putInt("change", payment - totalAmount);
                        dialog.setArguments(bundle);
                        dialog.show(getSupportFragmentManager(), "confirmDialog");
                    } else {                    // お金が足りないとき
                        textViewPayment.setText(R.string.show_payment_shortage);
                        textViewChange.setText("");
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_payment:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.nav_set_wallet_amount:
                startActivity(new Intent(this, SetWalletAmountActivity.class));
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://io.github.htlsne.optimizepayment/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://io.github.htlsne.optimizepayment/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // 所持金の取得
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (prefs.contains("walletAmount")) {
            int walletAmount = prefs.getInt("walletAmount", 0);
            TextView textView = (TextView) findViewById(R.id.textView_wallet_amount);
            textView.setText(getString(R.string.show_wallet_amount, walletAmount));
        } else {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("walletAmount", 0);
            editor.apply();
        }
    }
}

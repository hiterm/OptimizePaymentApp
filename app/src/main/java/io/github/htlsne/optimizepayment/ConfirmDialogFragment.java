package io.github.htlsne.optimizepayment;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConfirmDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View layout = inflater.inflate(R.layout.fragment_confirm_dialog, null);
        TextView textViewPayment = (TextView) layout.findViewById(R.id.textView_dialog_payment);
        TextView textViewChange = (TextView) layout.findViewById(R.id.textView_dialog_change);
        final int payment = getArguments().getInt("payment");
        int change = getArguments().getInt("change");
        textViewPayment.setText(Integer.toString(payment));
        textViewChange.setText(Integer.toString(change));

        builder.setView(layout)
                .setTitle("確認")
                .setPositiveButton("支払う", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // preferenceにwalletAmountの変更を書き出す
                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
                        int currentWalletAmount = prefs.getInt("walletAmount", 0);
                        int totalAmount = getArguments().getInt("totalAmount");
                        int nextWalletAmount = currentWalletAmount - totalAmount;
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putInt("walletAmount", nextWalletAmount);
                        editor.apply();

                        TextView textView = (TextView) getActivity().findViewById(R.id.textView_wallet_amount);
                        textView.setText(getString(R.string.show_wallet_amount, nextWalletAmount));
                    }
                })
                .setNegativeButton("キャンセル", null);

        // 表示
        return builder.create();
    }

}

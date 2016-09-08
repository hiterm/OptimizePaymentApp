package io.github.htlsne.optimizepayment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;


public class NoInputDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.dialog_no_input)
                .setPositiveButton(R.string.dialog_no_input_positive_button, null);
        // Create the AlertDialog object and return it
        return builder.create();
    }
}

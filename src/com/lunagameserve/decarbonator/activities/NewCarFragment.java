package com.lunagameserve.decarbonator.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.Toast;
import com.lunagameserve.decarbonator.R;
import com.lunagameserve.decarbonator.util.DataUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by sixstring982 on 2/22/15.
 */
public class NewCarFragment extends DialogFragment {

    private String newCarName;

    @Nullable
    private Integer newCarMPG = null;

    @Nullable
    private Runnable onAcceptCallback = null;

    public void setOnAcceptCallback(Runnable callback) {
        this.onAcceptCallback = callback;
    }

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final DialogFragment self = this;
        LayoutInflater inflater = getActivity().getLayoutInflater();
        return new AlertDialog.Builder(getActivity())
                .setView(inflater.inflate(R.layout.new_car_dialog,
                        null))
                .setPositiveButton("Add",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                EditText nameText = (EditText)self.getDialog()
                                     .findViewById(R.id.new_car_dialog_carname);

                                EditText mpgText = (EditText)self.getDialog()
                                 .findViewById(R.id.new_car_dialog_mpgtextview);

                                newCarName = nameText.getText().toString();

                                if (newCarName.length() < 1 ||
                                    !DataUtil.isDigits(
                                            mpgText.getText().toString())) {
                                    Toast.makeText(
                                            getActivity().getBaseContext(),
                                            "Sorry, didn't understand that.",
                                            Toast.LENGTH_LONG).show();
                                    getDialog().cancel();
                                } else if (onAcceptCallback != null) {
                                    newCarMPG = Integer.parseInt(
                                            mpgText.getText().toString());
                                    onAcceptCallback.run();
                                }
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                getDialog().cancel();
                            }
                        }).create();
    }

    public String getNewCarName() {
        return newCarName;
    }

    public int getNewCarMPG() {
        return newCarMPG;
    }
}

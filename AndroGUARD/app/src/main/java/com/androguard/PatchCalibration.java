package com.androguard;

import android.app.AlertDialog;
import android.content.Context;

/**
 * Class containing the noise generation to be applied to values of the Android
 * Sensor class in order to obscure the builtin error
 * @author  Gergö Kranz
 * @version 1.0
 * @since   17-11-2024
 */
public class PatchCalibration {
    private static boolean test = false;

    public static void showDialog(Context context) {
        if(test) return;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Alert !");
        builder.setMessage("Do you want to exit ?");

        builder.setCancelable(true);

        builder.setPositiveButton("Start", (dialog, which) -> {
            calibrate();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> {
            dialog.cancel();
        });

        AlertDialog alertDialog = builder.create();

        alertDialog.show();
    }

    private static void calibrate() {
        test = true;
    }
}
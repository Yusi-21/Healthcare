package com.mirea.healthcare;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

public class DialogUtils {
    public static void setupButtons(DialogInterface dialog, Context context) {
        if (dialog instanceof AlertDialog) {
            setupAlertDialogButtons((AlertDialog) dialog, context);
        } else if (dialog instanceof DatePickerDialog) {
            setupPickerDialogButtons((DatePickerDialog) dialog, context);
        } else if (dialog instanceof TimePickerDialog) {
            setupPickerDialogButtons((TimePickerDialog) dialog, context);
        }
    }

    private static void setupAlertDialogButtons(AlertDialog dialog, Context context) {
        dialog.setOnShowListener(d -> {
            styleButtons(
                    dialog.getButton(DialogInterface.BUTTON_POSITIVE),
                    dialog.getButton(DialogInterface.BUTTON_NEGATIVE),
                    context
            );
        });
    }

    private static void setupPickerDialogButtons(DatePickerDialog dialog, Context context) {
        dialog.setOnShowListener(d -> {
            styleButtons(
                    dialog.getButton(DialogInterface.BUTTON_POSITIVE),
                    dialog.getButton(DialogInterface.BUTTON_NEGATIVE),
                    context
            );
        });
    }

    private static void setupPickerDialogButtons(TimePickerDialog dialog, Context context) {
        dialog.setOnShowListener(d -> {
            styleButtons(
                    dialog.getButton(DialogInterface.BUTTON_POSITIVE),
                    dialog.getButton(DialogInterface.BUTTON_NEGATIVE),
                    context
            );
        });
    }

    private static void styleButtons(Button positiveButton, Button negativeButton, Context context) {
        if (positiveButton != null) {
            positiveButton.setTextColor(ContextCompat.getColor(context, R.color.blue));
        }
        if (negativeButton != null) {
            negativeButton.setTextColor(ContextCompat.getColor(context, R.color.red));
        }
    }
}

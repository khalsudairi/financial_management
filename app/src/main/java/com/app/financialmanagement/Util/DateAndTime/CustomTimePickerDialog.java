package com.app.financialmanagement.Util.DateAndTime;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.res.Configuration;

import java.util.Locale;

public class CustomTimePickerDialog extends TimePickerDialog {

    public CustomTimePickerDialog(Context context, OnTimeSetListener listener, int hourOfDay, int minute, boolean is24HourView) {
        super(context, listener, hourOfDay, minute, is24HourView);
        setLocale(context);
    }

    public CustomTimePickerDialog(Context context, int theme, OnTimeSetListener listener, int hourOfDay, int minute, boolean is24HourView) {
        super(context, theme, listener, hourOfDay, minute, is24HourView);
        setLocale(context);
    }

    private void setLocale(Context context) {
        // Set the desired locale for the dialog
        Locale locale = new Locale("ar"); // Change "ar" to the desired locale code (Arabic in this example)
        Locale.setDefault(locale);

        Configuration configuration = new Configuration();
        configuration.setLocale(locale);

        context.getResources().updateConfiguration(configuration, context.getResources().getDisplayMetrics());
    }
}


package com.app.financialmanagement.Util.DateAndTime;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.res.Configuration;

import java.util.Locale;

public class CustomDatePickerDialog extends DatePickerDialog {

    public CustomDatePickerDialog(Context context, OnDateSetListener listener, int year, int month, int dayOfMonth) {
        super(context, listener, year, month, dayOfMonth);
       // setLocale(context);
    }

    public CustomDatePickerDialog(Context context, int theme, OnDateSetListener listener, int year, int month, int dayOfMonth) {
        super(context, theme, listener, year, month, dayOfMonth);
        //setLocale(context);
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


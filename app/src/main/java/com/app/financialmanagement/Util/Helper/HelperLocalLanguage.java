package com.app.financialmanagement.Util.Helper;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;

import java.util.Locale;

public class HelperLocalLanguage {
    public static Context setLocale(Context context, String language) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return updateResources(context, language);
        } else {
            return updateResourcesLegacy(context, language);
        }
    }

    private static Context updateResources(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Configuration configuration = context.getResources().getConfiguration();
        configuration.setLocale(locale);

        return context.createConfigurationContext(configuration);
    }

    @SuppressWarnings("deprecation")
    private static Context updateResourcesLegacy(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Configuration configuration = context.getResources().getConfiguration();
        configuration.locale = locale;

        context.getResources().updateConfiguration(configuration, context.getResources().getDisplayMetrics());

        return context;
    }
}

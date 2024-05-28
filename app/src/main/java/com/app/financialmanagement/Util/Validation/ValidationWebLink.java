package com.app.financialmanagement.Util.Validation;

import android.util.Patterns;

public class ValidationWebLink {
    public static Boolean validateLink(String link) {

        if (!link.isEmpty() && Patterns.WEB_URL.matcher(link).matches()) {
            // Valid link
            return  true;
        } else {
            // Invalid link
            return  false;
        }
    }
}

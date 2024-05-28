package com.app.financialmanagement.Util.Validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidator {
    public static boolean isValidEmail(String email) {
        String EMAIL_REGEX =
                "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}

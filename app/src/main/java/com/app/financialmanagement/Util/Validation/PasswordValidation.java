package com.app.financialmanagement.Util.Validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordValidation {
    public static boolean validatePassword(String password) {
        // Minimum length of 8 characters
        if (password.length() < 8) {
            return false;
        }

        // At least one lowercase letter
        Pattern lowercasePattern = Pattern.compile("[a-z]");
        Matcher lowercaseMatcher = lowercasePattern.matcher(password);
        if (!lowercaseMatcher.find()) {
            return false;
        }

        // At least one uppercase letter
        Pattern uppercasePattern = Pattern.compile("[A-Z]");
        Matcher uppercaseMatcher = uppercasePattern.matcher(password);
        if (!uppercaseMatcher.find()) {
            return false;
        }

        // At least one special character
        Pattern specialCharPattern = Pattern.compile("[!@#$%^&*()\\-_+=<>?/{}~]");
        Matcher specialCharMatcher = specialCharPattern.matcher(password);
        if (!specialCharMatcher.find()) {
            return false;
        }

        // At least one digit
        Pattern digitPattern = Pattern.compile("\\d");
        Matcher digitMatcher = digitPattern.matcher(password);
        if (!digitMatcher.find()) {
            return false;
        }

        // All criteria met, password is valid
        return true;
    }

}

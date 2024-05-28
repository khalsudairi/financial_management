package com.app.financialmanagement.Util.Validation;

public class ValidationPhoneNumber {

    public static Boolean validatePhoneNumber(String phoneNumber) {
        // Regular expression for Saudi Arabian phone numbers
        String saudiRegex = "^((\\+966)|(00966)|0)?5[0-9]{8}$";

        if (!phoneNumber.isEmpty() && phoneNumber.matches(saudiRegex)) {
            // Valid Saudi Arabian phone number
            return true;
        } else {
            // Invalid phone number
            return false;
        }
    }
}

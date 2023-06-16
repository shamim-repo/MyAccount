package com.telentandtech.myaccount.core;

import java.util.regex.Pattern;

public class DataValidation {
    public static boolean emailValidation(String emailAddress) {
        return Pattern.compile("^(.+)@(\\S+)$").matcher(emailAddress).matches();
    }
}

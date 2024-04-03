package com.example.websocialnetwork.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.websocialnetwork.common.Const.PAGE_SIZE;

public class Validation {
    public static String checkUserName(String user){
        if(user.trim().isEmpty()){
            return "User name cannot be blank";
        }
        if(user.length() < 3 || user.length() > 15){
            return "User name must be between 5 and 15 words in length";
        }
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9._-]{3,}$");
        Matcher matcher = pattern.matcher(user);
        if(matcher.matches()){
            return "Username cannot contain forbidden characters";
        }
        return null;
    }

    public static String checkBirthday(String birthday){
        if(birthday == null || birthday.isEmpty()){
            return null;
        }
        try {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate date = LocalDate.parse(birthday,dateFormatter);
            if (!(date.compareTo(LocalDate.now()) < 0 )){
                return "Date of birth must be before current date";
            }
            return null;
        } catch (Exception ex){
            return "Invalid date of birth or incorrect format yyyy-MM-dd";
        }
    }
    public static int calculateTotalPages(long totalItems) {
        if (totalItems <= 0) {
            return 0;
        }

        return (int) Math.ceil((double) totalItems / PAGE_SIZE);
    }
}

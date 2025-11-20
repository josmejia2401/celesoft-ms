package com.jac.utils;

import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@UtilityClass
public class Helpers {
    public static long generateUniqueNumberDataBase() {
        return generateUniqueNumber(12);
    }

    public static long generateUniqueNumber(int n) {
        if (n < 8) {
            throw new IllegalArgumentException("El parámetro 'n' debe ser un entero mayor o igual a 8.");
        }
        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd");
        String datePrefix = now.format(formatter);
        int randomDigits = n - 6;
        long currentTimeMillis = System.currentTimeMillis();
        String timeComponent = Long.toString(currentTimeMillis);
        if (timeComponent.length() > randomDigits) {
            timeComponent = timeComponent.substring(timeComponent.length() - randomDigits);
        }
        Random random = new Random();
        String randomComponent = String.format("%0" + randomDigits + "d", random.nextInt((int) Math.pow(10, randomDigits)));
        String numeric = datePrefix + timeComponent;
        if (numeric.length() < n) {
            numeric += randomComponent.substring(0, n - numeric.length());
        } else if (numeric.length() > n) {
            numeric = numeric.substring(0, n);
        }
        if (numeric.charAt(0) == '0') {
            numeric = (random.nextInt(9) + 1) + numeric.substring(1);
        }
        return Long.parseLong(numeric);
    }
}

package com.celesoft.utils;

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

        // Prefijo de fecha: YYMMDD (6 dígitos)
        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd");
        String datePrefix = now.format(formatter);

        // Parte aleatoria (n - 6 dígitos)
        int randomDigits = n - 6;
        long currentTimeMillis = System.currentTimeMillis();
        String timeComponent = Long.toString(currentTimeMillis);
        if (timeComponent.length() > randomDigits) {
            timeComponent = timeComponent.substring(timeComponent.length() - randomDigits);
        }

        // Componente aleatorio
        Random random = new Random();
        String randomComponent = String.format("%0" + randomDigits + "d", random.nextInt((int) Math.pow(10, randomDigits)));

        // Combinación de fecha + timestamp parcial
        String numeric = datePrefix + timeComponent;

        // Ajustar longitud
        if (numeric.length() < n) {
            numeric += randomComponent.substring(0, n - numeric.length());
        } else if (numeric.length() > n) {
            numeric = numeric.substring(0, n);
        }

        // Asegurar que no empiece con 0
        if (numeric.charAt(0) == '0') {
            numeric = (random.nextInt(9) + 1) + numeric.substring(1);
        }

        return Long.parseLong(numeric);
    }
}

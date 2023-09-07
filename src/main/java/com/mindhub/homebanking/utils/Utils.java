package com.mindhub.homebanking.utils;

public class Utils {

    public int getRandomNumber(int max, int min) {
        return (int) (Math.random() * (max - min) + min);
    }

    public String getRandomNumberCard () {
        return getRandomNumber(9999,1000) + "-" +
                getRandomNumber(9999,1000) + "-" +
                getRandomNumber(9999,1000) + "-" +
                getRandomNumber(9999,1000);
    }
}

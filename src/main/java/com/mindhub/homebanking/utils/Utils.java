package com.mindhub.homebanking.utils;

public class Utils {

    public int getRandomNumber(int max, int min) {
        return (int) (Math.random() * (max - min) + min);
    }
}

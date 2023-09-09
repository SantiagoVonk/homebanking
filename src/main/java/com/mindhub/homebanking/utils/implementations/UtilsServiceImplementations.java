package com.mindhub.homebanking.utils.implementations;


import com.mindhub.homebanking.utils.UtilsService;
import org.springframework.stereotype.Service;

@Service
public class UtilsServiceImplementations implements UtilsService {
    @Override
    public int getRandomNumber(int max, int min) {
        return (int) (Math.random() * (max - min) + min);
    }

    @Override
    public String getRandomNumberCard() {
        return getRandomNumber(9999,1000) + "-" +
                getRandomNumber(9999,1000) + "-" +
                getRandomNumber(9999,1000) + "-" +
                getRandomNumber(9999,1000);
    }

}

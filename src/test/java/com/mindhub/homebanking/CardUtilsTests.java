package com.mindhub.homebanking;

import com.mindhub.homebanking.utils.implementations.UtilsServiceImplementations;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.criteria.CriteriaBuilder;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.Is.isA;
import static org.hamcrest.core.IsNot.not;

@SpringBootTest
public class CardUtilsTests {

    @Autowired
    private UtilsServiceImplementations utilsServiceImplementations;

    @Test
    public void cardNumberIsCreated() {
        String cardNumber = utilsServiceImplementations.getRandomNumberCard();
        assertThat(cardNumber,is(not(emptyOrNullString())));
    }

    @Test
    public void cardNumberIsString() {
        String cardNumber = utilsServiceImplementations.getRandomNumberCard();
    assertThat(cardNumber,isA(String.class));
    }

    @Test
    public void cardCVVIsCreated() {
        Integer cvv = utilsServiceImplementations.getRandomNumber(999, 111);
        assertThat(cvv,isA(Integer.class));
    }
}

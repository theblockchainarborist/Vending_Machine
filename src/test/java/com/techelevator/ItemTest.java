package com.techelevator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.techelevator.model.Item;

import java.math.BigDecimal;

public class ItemTest {
    Item potatoChip;
    Item moonpie;
    Item cowtales;
    Item cola;
    Item bigLeague;

    @Before
    public void setup(){
        potatoChip = new Item("A1", "Potato Crisps",  new BigDecimal(3.05), "Chip");
        moonpie = new Item("B1", "Moonpie",  new BigDecimal(1.80), "Candy");
        cowtales = new Item("B2", "Cowtales",  new BigDecimal(1.50), "Candy");
        cola = new Item("C1", "Cola",  new BigDecimal(1.25), "Drink");
        bigLeague = new Item("C2", "Big League Chew",  new BigDecimal(1.50), "Gum");

    }
    @Test
    public void does_getSlotID_return_correct_String(){
        String expected = "A1";
        String actual = potatoChip.getSlotID();
        Assert.assertEquals(expected,actual);
    }

    @Test
    public void does_getName_return_correct_String(){
        String expected = "Cola";
        String actual = cola.getName();
        Assert.assertEquals(expected,actual);
    }

    @Test
    public void does_getPrice_return_correct_String(){
        BigDecimal expected = new BigDecimal(3.05);
        BigDecimal actual = potatoChip.getPrice();
        Assert.assertEquals(expected,actual);
    }

    @Test
    public void does_getType_return_correct_String(){
        String expected = "Drink";
        String actual = cola.getType();
        Assert.assertEquals(expected,actual);
    }

    @Test
    public void does_toString_return_correct_String(){
        String expected = "C1: Cola, price= $1.25";
        String actual = cola.toString();
        Assert.assertEquals(expected,actual);
    }

    @Test
    public void does_dispensingItemNameAndPrice_return_correct_String(){
        String expected = "Cola, price= $1.25";
        String actual = cola.dispensingItemNameAndPrice();
        Assert.assertEquals(expected,actual);
    }

    @Test
    public void does_getMessage_return_correct_String(){
        String expectedChip = "\nCrunch Crunch, Yum!";
        String expectedCandy = "\nMunch Munch, Yum!";
        String expectedDrink = "\nGlug Glug, Yum!";
        String expectedGum = "\nChew Chew, Yum!";

        String actualChip = potatoChip.getMessage();
        String actualCandy = cowtales.getMessage();
        String actualDrink = cola.getMessage();
        String actualGum = bigLeague.getMessage();

        Assert.assertEquals("Chip message didn't match", expectedChip,actualChip);
        Assert.assertEquals("Candy message didn't match", expectedCandy,actualCandy);
        Assert.assertEquals("Drink message didn't match", expectedDrink,actualDrink);
        Assert.assertEquals("Gum message didn't match", expectedGum,actualGum);
    }
}

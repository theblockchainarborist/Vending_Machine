package com.techelevator.model;

import java.math.BigDecimal;

public class Item {

    private String slotID;
    private String name;
    private BigDecimal price;
    private String type;

    public String getSlotID() {
        return slotID;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getType() {
        return type;
    }

    public Item(String slotID, String name, BigDecimal price, String type) {
        this.slotID = slotID;
        this.name = name;
        this.price = price;
        this.type = type;
    }

    @Override
    public String toString() {
        return slotID + ": " + name + ", price= $" + price.setScale(2);
    }

    public String dispensingItemNameAndPrice(){
        return name + ", price= $" + price.setScale(2);
    }

    public String getMessage(){
        String result = "";

        if (type.equalsIgnoreCase("chip")){result = "\nCrunch Crunch, Yum!";}

        if (type.equalsIgnoreCase("candy")){result = "\nMunch Munch, Yum!";}

        if (type.equalsIgnoreCase("drink")){result = "\nGlug Glug, Yum!";}

        if (type.equalsIgnoreCase("gum")){result = "\nChew Chew, Yum!";}

        return result;
    }
}

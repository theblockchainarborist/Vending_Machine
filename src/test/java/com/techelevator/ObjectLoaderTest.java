package com.techelevator;

import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;

import com.techelevator.model.Item;
import com.techelevator.utilities.ObjectLoader;

import java.io.File;
import java.math.BigDecimal;
import java.util.Map;
import java.util.TreeMap;

public class ObjectLoaderTest extends TestCase {


    @Test
    public void testGenerateInventoryItems() {

        String testString = "A1|Potato Crisps|3.05|Chip\n" + "A2|Stackers|1.45|Chip\n" + "A3|Grain Waves|2.75|Chip\n";
        Map<String, Item> expected = new TreeMap<>();

        Item expectedItem = null;
        Item actual = null;

        String[] splitTestString = testString.split("\\n");

        // Split at the new line
        for (int i = 0; i < 1; i++) {
            String thisString = splitTestString[i];
            String[] thisStringSplit = thisString.split("\\|");

            String key = "";
            String name = "";
            BigDecimal cost = null;
            String type = "";

            for (int j = 0; j < 1; j++) {
                switch(i) {
                    case (0): {
                        key = thisStringSplit[0];
                    }
                    case (1): {
                        name = thisStringSplit[1].toString();
                    }
                    case (2): {
                        cost = BigDecimal.valueOf(Double.parseDouble(thisStringSplit[2]));
                    }
                    case (3): {
                        type = thisStringSplit[3];
                    }
                }
                Item item = new Item(key, name, cost, type);
                expected.put(key, item);
            }
        }

        // Here we get the desired item from the expected map.
        for (String expectedkey : expected.keySet()) {
            if (expectedkey.equalsIgnoreCase("A1")) {
                expectedItem = expected.get(expectedkey);
            }
        }

        File file = new File("src/test/java/com/techelevator/resources/ObjectLoaderTestFile.csv");
        ObjectLoader objectLoader = new ObjectLoader();
        Map<String, Item> response = objectLoader.generateInventoryItems(file);

        // Here we get the desired from the actual response.
        for (String itemKey : response.keySet()) {
            if (itemKey.equalsIgnoreCase("A1")) {
                System.out.println(itemKey);
                actual = response.get(itemKey);
            }
        }
        Assert.assertEquals(expectedItem.toString(), actual.toString());
    }
}
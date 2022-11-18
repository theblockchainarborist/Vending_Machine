package com.techelevator.utilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.*;

import com.techelevator.model.Item;

public class ObjectLoader {
    public ObjectLoader(){};

    public static Map<String, Item> generateInventoryItems(File inventoryFile){
        Map<String, Item> result = new TreeMap<>();

        try(Scanner loader = new Scanner(inventoryFile)){
            while (loader.hasNextLine()){
                String[] itemProperties = loader.nextLine().split("\\|");

                for (int i = 0; i < itemProperties.length; i++) {
                    itemProperties[i] = itemProperties[i].trim();
                }

                if(!itemProperties[0].startsWith("#")){
                    Item newItem = (new Item(itemProperties[0], itemProperties[1], BigDecimal.valueOf(Double.parseDouble(itemProperties[2])), itemProperties[3]));
                    result.put(newItem.getSlotID().toLowerCase(), newItem);
                }
            }
        }catch (FileNotFoundException fnf){
            System.out.println(fnf.getMessage());
        }
        return result;
    }
}


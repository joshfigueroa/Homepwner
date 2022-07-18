package com.rowan.homepwner;

import android.content.ContentValues;
import android.content.Context;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;

public class ItemLab {
    private static ItemLab sItemLab;
    private static Context mContext;

    private final List<Item> mItems;

    ArrayList<String> mItemAdj = new ArrayList<>();

    List<String> adjList = Arrays.asList("Elite", "Sleepy", "Jolly", "Tense", "Broken", "Careful", "Cheap",
            "Imported", "Trite", "Perfect", "Spiffy", "Quirky", "Glorious", "Violent");

    ArrayList<String> mItemNoun = new ArrayList<>();

    List<String> nounList = Arrays.asList("Donkey", "Vegetable", "Nest", "Iron", "Jeans", "Watch",
            "Train", "Sheep", "Dime", "Cabbage", "Bag", "Horse", "Bed", "Hammer");



    Random random = new Random();

    public static ItemLab get(Context context) {
        mContext = context.getApplicationContext();
        if (sItemLab == null) {
            sItemLab = new ItemLab(context);
        }
        return sItemLab;
    }

    private ItemLab(Context context) {
        mItems = new ArrayList<>();

        mItemAdj.addAll(adjList);
        mItemNoun.addAll(nounList);


        for (int i = 0; i < 100; i++) {
            Item item = new Item();
            item.setName(mItemAdj.get(random.nextInt(14)) + " " +
                    mItemNoun.get(random.nextInt(14)));
            item.setSerial(item.getId().toString().toUpperCase().substring(1,9));
            item.setValue("$" + random.nextInt(90));
            mItems.add(item);
        }

    }

    public void addItem(Item item) {
        mItems.add(item);
    }

    public List<Item> getItems() {
        return mItems;
    }

    public Item getItem(UUID id) {
        for (Item item : mItems) {
            if (item.getId().equals(id)) {
                return item;
            }
        }
        return null;
    }

    public File getPhotoFile(Item item) {
        File filesDir = mContext.getFilesDir();
        return new File(filesDir, item.getPhotoFilename());
    }

    public void updateItem(Item item) {
        String uuidString = item.getId().toString();
    }
}
package com.rowan.homepwner;

import java.util.Date;
import java.util.UUID;

public class Item {

    private final UUID mId;
    private String mName;
    private String mSerialNumber;
    private String mValueInDollars;
    private Date mDateCreated;




    public Item() {
        mId = UUID.randomUUID();
        mDateCreated = new Date();
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getSerial() {
        return mSerialNumber;
    }

    public void setSerial(String serial) {
        mSerialNumber = serial;
    }

    public String getValue() {
        return mValueInDollars;
    }

    public void setValue(String value) {
        mValueInDollars = value;
    }

    public UUID getId() {
        return mId;
    }

    public Date getDate() {
        return mDateCreated;
    }

    public void setDate(Date date) {
        mDateCreated = date;
    }

    public String getPhotoFilename() {
        return "IMG_" + getId().toString() + ".jpg";
    }

}

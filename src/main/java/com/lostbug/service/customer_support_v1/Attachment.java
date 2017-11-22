package com.lostbug.service.customer_support_v1;

public class Attachment {
    private String mName;
    private byte[] mContents;

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public byte[] getContents() {
        return mContents;
    }

    public void setContents(byte[] contents) {
        mContents = contents;
    }
}

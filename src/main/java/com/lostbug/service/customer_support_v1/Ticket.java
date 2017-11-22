package com.lostbug.service.customer_support_v1;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class Ticket {
    private String mCustomerName;
    private String mSubject;
    private String mBody;
    private Map<String, Attachment> mAttachments;

    public Ticket() {
        mAttachments = new LinkedHashMap<>();
    }

    public String getCustomerName() {
        return mCustomerName;
    }

    public void setCustomerName(String customerName) {
        mCustomerName = customerName;
    }

    public String getSubject() {
        return mSubject;
    }

    public void setSubject(String subject) {
        mSubject = subject;
    }

    public String getBody() {
        return mBody;
    }

    public void setBody(String body) {
        mBody = body;
    }

    public Attachment getAttachment(String name) {
        return this.mAttachments.get(name);
    }

    public Collection<Attachment> getAttachments() {
        return this.mAttachments.values();
    }

    public void addAttachment(Attachment attachment) {
        this.mAttachments.put(attachment.getName(), attachment);
    }

    public int getNumberOfAttachments() {
        return this.mAttachments.size();
    }
}

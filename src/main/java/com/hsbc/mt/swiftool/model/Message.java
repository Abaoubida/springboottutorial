package com.hsbc.mt.swiftool.model;

import java.util.List;


public class Message {
    public Message() {
    }

    private String value;
    private String type;
    private String sender;
    private String receiver;
    private List<SwiftField> swiftFields;

    public List<SwiftField> getSwiftFields() {
        return swiftFields;
    }

    public void setSwiftFields(List<SwiftField> swiftFields) {
        this.swiftFields = swiftFields;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}

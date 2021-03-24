package com.hsbc.mt.swiftool.service;

import com.hsbc.mt.swiftool.model.Bloc4;
import com.hsbc.mt.swiftool.model.Message;
import com.hsbc.mt.swiftool.model.SwiftField;
import com.prowidesoftware.swift.model.AbstractMessage;
import com.prowidesoftware.swift.model.SwiftMessage;
import com.prowidesoftware.swift.model.Tag;
import com.prowidesoftware.swift.model.field.*;
import com.prowidesoftware.swift.model.mt.AbstractMT;
import com.prowidesoftware.swift.model.mt.mt1xx.MT103;
import com.prowidesoftware.swift.model.mt.mt4xx.MT400;
import com.prowidesoftware.swift.model.mt.mt4xx.MT410;
import com.prowidesoftware.swift.model.mt.mt4xx.MT499;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

@Service
public class MessageService {

    Locale locale=Locale.getDefault();

    public SwiftMessage parseMessage(String messageToParse) throws IOException {

        SwiftMessage sm=SwiftMessage.parse(messageToParse);
        return sm;
    }

    public String getMessageSender(SwiftMessage sm){

        String sender=sm.getSender();
        return sender;
    }

    public String getMessageReceiver(SwiftMessage sm){

        String receiver=sm.getReceiver();
        return receiver;
    }


    public String getMessageType(SwiftMessage sm){
        String messageType=sm.getType();

        return messageType;
    }


    public List<SwiftField> getFields(SwiftMessage sm){

        List<SwiftField>  fieldsList= new ArrayList<SwiftField>();

        for (Tag tag : sm.getBlock4().getTags()) {
            Field field = tag.asField();
            SwiftField sf= new SwiftField();
            sf.setFieldName(field.getLabel(field.getName(), sm.getType(), null, locale));
            sf.setFieldValue(field.getValueDisplay(locale));
            fieldsList.add(sf);
        }

        return fieldsList;
    }



    public Message formatMessage(String message){

        Message swiftMessage =new Message();

        SwiftMessage sm= new SwiftMessage();
        try {
            sm = parseMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        swiftMessage.setValue(message);
        swiftMessage.setSender(getMessageSender(sm));
        swiftMessage.setReceiver(getMessageReceiver(sm));
        swiftMessage.setType(getMessageType(sm));
        swiftMessage.setSwiftFields(getFields(sm));

        return swiftMessage;
    }

    public SwiftMessage formatMessage2(String message){


        SwiftMessage sm= new SwiftMessage();
        try {
            sm = parseMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sm;
    }

    public AbstractMessage formatMessage3(String message) throws IOException{

            AbstractMessage sm= AbstractMT.parse(message);
            return sm;
    }

    public AbstractMT getMessageByType(String messageType){

        AbstractMT mt;

        switch (messageType){

            case "103":{
                mt=new MT103();
                mt.setSender("");
                mt.addField(new Field20(""));
                mt.addField(new Field23B(""));

                Field32A f32A = new Field32A()
                        .setDate(Calendar.getInstance())
                        .setCurrency("")
                        .setAmount("");
                mt.addField(f32A);

                Field50A f50A = new Field50A()
                        .setAccount("")
                        .setBIC("");
                mt.addField(f50A);

                Field59 f59 = new Field59()
                        .setAccount("")
                        .setNameAndAddress("");
                mt.addField(f59);

                mt.addField(new Field71A(""));

                break;
            }
            case "410":{
                mt=new MT410();
                break;
            }

            default:{
                mt=new MT499();
                break;
            }
        }

        return mt;
    }
}

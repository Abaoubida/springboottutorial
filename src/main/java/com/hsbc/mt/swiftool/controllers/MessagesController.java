package com.hsbc.mt.swiftool.controllers;

import com.hsbc.mt.swiftool.service.MessageService;
import com.prowidesoftware.swift.model.SwiftMessage;
import com.prowidesoftware.swift.model.Tag;
import com.prowidesoftware.swift.model.field.*;
import com.prowidesoftware.swift.model.mt.mt1xx.MT103;
import com.prowidesoftware.swift.model.mt.mt4xx.MT410;
import com.prowidesoftware.swift.model.mt.mt4xx.MT499;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

@Controller
public class MessagesController {

    Locale locale = Locale.ENGLISH;
//    String message="{1:F01BACOARB1A0B20000000000}{2:I103ADRBNL21XXXXU2}{3:{108:FOOB3926BE868XXX}}{4:\n" +
//            ":20:REFERENCE\n" +
//            ":23B:CRED\n" +
//            ":32A:180730USD1234567,89\n" +
//            ":50A:/12345678901234567890\n" +
//            "CFIMHKH1XXX\n" +
//            ":59:/12345678901234567890\n" +
//            "JOE DOE\n" +
//            "MyStreet 1234\n" +
//            ":71A:OUR\n" +
//            "-}{5:{CHK:3916EF336FF7}}";

    @Autowired
    MessageService service;

    @GetMapping("/parse")
    public String getMessage(Model model) {
        model.addAttribute("swiftMessage", "");

        return "parse-message";
    }

    @PostMapping("/format")
    public String formatMessage(Model model, @RequestParam String swiftMessage) throws IOException {

        SwiftMessage entity = service.formatMessage2(swiftMessage);
        model.addAttribute("message", entity);

        List<Tag> tags = entity.getBlock4().getTags();
        model.addAttribute("tags", tags);

        List<Field> fields = new ArrayList<Field>();
        for (Tag tag : tags) {
            Field field = tag.asField();

            fields.add(field);
        }
        model.addAttribute("fields", fields);

        model.addAttribute("locale", locale);

        return "result";

    }

    @GetMapping("/build")
    public String showBuildMessagePage(Model model) {

        return "build-message";
    }

    @PostMapping("/build")
    public String getMessageFormById(Model model, @RequestParam String messageType) {
        switch (messageType) {

            case "103": {

                MT103 mt=new MT103();
                mt.setSender("BACOARB1A0B2");
                mt.addField(new Field20(""));
                mt.addField(new Field23B(""));

                Field32A f32A = new Field32A()
                        .setDate(Calendar.getInstance())
                        .setCurrency("EUR")
                        .setAmount("00.00");
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
                model.addAttribute("locale", locale);
                model.addAttribute("mt", mt);
                break;
            }
            case "410": {
                model.addAttribute("locale", locale);
                model.addAttribute("mt", new MT410());
                break;
            }

            default: {
                model.addAttribute("locale", locale);
                model.addAttribute("mt", new MT499());
                break;
            }

        }
        return "/result-build";
    }
}

package com.jgazula.easyresources.core.enhancedresourcebundle;

import org.junit.jupiter.api.Test;

import java.text.ChoiceFormat;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.MessageFormat;

public class ERBGeneratorTests {

    @Test
    public void test() {
        String msg = "On the date {0,date} at time {1,time}, I had {2,number,currency} cakes for {3}. Blah {4,choice,0#none|1#thing|1<things}";
        MessageFormat messageFormat = new MessageFormat("");
        messageFormat.applyPattern(msg);


        for (Format format : messageFormat.getFormatsByArgumentIndex()) {
//            System.out.println(format);
            if (format instanceof DateFormat) {
//                System.out.println("date format!");
            } else if (format instanceof DecimalFormat) {
//                System.out.println("decimal format!");
            } else if (format instanceof ChoiceFormat) {
//                System.out.println("choice format!");
            } else {
//                System.out.println("null format!");
            }
        }
    }
}

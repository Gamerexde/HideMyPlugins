package com.gamerexde.hidemypluginsbungee.Utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateGenerator {
    public static String getDate() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        String date = dtf.format(now);

        return date;
    }
}

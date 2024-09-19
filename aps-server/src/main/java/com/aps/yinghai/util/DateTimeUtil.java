package com.aps.yinghai.util;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {


    public static LocalDate parseDate(String dateStr,String formatter){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(formatter);
        return LocalDate.parse(dateStr, dateTimeFormatter);
    }

    public static LocalTime parseTime(String dateStr, String formatter){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(formatter);
        return LocalTime.parse(dateStr, dateTimeFormatter);
    }

}

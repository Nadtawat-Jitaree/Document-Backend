package com.document.utility;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.springframework.stereotype.Component;

@Component
public class DateUtils {

    public static String getCurrentDate(){
    	Date currentDate = new Date();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
        return  dateFormatter.format(currentDate);
    }
    
    public static String getCurrentDateTime(){
    	Date currentDate = new Date();
    	SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return  dateFormatter.format(currentDate);
    }

    public static Date getNowDate(){
        try {
            String formatDate = "dd/MM/yyyy HH:mm:ss";
            DateFormat dateFormat = new SimpleDateFormat(formatDate);
            Date strDate = dateFormat.parse(getCurrentDateTime());

            Calendar calendar = new GregorianCalendar();
            calendar.setTime(strDate);
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int date = calendar.get(Calendar.DATE);

            if (year > 2300) {
                year -= 543;
            } else if (year < 1700) {
                year += 543;
            }

            calendar.set(year, month, date);
            strDate = calendar.getTime();

            return strDate;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public static Date checkDateFormat(Date dateTransaction){
        try {
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(dateTransaction);
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int date = calendar.get(Calendar.DATE);

            if (year > 2300) {
                year -= 543;
            } else if (year < 1700) {
                year += 543;
            }

            calendar.set(year, month, date);
            dateTransaction = calendar.getTime();

        } catch (Exception ex) {
            
        }

        return dateTransaction;
    }
    
    public static Date convertStringToDate(String dateString, String format) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date convertedCurrentDate = sdf.parse(dateString);
        return convertedCurrentDate;
    }
}

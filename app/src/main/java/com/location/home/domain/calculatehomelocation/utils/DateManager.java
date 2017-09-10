package com.location.home.domain.calculatehomelocation.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateManager {

    public boolean isTodayWeekend(){

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        if (day == Calendar.SUNDAY || day == Calendar.SATURDAY)
            return true;

        return false;

    }

    public String getCurrentDate(){

        return new SimpleDateFormat("yyyy-MM-dd").format(new Date());

    }

}

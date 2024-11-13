package kiss.depot.util;

/*
* 提供和时间相关的工具类
* author: koishikiss
* launch: 2024/11/1
* last update: 2024/11/1
* */

import kiss.depot.config.exceptionConfig.exceptions.ParamCheckException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Deprecated
public class DateUtil {

    public static final SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
    public static final SimpleDateFormat mouth_dayFormat = new SimpleDateFormat("MM-dd");
    public static final SimpleDateFormat dayFormat = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String getCurrentTime() {
        return timeFormat.format(new Date());
    }

    public static boolean checkStringByFormat(String date, SimpleDateFormat format) {
        try {
            dayFormat.parse(date);
        } catch (ParseException e) {
            throw new ParamCheckException("日期格式错误!\n格式:" + format);
        }
        return true;
    }

    public static String addDaysOnString(String date,int day) {
        try {
            Date oldDate = dayFormat.parse(date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(oldDate);
            calendar.add(Calendar.DATE,day);
            return dayFormat.format(calendar.getTime());
        } catch (ParseException e) {
            throw new ParamCheckException("日期格式错误!\n格式:yyyy-MM-dd");
        }
    }

    public static String switchTimeToMouth_Day(Date time) {
        return mouth_dayFormat.format(time);
    }

}

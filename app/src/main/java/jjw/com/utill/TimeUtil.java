package jjw.com.utill;

import android.util.Log;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by YSK on 2016-12-28.
 */

public class TimeUtil {
    public static Long MILLIS_PER_ONE_HOUR = ((long) 1000 * 60 * 60 * 1);

    private static String TAG = TimeUtil.class.getSimpleName();
    /**
     * convert hour of time to 0
     * ex: 2016.02.23 13:33:44 => 2016.02.23 00:00:00
     * @param orgTimeVal
     * @return
     */
    public static long getLongMilliValByDay(long orgTimeVal){
        return ( orgTimeVal - ( orgTimeVal % (MILLIS_PER_ONE_HOUR * 24) ) - (MILLIS_PER_ONE_HOUR * 9) );
    }

    /**
     * last Monday or tuesday ...
     * @param cal
     * @param trgtDayOfWeek
     * @return ex : 2016.02.23 00:00:00
     */
    public static Calendar getLastDayOfWeek(Calendar cal, int trgtDayOfWeek){
        int cur_day_of_week = cal.get ( Calendar.DAY_OF_WEEK ); // current day of week

        int check_date = trgtDayOfWeek - cur_day_of_week;
        if(check_date > 0){
            check_date -= 7;
        }
        cal.add(Calendar.DAY_OF_WEEK, check_date );
        cal.setTime(new Date(TimeUtil.getLongMilliValByDay(cal.getTime().getTime()))); ;

        return cal;
    }

    public static long getTodayMilli(){
        Calendar calendar = Calendar.getInstance ( );
        return calendar.getTime().getTime();
    }
}

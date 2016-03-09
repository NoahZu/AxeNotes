package noahzu.github.io.axenotes.utils;

import java.util.Date;

/**
 * Created by Administrator on 2016/3/7.
 */
public class TimeUtils {

    public static long getOnlyIdByDate(){
        Date date = new Date();
        return date.getTime();
    }
}

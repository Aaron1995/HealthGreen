package com.aarondesign.healthgreen.Util;

/**
 * Created by Aaron on 2016/4/27 0027.
 */
public class StringUtil {

    public static String getCarKind(int i){
        String s = "";
        switch (i){
            case 2:
                s = "小轿车  汽  1.4L以下";
                break;
            case 3:
                s = "小轿车  汽  1.4-2.0L";
                break;
            case 4:
                s = "小轿车  汽  2.0以上";
                break;
            case 5:
                s = "公交车  汽";
                break;
            case 6:
                s = "小型货车  石油气  2.5t以下";
                break;
            case 7:
                s = "轻型货车  汽  3.5t以下";
                break;
            case 8:
                s = "轻型货车  柴  3.5t以下";
                break;
            case 9:
                s = "重型货车  汽  3.5t以上";
                break;
            case 10:
                s = "重型货车  柴  3.5t以上";
                break;
        }
        return s;
    }

}

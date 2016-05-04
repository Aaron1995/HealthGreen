package com.aarondesign.healthgreen.Util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.aarondesign.healthgreen.Acitivitys.HomeNew;
import com.aarondesign.healthgreen.R;
import com.aarondesign.healthgreen.Static.Configs;

/**
 * Created by Aaron on 2016/3/10 0010.
 */
public class TransitionsUtil {

    public static void TranslateOfActivity(Activity activity, Class example, int style) {
//            Context context = activity.getBaseContext();
        Intent i = new Intent(activity, example);
        i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        activity.startActivity(i);
        switch (style) {
            case Configs.TRANSLATE_STYLE_MAIN:
                activity.overridePendingTransition(R.anim.ts_main_in, R.anim.ts_main_out);
                break;
            case Configs.TRANSLATE_STYLE_MAIN_BACK:
                activity.overridePendingTransition(R.anim.ts_main_in_back, R.anim.ts_main_out_back);
                break;
            case Configs.TRANSLATE_STYLE_POP:
                activity.overridePendingTransition(R.anim.ts_horizontal3,R.anim.no_main);
            case Configs.TRANSLATE_STYLE_POP_BACK:
                activity.overridePendingTransition(R.anim.no_main,R.anim.ts_horizontal2);
        }

    }

}

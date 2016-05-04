package com.aarondesign.healthgreen.Static;

/**
 * Created by Aaron on 2015/11/5 0005.
 */
public class Configs {
    public static final String URL_HEAD = "http://139.129.30.49:8080/HealWalkly2/";                 //  所有URL的Head
    public static final String URL_CAR_DATAS_OF_USER = "CarDatasOfUser.jsp?user_id=";               //  获取用户的car信息
    public static final String URL_PERSON_DATAS_OF_USER = "PersonDatasOfUser.jsp?user_id=";         //  获取用户的person信息
    public static final String URL_LOGIN_USER = "UserGetLoginStatus.jsp?";                          //  获取用户的登录状态   成功为status=1 失败为0
    public static final String URL_ROADINFO_OF_STATUS = "RoadGetStatusInfo.jsp?status=";            //  获取杭州道路信息
    public static final String URL_CHECK_USER = "UserGetCheckStatus.jsp?phone=";                    //  获取该手机号是否已注册
    public static final String URL_UPLOAD_USER = "UserPutDatasStatus.jsp?";                         //  注册用户信息
    public static final String URL_MODIFY_USER = "UserGetModifyStatus.jsp?";                        //  修改用户信息
    public static final String URL_EXHAUST_OF_CACULATE = "CarGetExhaustDetails.jsp?";               //  提交数据获取算法得到的结果
    public static final String URL_PUT_CAR_DATAS = "CarPutDatasStatus.jsp?";                        //  往car表插入数据
    public static final String URL_UPDATE_CAR_DATAS = "CarUpdateDatasStatus.jsp?";                  //  往car表更新数据

    /**
     * status
     */
    public static final String LOGIN_STATUS = "login_status";
    public static final String LOGIN_DATETIME = "login_datetime";
    public static final int LOGINED_SUCCESS = 1;          //   已登录状态
    public static final int LOGINED_FAILURE = 0;          //   未登录状态
    public static final String CONNECTION_STATUS = "connection_status";
    public static final int CONNECTION_SUCCESS = 1;
    public static final int CONNECTION_FAILURE = 0;
    public static final int CONNECTION_ERROR = 2;

    public static int REGISTER_STATUS = -1;         //  setting页面处于register跳转状态
    public static int USER_STATUS = -2;             //  setting页面处于User跳转状态

    /**
     * translate
     */
    public static final int TRANSLATE_USER = 1;             //跳转user页面
    public static final int TRANSLATE_CAR = 2;              //跳转car页面
    public static final int TRANSLATE_MAP = 3;              //跳转map页面
    public static final int TRANSLATE_PERSON = 4;           //跳转person页面
    public static final int TRANSLATE_SETTING = 5;          //跳转setting页面
    public static final int TRANSLATE_HOME = 6;             //跳转home页面
    public static final int TRANSLATE_CAR_ADD = 7;          //跳转car_add页面
    public static final int TRANSLATE_CAR_ADD_MAP = 8;      //跳转car_add_map页面
    public static final int TRANSLATE_BACK = 0;             //跳转返回功能

    // 动画
    public static final int TRANSLATE_STYLE_MAIN = 0;       //Activity主要切换动画
    public static final int TRANSLATE_STYLE_MAIN_BACK = 1;  //Activity主要返回动画
    public static final int TRANSLATE_STYLE_POP = 2;        //Activity内弹窗切换动画
    public static final int TRANSLATE_STYLE_POP_BACK = 3;   //Activity内弹窗返回动画

    // Object类型判断标记
    public static final int TYPE_INTEGER = 0;
    public static final int TYPE_STRING = 1;
    public static final int TYPE_FLOAT = 2;
    public static final int TYPE_LONG = 3;
    public static final int TYPE_BOOLEAN = 4;

    /**
     * Mob 信息
     */
    public static final String MOB_APP_KEY = "10c95b1b1bbc0";
    public static final String MOB_APP_SECRET = "acaceebb665e4113c2963c3241a03abc";
    public static final String MOB_COUNTRY_CHINA = "86";

    /**
     * System details
     */
    public static final String SCREEN_WIDTH = "screen_width";
    public static final String SCREEN_HEIGHT = "screen_height";
}

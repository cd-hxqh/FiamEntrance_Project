package com.hqxh.fiamproperty.constant;

/**
 * 全局配置
 *
 */

public final class GlobalConfig {

    /**服务器地址**/
    public final static String HTTP_URL="http://10.60.12.98:9080";

    /**服务器登录接口地址**/

    public final static String HTTP_URL_LOGIN=HTTP_URL+"/maximo/mobile/system/portallogin";

    /**服务器通用查询接口地址**/

    public final static String HTTP_URL_SEARCH=HTTP_URL+"/maximo/mobile/common/api";



    ///////////////////////////
    /**项目类型**/
    public final static String TRAVEL="TRAVEL"; //国内出差/出差申请
    public final static String GRWZ="GRWZ"; //物资出门/出门管理
    public final static String SZPR="SZPR"; //试制采购申请/采购
    public final static String TOQT="TOQT"; //其它任务单/任务单










    /**定义appid,objectname**/
    //待办appid
    public static final String WFADMIN_APPID = "WFADMIN" ;

    //待办的表名
    public static final String WFASSIGNMENT_NAME = "WFASSIGNMENT";

    //国内出差appid
    public static final String TRAVEL_APPID = "TRAVEL" ;
    //出差表名/任务单
    public static final String WORKORDER_NAME = "WORKORDER" ;
    //国外立项appid
    public static final String TRAVELS_APPID = "TRAVELS" ;

    //出门管理appid
    public static final String GRWZ_APPID = "GRWZ" ;
    //出门管理表名
    public static final String GR_APPID = "GR" ;

    //技术采购申请appid
    public static final String JSPR_APPID = "JSPR" ;
    //试制采购申请appid
    public static final String SZPR_APPID = "SZPR" ;
    //物资采购申请appid
    public static final String PR_APPID = "PR" ;
    //服务采购申请appid
    public static final String FWPR_APPID = "FWPR" ;
    //外培采购申请appid
    public static final String WPPR_APPID = "WPPR" ;
    //采购申请表名
    public static final String PR_NAME = "PR" ;

    /**任务单**/
    //试验任务单
    public static final String TOSY_APPID = "TOSY" ;
    //试制任务单
    public static final String TOSZ_APPID = "TOSZ" ;
    //物资领料单
    public static final String TOLL_APPID = "TOLL" ;
    //调件任务单
    public static final String TODJ_APPID = "TODJ" ;
    //燃油申请单
    public static final String TOOIL_APPID = "TOOIL" ;
    //其它任务单
    public static final String TOQT_APPID = "TOQT" ;


    /**
     * 用户登录表识--开始*
     */
    public static final String LOGINSUCCESS = "USER-S-101"; //登录成功

    public static final String CHANGEIMEI = "USER-S-104"; //登录成功,检测到用户更换手机登录

    public static final String USERNAMEERROR = "USER-E-100";//用户名密码错误

    public static final String GETDATASUCCESS = "GLOBAL-S-0";//获取数据成功
}

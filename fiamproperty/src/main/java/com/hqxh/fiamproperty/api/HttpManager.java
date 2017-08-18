package com.hqxh.fiamproperty.api;

import com.hqxh.fiamproperty.constant.GlobalConfig;

/**
 * Created by apple on 17/7/27.
 * 查询条件
 */

public class HttpManager {
    /**
     * 待办任务*
     */
    public static String getWFASSIGNMENTUrl(String search,String username, String assignstatus,int curpage, int showcount) {
         if (null==search||search.equals("")){
             return "{'appid':'" + GlobalConfig.WFADMIN_APPID + "','objectname':'" + GlobalConfig.WFASSIGNMENT_NAME + "','username':'"+username+"','curpage':" + curpage + ",'showcount':" + showcount + ",'option':'read','condition':{'ASSIGNSTATUS':'="+assignstatus+"','ASSIGNCODE':'="+username+"'}}";

         }else{
             return "{'appid':'" + GlobalConfig.WFADMIN_APPID + "','objectname':'" + GlobalConfig.WFASSIGNMENT_NAME + "','username':'"+username+"','curpage':" + curpage + ",'showcount':" + showcount + ",'option':'read','condition':{'ASSIGNSTATUS':'="+assignstatus+"','ASSIGNCODE':'="+username+"'},'reporsearch':{'UDAPPNAME':'"+search+"','DESCRIPTION':'"+search+"'}}";

         }

    }
    /**
     * 国内出差*
     */
    public static String getWORKORDERUrl(String username,int curpage, int showcount) {
            return "{'appid':'" + GlobalConfig.TRAVEL_APPID + "','objectname':'" + GlobalConfig.WORKORDER_NAME + "','username':'"+username+"','curpage':" + curpage + ",'showcount':" + showcount + ",'option':'read'}";

    }

    /**
     * 出差人*
     */
    public static String getR_PERSONRELATIONUrl(String username,String wonum,int curpage, int showcount) {
        return "{'appid':'" + GlobalConfig.TRAVEL_APPID + "','objectname':'" + GlobalConfig.PERSONRELATION_NAME + "','username':'"+username+"','curpage':" + curpage + ",'showcount':" + showcount + ",'option':'read','condition':{'WONUM':'="+wonum+"'}}";

    }

    /*
    物资明细
    */
   public static String getGRLINEUrl(String username,String grnum,int curpage, int showcount) {
        return "{'appid':'" + GlobalConfig.GRWZ_APPID + "','objectname':'" + GlobalConfig.GRLINE_NAME + "','username':'"+username+"','curpage':" + curpage + ",'showcount':" + showcount + ",'option':'read','condition':{'GRNUM':'="+grnum+"'}}";

    }
/*
    整车明细
*/

    public static String getZCMXUrl(String username,String grnum,int curpage, int showcount) {
//        return "{'appid':'" + GlobalConfig.GRZC_APPID + "','objectname':'" + GlobalConfig.GRLINE_NAME + "','username':'"+username+"','curpage':" + curpage + ",'showcount':" + showcount + ",'option':'read','condition':{'GRNUM':'="+grnum+"'}}";
        return "{'appid':'" + GlobalConfig.GRZC_APPID + "','objectname':'" + GlobalConfig.GRLINE_NAME + "','username':'"+username+"','curpage':" + curpage + ",'showcount':" + showcount + ",'option':'read'}";

    }


    /**
     * 出国立项*
     */
    public static String getGWWORKORDERUrl(String username,int curpage, int showcount) {
        return "{'appid':'" + GlobalConfig.TRAVELS_APPID + "','objectname':'" + GlobalConfig.WORKORDER_NAME + "','username':'"+username+"','curpage':" + curpage + ",'showcount':" + showcount + ",'option':'read'}";

    }

    /**
     * 出门管理*
     */
    public static String getGRUrl(String username,int curpage, int showcount) {
        return "{'appid':'" + GlobalConfig.GRWZ_APPID + "','objectname':'" + GlobalConfig.GR_NAME + "','username':'"+username+"','curpage':" + curpage + ",'showcount':" + showcount + ",'option':'read'}";

    }

    /**
     * 采购申请*
     */
    public static String getPRUrl(String appid,String username,int curpage, int showcount) {
        return "{'appid':'" + appid + "','objectname':'" + GlobalConfig.PR_NAME + "','username':'"+username+"','curpage':" + curpage + ",'showcount':" + showcount + ",'option':'read'}";

    }

    /**
     * 任务单*
     */
    public static String getRWDUrl(String appid,String username,int curpage, int showcount) {
        return "{'appid':'" + appid + "','objectname':'" + GlobalConfig.WORKORDER_NAME + "','username':'"+username+"','curpage':" + curpage + ",'showcount':" + showcount + ",'option':'read'}";

    }

    /**
     * 合同*
     */
    public static String getPURCHVIEWUrl(String username,int curpage, int showcount) {
        return "{'appid':'" + GlobalConfig.CONTPURCH_APPID + "','objectname':'" + GlobalConfig.PURCHVIEW_NAME + "','username':'"+username+"','curpage':" + curpage + ",'showcount':" + showcount + ",'option':'read'}";

    }

    /**
     * 付款验收*
     */
    public static String getPAYCHECKUrl(String username,int curpage, int showcount) {
        return "{'appid':'" + GlobalConfig.PAYCHECK_APPID + "','objectname':'" + GlobalConfig.PAYCHECK_NAME + "','username':'"+username+"','curpage':" + curpage + ",'showcount':" + showcount + ",'option':'read'}";

    }

    /**
     * 需款计划*
     */
    public static String getPAYPLANUrl(String username,int curpage, int showcount) {
        return "{'appid':'" + GlobalConfig.PP_APPID + "','objectname':'" + GlobalConfig.PAYPLAN_NAME + "','username':'"+username+"','curpage':" + curpage + ",'showcount':" + showcount + ",'option':'read'}";

    }

    /**
     * 差旅报销单／备用金报销*
     */
    public static String getEXPENSEUrl(String appid,String username,int curpage, int showcount) {
        return "{'appid':'" + appid + "','objectname':'" + GlobalConfig.EXPENSE_NAME + "','username':'"+username+"','curpage':" + curpage + ",'showcount':" + showcount + ",'option':'read'}";

    }

    /**
     *借款单*
     */
    public static String getBoUrl(String username,int curpage, int showcount) {
        return "{'appid':'" + GlobalConfig.BO_APPID + "','objectname':'" + GlobalConfig.BO_NAME + "','username':'"+username+"','curpage':" + curpage + ",'showcount':" + showcount + ",'option':'read'}";

    }

    /**
     * 审批记录*
     */
    public static String getWFTRANSACTIONUrl(String username, String ownertable, String ownerid, int curpage, int showcount) {
        return "{'appid':'" + GlobalConfig.WFADMIN_APPID + "','objectname':'" + GlobalConfig.WFTRANSACTION_NAME + "','username':'" + username + "','curpage':" + curpage + ",'showcount':" + showcount + ",'option':'read','condition':{'OWNERTABLE':'=" + ownertable + "','OWNERID':'=" + ownerid + "'}}";

    }

    /**
     * 执行部门 /科室*
     * GlobalConfig.USER_APPID 部门
     * GlobalConfig.ROLE_APPID 科室
     */
    public static String getCUDEPTUrl(String appid, String deptnum, String username, int curpage, int showcount) {
        if (appid.equals(GlobalConfig.USER_APPID)) {
            return "{'appid':'" + GlobalConfig.USER_APPID + "','objectname':'" + GlobalConfig.CUDEPT_NAME + "','username':'" + username + "','curpage':" + curpage + ",'showcount':" + showcount + ",'option':'read'}";
        } else if (appid.equals(GlobalConfig.ROLE_APPID)) {
            return "{'appid':'" + GlobalConfig.ROLE_APPID + "','objectname':'" + GlobalConfig.CUDEPT_NAME + "','username':'" + username + "','curpage':" + curpage + ",'showcount':" + showcount + ",'option':'read','condition':{'PARENT':'=" + deptnum + "'}}";

        }
        return null;
    }


    /**
     * 执行人
     * @param cudept 部门
     * @param cucrew 科室
     * @param username 用户名
     * @param curpage 当前页
     * @param showcount 显示条数
     *
     */
    public static String getPERSONUrl(String cudept, String cucrew, String username, int curpage, int showcount) {
        return "{'appid':'" + GlobalConfig.USER_APPID + "','objectname':'" + GlobalConfig.PERSON_NAME + "','username':'" + username + "','curpage':" + curpage + ",'showcount':" + showcount + ",'option':'read','condition':{'CUDEPT':'=" + cudept + "','CUCREW':'="+cucrew+"'}}";
    }



}

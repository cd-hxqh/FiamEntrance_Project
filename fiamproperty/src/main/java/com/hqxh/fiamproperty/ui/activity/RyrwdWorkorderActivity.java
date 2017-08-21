package com.hqxh.fiamproperty.ui.activity;import android.app.AlertDialog;import android.content.DialogInterface;import android.content.Intent;import android.graphics.drawable.Drawable;import android.os.Bundle;import android.util.Log;import android.view.Gravity;import android.view.View;import android.view.ViewGroup;import android.view.animation.Animation;import android.view.animation.AnimationUtils;import android.view.animation.LinearInterpolator;import android.widget.Button;import android.widget.CheckBox;import android.widget.ImageView;import android.widget.LinearLayout;import android.widget.TextView;import com.google.gson.Gson;import com.hqxh.fiamproperty.R;import com.hqxh.fiamproperty.base.BaseTitleActivity;import com.hqxh.fiamproperty.bean.R_APPROVE;import com.hqxh.fiamproperty.bean.R_WORKFLOW;import com.hqxh.fiamproperty.constant.GlobalConfig;import com.hqxh.fiamproperty.dateviews.cons.DPMode;import com.hqxh.fiamproperty.model.R_WPITEM.WPITEM;import com.hqxh.fiamproperty.model.R_Workorder.Workorder;import com.hqxh.fiamproperty.ui.widget.ConfirmDialog;import com.hqxh.fiamproperty.ui.widget.DatePicker;import com.hqxh.fiamproperty.unit.AccountUtils;import com.hqxh.fiamproperty.unit.DataUtils;import com.hqxh.fiamproperty.unit.JsonUnit;import com.rx2androidnetworking.Rx2AndroidNetworking;import java.util.ArrayList;import java.util.List;import io.reactivex.android.schedulers.AndroidSchedulers;import io.reactivex.annotations.NonNull;import io.reactivex.functions.Consumer;import io.reactivex.schedulers.Schedulers;/** * 燃油申请单 **/public class RyrwdWorkorderActivity extends BaseTitleActivity {    private static final String TAG = "RyrwdWorkorderActivity";    /**     * 信息展示     **/    private TextView wonumText; //合同编号    private TextView udtodescText; //任务单描述    private TextView udtooil1Text; //燃油类型及标号    private TextView statusText; //状态    private TextView projectidText; //费用号    private TextView projectdescText; //项目名称    private TextView pmText; //项目经理    private TextView reportedbynameText; //提单人    private TextView reportedbycudeptText; //部门    private TextView reportedbycucrewText; //科室    private TextView reportdateText; //提单日期    private TextView phonenumText; //电话    private TextView udtargstartdateText; //建议完成时间    private TextView udqty1Text; //申请数量    private TextView cuunitText; //单位    private ImageView sqjlImageView; //审批记录    private Button workflowBtn;    private Workorder workorder;    @Override    protected void beforeInit() {        super.beforeInit();        workorder = (Workorder) getIntent().getSerializableExtra("workorder");    }    @Override    protected int getContentViewLayoutID() {        return R.layout.activity_rlrwd_workorder;    }    @Override    protected void initView(Bundle savedInstanceState) {        wonumText = (TextView) findViewById(R.id.sqd_text_id);        udtodescText = (TextView) findViewById(R.id.udtodesc_text_id);        udtooil1Text = (TextView) findViewById(R.id.udtooil1_text_id);        statusText = (TextView) findViewById(R.id.status_text_id);        projectidText = (TextView) findViewById(R.id.projectid_text_id);        projectdescText = (TextView) findViewById(R.id.projectdesc_text_id);        pmText = (TextView) findViewById(R.id.pm_text_id);        reportedbynameText = (TextView) findViewById(R.id.reportedbyname_text_id);        reportedbycudeptText = (TextView) findViewById(R.id.reportedbycudept_text_id);        reportedbycucrewText = (TextView) findViewById(R.id.reportedbycucrew_text_id);        reportdateText = (TextView) findViewById(R.id.reportdate_text_id);        phonenumText = (TextView) findViewById(R.id.phonenum_text_id);        udtargstartdateText = (TextView) findViewById(R.id.udtargstartdate_text_id);        udqty1Text = (TextView) findViewById(R.id.udqty1_text_id);        cuunitText = (TextView) findViewById(R.id.cuunit_text_id);        sqjlImageView = (ImageView) findViewById(R.id.sqjl_imageview_id);        workflowBtn = (Button) findViewById(R.id.workflow_btn_id);        showData();    }    //展示界面数据    private void showData() {        wonumText.setText(JsonUnit.convertStrToArray(workorder.getWONUM())[0] + "," + JsonUnit.convertStrToArray(workorder.getDESCRIPTION())[0]);        udtodescText.setText(JsonUnit.convertStrToArray(workorder.getUDTODESC())[0]);        udtooil1Text.setText(JsonUnit.convertStrToArray(workorder.getUDTOOIL1())[0]);        statusText.setText(JsonUnit.convertStrToArray(workorder.getSTATUSDESC())[0]);        projectidText.setText(JsonUnit.convertStrToArray(workorder.getPROJECTID())[0]);        projectdescText.setText(JsonUnit.convertStrToArray(workorder.getPROJECTDESC())[0]);        pmText.setText(JsonUnit.convertStrToArray(workorder.getPM())[0]);        reportedbynameText.setText(JsonUnit.convertStrToArray(workorder.getREPORTEDBYNAME())[0]);        reportedbycudeptText.setText(JsonUnit.convertStrToArray(workorder.getREPORTEDBYCUDEPT())[0]);        reportedbycucrewText.setText((JsonUnit.convertStrToArray(workorder.getREPORTEDBYCUCREW())[0]));        reportdateText.setText(JsonUnit.convertStrToArray(workorder.getREPORTDATE())[0]);        phonenumText.setText(JsonUnit.convertStrToArray(workorder.getPHONENUM())[0]);        udtargstartdateText.setText(JsonUnit.strToDateString(JsonUnit.convertStrToArray(workorder.getUDTARGSTARTDATE())[0]));        udqty1Text.setText(JsonUnit.convertStrToArray(workorder.getUDQTY1())[0]);        cuunitText.setText(JsonUnit.convertStrToArray(workorder.getCUUNIT())[0]);        sqjlImageView.setOnClickListener(sqjlImageViewOnClickListener);        workflowBtn.setOnClickListener(workflowBtnOnClickListener);    }    @Override    protected String getSubTitle() {        return getString(R.string.rysqdxq_text);    }    //审批记录    private View.OnClickListener sqjlImageViewOnClickListener = new View.OnClickListener() {        @Override        public void onClick(View view) {            Intent intent = new Intent(RyrwdWorkorderActivity.this, WftransactionActivity.class);            intent.putExtra("ownertable", GlobalConfig.WORKORDER_NAME);            intent.putExtra("ownerid", JsonUnit.convertStrToArray(workorder.getWORKORDERID())[0]);            startActivityForResult(intent, 0);        }    };    //审批    private View.OnClickListener workflowBtnOnClickListener = new View.OnClickListener() {        @Override        public void onClick(View view) {            PostStart(GlobalConfig.WORKORDER_NAME, JsonUnit.convertStrToArray(workorder.getWORKORDERID())[0], GlobalConfig.TOOIL_APPID, AccountUtils.getpersonId(RyrwdWorkorderActivity.this));        }    };    //流程启动    private void PostStart(String ownertable, String ownerid, String appid, String userid) {        Log.e(TAG, "ownertable=" + ownertable + ",ownerid=" + ownerid + ",appid=" + appid + ",userid=" + userid);        Rx2AndroidNetworking.post(GlobalConfig.HTTP_URL_START_WORKFLOW)                .addBodyParameter("ownertable", ownertable)                .addBodyParameter("ownerid", ownerid)                .addBodyParameter("appid", appid)                .addBodyParameter("userid", userid)                .build().getStringObservable()                .subscribeOn(Schedulers.io())        // 在io线程进行网络请求                .observeOn(AndroidSchedulers.mainThread()) // 在主线程处理获取数据列表的请求结果                .doOnNext(new Consumer<String>() {                    @Override                    public void accept(@NonNull String string) throws Exception {                    }                })                .subscribeOn(Schedulers.io())                .observeOn(AndroidSchedulers.mainThread())                .subscribe(new Consumer<String>() {                    @Override                    public void accept(@NonNull String s) throws Exception {                        R_WORKFLOW workflow = new Gson().fromJson(s, R_WORKFLOW.class);                        if (workflow.getErrcode().equals(GlobalConfig.WORKFLOW_106)) {                            R_APPROVE r_approve = new Gson().fromJson(s, R_APPROVE.class);                            showDialog(r_approve.getResult());                        } else {                            showMiddleToast(RyrwdWorkorderActivity.this, workflow.getErrmsg());                        }                    }                }, new Consumer<Throwable>() {                    @Override                    public void accept(@NonNull Throwable throwable) throws Exception {                        showMiddleToast(RyrwdWorkorderActivity.this, getString(R.string.spsb_text));                    }                });    }    private void PostApprove(String ownertable, String ownerid, String memo, String selectWhat, String userid) {        Rx2AndroidNetworking                .post(GlobalConfig.HTTP_URL_APPROVE_WORKFLOW)                .addBodyParameter("ownertable", ownertable)                .addBodyParameter("ownerid", ownerid)                .addBodyParameter("memo", memo)                .addBodyParameter("selectWhat", selectWhat)                .addBodyParameter("userid", userid)                .build()                .getStringObservable()                .subscribeOn(Schedulers.io())        // 在io线程进行网络请求                .observeOn(AndroidSchedulers.mainThread()) // 在主线程处理获取数据列表的请求结果                .doOnNext(new Consumer<String>() {                    @Override                    public void accept(@NonNull String string) throws Exception {                    }                })                .subscribeOn(Schedulers.io())                .observeOn(AndroidSchedulers.mainThread())                .subscribe(new Consumer<String>() {                    @Override                    public void accept(@NonNull String s) throws Exception {                        Log.e(TAG, "审批=" + s);                        R_WORKFLOW workflow = new Gson().fromJson(s, R_WORKFLOW.class);                        showMiddleToast(RyrwdWorkorderActivity.this, workflow.getErrmsg());                    }                }, new Consumer<Throwable>() {                    @Override                    public void accept(@NonNull Throwable throwable) throws Exception {                        showMiddleToast(RyrwdWorkorderActivity.this, getString(R.string.spsb_text));                    }                });    }    //弹出对话框    public void showDialog(List<R_APPROVE.Result> results) {//        ConfirmDialog.Builder dialog = new ConfirmDialog.Builder(this);        dialog.setTitle("审批")                .setData(results)                .setPositiveButton("确定", new ConfirmDialog.Builder.cOnClickListener() {                    @Override                    public void cOnClickListener(DialogInterface dialogInterface, R_APPROVE.Result result, String memo) {                        dialogInterface.dismiss();                        PostApprove(GlobalConfig.WORKORDER_NAME, JsonUnit.convertStrToArray(workorder.getWORKORDERID())[0], memo, result.getIspositive(), AccountUtils.getpersonId(RyrwdWorkorderActivity.this));                    }                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {            @Override            public void onClick(DialogInterface dialogInterface, int i) {                dialogInterface.dismiss();            }        }).create().show();    }}
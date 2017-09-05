package com.hqxh.fiamproperty.ui.activity;import android.app.AlertDialog;import android.content.DialogInterface;import android.content.Intent;import android.graphics.drawable.Drawable;import android.os.Bundle;import android.util.Log;import android.view.Gravity;import android.view.View;import android.view.ViewGroup;import android.view.animation.Animation;import android.view.animation.AnimationUtils;import android.view.animation.LinearInterpolator;import android.widget.Button;import android.widget.ImageView;import android.widget.LinearLayout;import android.widget.RelativeLayout;import android.widget.ScrollView;import android.widget.TextView;import com.google.gson.Gson;import com.hqxh.fiamproperty.R;import com.hqxh.fiamproperty.api.HttpManager;import com.hqxh.fiamproperty.base.BaseTitleActivity;import com.hqxh.fiamproperty.bean.R_APPROVE;import com.hqxh.fiamproperty.bean.R_WORKFLOW;import com.hqxh.fiamproperty.constant.GlobalConfig;import com.hqxh.fiamproperty.dateviews.cons.DPMode;import com.hqxh.fiamproperty.model.R_PR;import com.hqxh.fiamproperty.model.R_PR.PR;import com.hqxh.fiamproperty.model.R_ZXPERSON.PERSON;import com.hqxh.fiamproperty.ui.widget.ConfirmDialog;import com.hqxh.fiamproperty.ui.widget.DatePicker;import com.hqxh.fiamproperty.unit.AccountUtils;import com.hqxh.fiamproperty.unit.DataUtils;import com.hqxh.fiamproperty.unit.JsonUnit;import com.rx2androidnetworking.Rx2AndroidNetworking;import java.util.List;import io.reactivex.android.schedulers.AndroidSchedulers;import io.reactivex.annotations.NonNull;import io.reactivex.functions.Consumer;import io.reactivex.functions.Function;import io.reactivex.schedulers.Schedulers;/** * 物资采购申请 **/public class WzPrActivity extends BaseTitleActivity {    private static final String TAG = "WzPrActivity";    private ScrollView scrollView;    /**     * 信息展示     **/    private TextView prnumText; //申请单号    private TextView descriptionText; //名称    private TextView cutypeText; //类型    private TextView statusText; //状态    private TextView totalcostText; //总预算(人民币)    private TextView wonumText; //任务单    private TextView projectidText; //费用号    private TextView projectdescText; //项目名称    private TextView udremark1Text; //申请原因    private ImageView qtxxImageView;  //其它信息    private LinearLayout qtLinearLayout;//其它布局    private View qtView;//其它布局    private TextView uddate1Text; //建议完成时间    private TextView uddate3Text; //结算完成时间    private TextView uddate2Text; //要求完成时间    private TextView pmText; //项目经理    private TextView rdcheadText; //中心分管领导    private TextView newvendorText; //制定供应商    private TextView contactText; //联系人    private TextView reqbypersonText; //申请人    private TextView cudeptText; //部门    private TextView cucrewText; //科室    private TextView issuedateText; //申请日期    private TextView reqbyphoneText; //申请人电话    private ImageView sqmxImageView; //申请明细    private ImageView yxmxImageView; //预算明细    private ImageView sqjlImageView; //审批记录    private Button workflowBtn;    private RelativeLayout workflowRelativeLayout;//布局    private PR pr;    private Animation rotate;    private String personnum;//执行人编码    private String rdchead;//中心分管领导编码    private int mark = 0;//跳转标识    private String appid; //appid    private String ownernum;//ownernum    private String ownertable;//ownertable    @Override    protected void beforeInit() {        super.beforeInit();        if (getIntent().hasExtra("pr")) {            pr = (PR) getIntent().getSerializableExtra("pr");        }        if (getIntent().hasExtra("mark")) {            mark = getIntent().getExtras().getInt("mark");        }        if (getIntent().hasExtra("appid")) {            appid = getIntent().getExtras().getString("appid");        }        if (getIntent().hasExtra("ownernum")) {            ownernum = getIntent().getExtras().getString("ownernum");        }        if (getIntent().hasExtra("ownertable")) {            ownertable = getIntent().getExtras().getString("ownertable");        }    }    @Override    protected int getContentViewLayoutID() {        return R.layout.activity_wzcg_pr;    }    @Override    protected void initView(Bundle savedInstanceState) {        scrollView = (ScrollView) findViewById(R.id.scrollView_id);        prnumText = (TextView) findViewById(R.id.prnum_text_id);        descriptionText = (TextView) findViewById(R.id.description_text_id);        cutypeText = (TextView) findViewById(R.id.type_text_id);        statusText = (TextView) findViewById(R.id.status_text_id);        totalcostText = (TextView) findViewById(R.id.totalcost_text_id);        wonumText = (TextView) findViewById(R.id.wonum_text_id);        projectidText = (TextView) findViewById(R.id.projectid_text_id);        projectdescText = (TextView) findViewById(R.id.projectdesc_text_id);        udremark1Text = (TextView) findViewById(R.id.udremark1_text_id);        qtxxImageView = (ImageView) findViewById(R.id.jbxx_kz_imageview_id);        qtLinearLayout = (LinearLayout) findViewById(R.id.linearLayout_id);        qtView = (View) findViewById(R.id.jbxx_view_id);        uddate1Text = (TextView) findViewById(R.id.uddate1_text_id);        uddate3Text = (TextView) findViewById(R.id.uddate3_text_id);        uddate2Text = (TextView) findViewById(R.id.udtargcompdate_text_id);        pmText = (TextView) findViewById(R.id.pm_text_id);        rdcheadText = (TextView) findViewById(R.id.rdchead_text_id);        newvendorText = (TextView) findViewById(R.id.newvendor_text_id);        contactText = (TextView) findViewById(R.id.contact_text_id);        reqbypersonText = (TextView) findViewById(R.id.reqbyperson_text);        cudeptText = (TextView) findViewById(R.id.cudept_text_id);        cucrewText = (TextView) findViewById(R.id.cucrew_text_id);        issuedateText = (TextView) findViewById(R.id.reportdate_text_id);        reqbyphoneText = (TextView) findViewById(R.id.reqbyphone_text_id);        sqmxImageView = (ImageView) findViewById(R.id.sqmx_imageview_id);        yxmxImageView = (ImageView) findViewById(R.id.yxmx_imageview_id);        sqjlImageView = (ImageView) findViewById(R.id.sqjl_imageview_id);        workflowBtn = (Button) findViewById(R.id.workflow_btn_id);        workflowRelativeLayout = (RelativeLayout) findViewById(R.id.relavtivelayout_btn_id);        if (null == appid) {            showData();        } else {            if (mark == HomeActivity.DB_CODE) { //待办任务                workflowRelativeLayout.setVisibility(View.VISIBLE);                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);                layoutParams.setMargins(0, 0, 0, getHeight(workflowRelativeLayout));//4个参数按顺序分别是左上右下                scrollView.setLayoutParams(layoutParams);            }            showLoadingDialog(getResources().getString(R.string.loading_hint));            getNetWorkPR();        }    }    //展示界面数据    private void showData() {        prnumText.setText(JsonUnit.convertStrToArray(pr.getPRNUM())[0]);        descriptionText.setText(JsonUnit.convertStrToArray(pr.getDESCRIPTION())[0]);        cutypeText.setText(JsonUnit.convertStrToArray(pr.getCUTYPE())[0]);        statusText.setText(JsonUnit.convertStrToArray(pr.getSTATUSDESC())[0]);        totalcostText.setText(JsonUnit.convertStrToArray(pr.getTOTALCOST())[0]);        wonumText.setText(JsonUnit.convertStrToArray(pr.getWONUM())[0]);        projectidText.setText(JsonUnit.convertStrToArray(pr.getPROJECTID())[0]);        projectdescText.setText(JsonUnit.convertStrToArray(pr.getPROJECTDESC())[0]);        udremark1Text.setText(JsonUnit.convertStrToArray(pr.getUDREMARK1())[0]);        uddate1Text.setText(JsonUnit.strToDateString(JsonUnit.convertStrToArray(pr.getUDDATE1())[0]));        uddate3Text.setText(JsonUnit.strToDateString(JsonUnit.convertStrToArray(pr.getUDDATE3())[0]));        uddate2Text.setText(JsonUnit.strToDateString(JsonUnit.convertStrToArray(pr.getUDDATE2())[0]));        pmText.setText(JsonUnit.convertStrToArray(pr.getPM())[0]);        rdcheadText.setText(JsonUnit.convertStrToArray(pr.getRDCHEADPERSON())[0]);        newvendorText.setText(JsonUnit.convertStrToArray(pr.getNEWVENDOR())[0]);        contactText.setText(JsonUnit.convertStrToArray(pr.getCONTACT())[0]);        reqbypersonText.setText(JsonUnit.convertStrToArray(pr.getREQBYPERSON())[0]);        cudeptText.setText(JsonUnit.convertStrToArray(pr.getCUDEPT())[0]);        cucrewText.setText(JsonUnit.convertStrToArray(pr.getCUCREW())[0]);        issuedateText.setText(JsonUnit.convertStrToArray(pr.getISSUEDATE())[0]);        reqbyphoneText.setText(JsonUnit.convertStrToArray(pr.getREQBYPHONE())[0]);        rotate = AnimationUtils.loadAnimation(this, R.anim.arrow_rotate);//创建动画        qtxxImageView.setOnClickListener(jbxxImageViewOnClickListener);        sqmxImageView.setOnClickListener(qzgysImageViewOnClickListener);        yxmxImageView.setOnClickListener(yxmxImageViewOnClickListener);        sqjlImageView.setOnClickListener(sqjlImageViewOnClickListener);        workflowBtn.setOnClickListener(workflowBtnOnClickListener);        onClickListener();    }    //设置事件监听    private void onClickListener() {        if (JsonUnit.convertStrToArray(pr.getUDDATE2())[1].equals(GlobalConfig.NOTREADONLY) && appid != null) {            Drawable nav_up = getResources().getDrawable(R.drawable.ic_data);            nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());            uddate2Text.setCompoundDrawablesWithIntrinsicBounds(null, null, nav_up, null);            uddate2Text.setOnClickListener(udtargcompdateTextOnClickListener);        }        if (JsonUnit.convertStrToArray(pr.getRDCHEAD())[1].equals(GlobalConfig.NOTREADONLY) && appid != null) {            Drawable nav_up = getResources().getDrawable(R.drawable.ic_person_black);            nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());            rdcheadText.setCompoundDrawablesWithIntrinsicBounds(null, null, nav_up, null);            rdcheadText.setOnClickListener(rdcheadTextOnClickListener);        }    }    //日期选择    private View.OnClickListener udtargcompdateTextOnClickListener = new View.OnClickListener() {        @Override        public void onClick(View view) {            final AlertDialog dialog = new AlertDialog.Builder(WzPrActivity.this).create();            dialog.show();            DatePicker picker = new DatePicker(WzPrActivity.this);            picker.setDate(DataUtils.getYear(), DataUtils.getMonths());            picker.setMode(DPMode.SINGLE);            picker.setFestivalDisplay(false);            picker.setOnDatePickedListener(new DatePicker.OnDatePickedListener() {                @Override                public void onDatePicked(String date) {                    uddate2Text.setText(date);                    dialog.dismiss();                }            });            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);            dialog.getWindow().setContentView(picker, params);            dialog.getWindow().setGravity(Gravity.CENTER);        }    };    //中心分管领导    private View.OnClickListener rdcheadTextOnClickListener = new View.OnClickListener() {        @Override        public void onClick(View view) {            Intent intent = new Intent(WzPrActivity.this, PersonActivity.class);            intent.putExtra("appid", GlobalConfig.ROLE_APPID);            intent.putExtra("title", getResources().getString(R.string.rdchead_text));            startActivityForResult(intent, GlobalConfig.RDCHEAD_REQUESTCODE);        }    };    @Override    protected String getSubTitle() {        return getString(R.string.wzcgsqxq_text);    }    //基本信息    private View.OnClickListener jbxxImageViewOnClickListener = new View.OnClickListener() {        @Override        public void onClick(View view) {            if (startAnaim()) {                qtLinearLayout.setVisibility(View.GONE);                qtView.setVisibility(View.GONE);            } else {                qtLinearLayout.setVisibility(View.VISIBLE);                qtView.setVisibility(View.VISIBLE);            }        }    };    //启动动画    private boolean startAnaim() {        rotate.setInterpolator(new LinearInterpolator());//设置为线性旋转        rotate.setFillAfter(!rotate.getFillAfter());//每次都取相反值，使得可以不恢复原位的旋转        qtxxImageView.startAnimation(rotate);        return rotate.getFillAfter();    }    //申请明细    private View.OnClickListener qzgysImageViewOnClickListener = new View.OnClickListener() {        @Override        public void onClick(View view) {            Intent intent = new Intent(WzPrActivity.this, PrlineActivity.class);            intent.putExtra("appid", GlobalConfig.PR_APPID);            intent.putExtra("prnum", JsonUnit.convertStrToArray(pr.getPRNUM())[0]);            startActivityForResult(intent, 0);        }    };    //预算明细    private View.OnClickListener yxmxImageViewOnClickListener = new View.OnClickListener() {        @Override        public void onClick(View view) {            Intent intent = new Intent(WzPrActivity.this, FctaskrelationActivity.class);            intent.putExtra("searchName", "PRNUM");            intent.putExtra("searchValue", JsonUnit.convertStrToArray(pr.getPRNUM())[0]);            intent.putExtra("title", getResources().getString(R.string.yxmx_text));            startActivityForResult(intent, 0);        }    };    //审批记录    private View.OnClickListener sqjlImageViewOnClickListener = new View.OnClickListener() {        @Override        public void onClick(View view) {            Intent intent = new Intent(WzPrActivity.this, WftransactionActivity.class);            intent.putExtra("ownertable", GlobalConfig.PR_NAME);            intent.putExtra("ownerid", JsonUnit.convertStrToArray(pr.getPRID())[0]);            startActivityForResult(intent, 0);        }    };    //审批    private View.OnClickListener workflowBtnOnClickListener = new View.OnClickListener() {        @Override        public void onClick(View view) {            update(JsonUnit.WzPrData(JsonUnit.convertStrToArray(pr.getPRID())[0], rdchead, uddate2Text.getText().toString(), null, AccountUtils.getpersonId(WzPrActivity.this), GlobalConfig.JSPR_APPID));        }    };    //流程启动2017CG00193    private void PostStart(String ownertable, String ownerid, String appid, String userid) {        Rx2AndroidNetworking.post(GlobalConfig.HTTP_URL_START_WORKFLOW)                .addBodyParameter("ownertable", ownertable)                .addBodyParameter("ownerid", ownerid)                .addBodyParameter("appid", appid)                .addBodyParameter("userid", userid)                .build().getStringObservable()                .subscribeOn(Schedulers.io())        // 在io线程进行网络请求                .observeOn(AndroidSchedulers.mainThread()) // 在主线程处理获取数据列表的请求结果                .doOnNext(new Consumer<String>() {                    @Override                    public void accept(@NonNull String string) throws Exception {                    }                })                .subscribeOn(Schedulers.io())                .observeOn(AndroidSchedulers.mainThread())                .subscribe(new Consumer<String>() {                    @Override                    public void accept(@NonNull String s) throws Exception {                        R_WORKFLOW workflow = new Gson().fromJson(s, R_WORKFLOW.class);                        if (workflow.getErrcode().equals(GlobalConfig.WORKFLOW_106)) {                            R_APPROVE r_approve = new Gson().fromJson(s, R_APPROVE.class);                            showDialog(r_approve.getResult());                        } else {                            showMiddleToast(WzPrActivity.this, workflow.getErrmsg());                        }                    }                }, new Consumer<Throwable>() {                    @Override                    public void accept(@NonNull Throwable throwable) throws Exception {                        showMiddleToast(WzPrActivity.this, getString(R.string.spsb_text));                    }                });    }    private void PostApprove(String ownertable, String ownerid, String memo, String selectWhat, String userid) {        Rx2AndroidNetworking                .post(GlobalConfig.HTTP_URL_APPROVE_WORKFLOW)                .addBodyParameter("ownertable", ownertable)                .addBodyParameter("ownerid", ownerid)                .addBodyParameter("memo", memo)                .addBodyParameter("selectWhat", selectWhat)                .addBodyParameter("userid", userid)                .build()                .getStringObservable()                .subscribeOn(Schedulers.io())        // 在io线程进行网络请求                .observeOn(AndroidSchedulers.mainThread()) // 在主线程处理获取数据列表的请求结果                .doOnNext(new Consumer<String>() {                    @Override                    public void accept(@NonNull String string) throws Exception {                    }                })                .subscribeOn(Schedulers.io())                .observeOn(AndroidSchedulers.mainThread())                .subscribe(new Consumer<String>() {                    @Override                    public void accept(@NonNull String s) throws Exception {                        R_WORKFLOW workflow = new Gson().fromJson(s, R_WORKFLOW.class);                        showMiddleToast(WzPrActivity.this, workflow.getErrmsg());                        if (workflow.getErrcode().equals(GlobalConfig.WORKFLOW_103)) {                            setResult(ActiveTaskActivity.TASK_RESULTCODE);                            finish();                        }                    }                }, new Consumer<Throwable>() {                    @Override                    public void accept(@NonNull Throwable throwable) throws Exception {                        showMiddleToast(WzPrActivity.this, getString(R.string.spsb_text));                    }                });    }    //弹出对话框    public void showDialog(List<R_APPROVE.Result> results) {//        ConfirmDialog.Builder dialog = new ConfirmDialog.Builder(this);        dialog.setTitle("审批")                .setData(results)                .setPositiveButton("确定", new ConfirmDialog.Builder.cOnClickListener() {                    @Override                    public void cOnClickListener(DialogInterface dialogInterface, R_APPROVE.Result result, String memo) {                        dialogInterface.dismiss();                        PostApprove(ownertable, JsonUnit.convertStrToArray(pr.getPRID())[0], memo, result.getIspositive(), AccountUtils.getpersonId(WzPrActivity.this));                    }                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {            @Override            public void onClick(DialogInterface dialogInterface, int i) {                dialogInterface.dismiss();            }        }).create().show();    }    @Override    protected void onActivityResult(int requestCode, int resultCode, Intent data) {        super.onActivityResult(requestCode, resultCode, data);        switch (requestCode) {            case GlobalConfig.RDCHEAD_REQUESTCODE: //中心分管领导                if (resultCode == GlobalConfig.CUDEPT_REQUESTCODE) {                    PERSON persion = (PERSON) data.getSerializableExtra("person");                    rdchead = JsonUnit.convertStrToArray(persion.getPERSONID())[0];                    rdcheadText.setText(JsonUnit.convertStrToArray(persion.getDISPLAYNAME())[0]);                }                break;        }    }    //更新操作    private void update(String data) {        Log.e(TAG, "data=" + data);        Rx2AndroidNetworking                .post(GlobalConfig.HTTP_URL_SEARCH)                .addBodyParameter("data", data) //数据                .build()                .getStringObservable()                .subscribeOn(Schedulers.io())        // 在io线程进行网络请求                .observeOn(AndroidSchedulers.mainThread()) // 在主线程处理获取数据列表的请求结果                .doOnNext(new Consumer<String>() {                    @Override                    public void accept(@NonNull String string) throws Exception {                    }                })                .subscribeOn(Schedulers.io())                .observeOn(AndroidSchedulers.mainThread())                .subscribe(new Consumer<String>() {                    @Override                    public void accept(@NonNull String s) throws Exception {                        Log.e(TAG, "s=" + s);                        R_WORKFLOW r = new Gson().fromJson(s, R_WORKFLOW.class);                        if (r.getErrcode().equals(GlobalConfig.GETDATASUCCESS)) {                            PostStart(GlobalConfig.PR_NAME, JsonUnit.convertStrToArray(pr.getPRID())[0], GlobalConfig.JSPR_APPID, AccountUtils.getpersonId(WzPrActivity.this));                        } else {                            showMiddleToast(WzPrActivity.this, r.getErrmsg());                        }                    }                }, new Consumer<Throwable>() {                    @Override                    public void accept(@NonNull Throwable throwable) throws Exception {                        showMiddleToast(WzPrActivity.this, getString(R.string.spsb_text));                    }                });    }    //根据appid,grnum,objctname获取国内出差信息    private void getNetWorkPR() {        String data = HttpManager.getPRUrl(appid, ownernum, AccountUtils.getpersonId(this), 1, 10);        Rx2AndroidNetworking.post(GlobalConfig.HTTP_URL_SEARCH)                .addBodyParameter("data", data)                .build()                .getObjectObservable(R_PR.class) // 发起获取数据列表的请求，并解析到FootList                .subscribeOn(Schedulers.io())        // 在io线程进行网络请求                .observeOn(AndroidSchedulers.mainThread()) // 在主线程处理获取数据列表的请求结果                .doOnNext(new Consumer<R_PR>() {                    @Override                    public void accept(@NonNull R_PR r_pr) throws Exception {                    }                })                .map(new Function<R_PR, R_PR.ResultBean>() {                    @Override                    public R_PR.ResultBean apply(@NonNull R_PR r_pr) throws Exception {                        return r_pr.getResult();                    }                })                .map(new Function<R_PR.ResultBean, List<R_PR.PR>>() {                    @Override                    public List<R_PR.PR> apply(@NonNull R_PR.ResultBean resultBean) throws Exception {                        return resultBean.getResultlist();                    }                })                .subscribeOn(Schedulers.io())                .observeOn(AndroidSchedulers.mainThread())                .subscribe(new Consumer<List<PR>>() {                    @Override                    public void accept(@NonNull List<PR> prs) throws Exception {                        dismissLoadingDialog();                        if (prs == null || prs.isEmpty()) {                        } else {                            pr = prs.get(0);                            showData();                        }                    }                }, new Consumer<Throwable>() {                    @Override                    public void accept(@NonNull Throwable throwable) throws Exception {                        dismissLoadingDialog();                    }                });    }}
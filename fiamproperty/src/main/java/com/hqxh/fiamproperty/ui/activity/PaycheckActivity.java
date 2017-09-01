package com.hqxh.fiamproperty.ui.activity;import android.app.AlertDialog;import android.content.DialogInterface;import android.content.Intent;import android.graphics.drawable.Drawable;import android.os.Bundle;import android.text.Spannable;import android.text.SpannableStringBuilder;import android.text.style.ForegroundColorSpan;import android.util.Log;import android.view.Gravity;import android.view.View;import android.view.ViewGroup;import android.view.animation.Animation;import android.view.animation.AnimationUtils;import android.view.animation.LinearInterpolator;import android.widget.Button;import android.widget.CheckBox;import android.widget.ImageView;import android.widget.LinearLayout;import android.widget.RelativeLayout;import android.widget.ScrollView;import android.widget.TextView;import com.google.gson.Gson;import com.hqxh.fiamproperty.R;import com.hqxh.fiamproperty.api.HttpManager;import com.hqxh.fiamproperty.base.BaseTitleActivity;import com.hqxh.fiamproperty.bean.R_APPROVE;import com.hqxh.fiamproperty.bean.R_WORKFLOW;import com.hqxh.fiamproperty.constant.GlobalConfig;import com.hqxh.fiamproperty.dateviews.cons.DPMode;import com.hqxh.fiamproperty.model.R_PAYCHECK;import com.hqxh.fiamproperty.model.R_PAYCHECK.PAYCHECK;import com.hqxh.fiamproperty.model.R_ZXPERSON.PERSON;import com.hqxh.fiamproperty.ui.widget.ConfirmDialog;import com.hqxh.fiamproperty.ui.widget.DatePicker;import com.hqxh.fiamproperty.unit.AccountUtils;import com.hqxh.fiamproperty.unit.DataUtils;import com.hqxh.fiamproperty.unit.JsonUnit;import com.rx2androidnetworking.Rx2AndroidNetworking;import java.util.List;import io.reactivex.android.schedulers.AndroidSchedulers;import io.reactivex.annotations.NonNull;import io.reactivex.functions.Consumer;import io.reactivex.functions.Function;import io.reactivex.schedulers.Schedulers;/** * 付款验收 **/public class PaycheckActivity extends BaseTitleActivity {    private static final String TAG = "PaycheckActivity";    private ScrollView scrollView;    /**     * 信息展示     **/    private TextView paychecknumText; //付款验收单    private TextView checktypeText; //类型    private TextView totalcostText; //验收金额    private TextView statusText; //状态    private TextView contractnumText; //合同    private TextView prnumText; //采购申请    private TextView wonumText; //任务单    private TextView vendornameText; //供应商    private TextView projectidText; //费用号    private TextView pmText; //项目经理    private ImageView qtxxImageView;  //其它信息    private LinearLayout qtLinearLayout;//其它布局    private CheckBox isfinalCheck; //最后一次验收    private TextView phaseText; //验收阶段    private TextView checkeText; //验收人    private TextView checkdateText; //验收时间    private TextView enterbyText; //提单人    private TextView enterdateText; //提单日期    private TextView cudeptText; //提单部门    private TextView cucrewText; //提单科室    private TextView pc1Text; //交付物和验收内容    private TextView pc2Text; //验收结论/意见    private ImageView ysmxImageView; //验收明细    private ImageView doclinksImageView; //附件    private ImageView sqjlImageView; //审批记录    private Button workflowBtn;    private RelativeLayout workflowRelativeLayout;//布局    private PAYCHECK paycheck;    private Animation rotate;    private String personnum;//执行人编码    private String rdchead;//中心分管领导编码    private int mark = 0;//跳转标识    private String appid; //appid    private String ownernum;//ownernum    private String ownertable;//ownertable    @Override    protected void beforeInit() {        super.beforeInit();        if (getIntent().hasExtra("paycheck")) {            paycheck = (PAYCHECK) getIntent().getSerializableExtra("paycheck");        }        if (getIntent().hasExtra("mark")) {            mark = getIntent().getExtras().getInt("mark");        }        if (getIntent().hasExtra("appid")) {            appid = getIntent().getExtras().getString("appid");        }        if (getIntent().hasExtra("ownernum")) {            ownernum = getIntent().getExtras().getString("ownernum");        }        if (getIntent().hasExtra("ownertable")) {            ownertable = getIntent().getExtras().getString("ownertable");        }    }    @Override    protected int getContentViewLayoutID() {        return R.layout.activity_paycheck;    }    @Override    protected void initView(Bundle savedInstanceState) {        scrollView = (ScrollView) findViewById(R.id.scrollView_id);        paychecknumText = (TextView) findViewById(R.id.paychecknum_text_id);        checktypeText = (TextView) findViewById(R.id.type_text_id);        totalcostText = (TextView) findViewById(R.id.ys_totalcost_text_id);        statusText = (TextView) findViewById(R.id.status_text_id);        contractnumText = (TextView) findViewById(R.id.contractnum_text_id);        prnumText = (TextView) findViewById(R.id.prnum_text_id);        wonumText = (TextView) findViewById(R.id.wonum_text_id);        vendornameText = (TextView) findViewById(R.id.vendorname_text_id);        projectidText = (TextView) findViewById(R.id.projectid_text_id);        pmText = (TextView) findViewById(R.id.pm_text_id);        qtxxImageView = (ImageView) findViewById(R.id.jbxx_kz_imageview_id);        qtLinearLayout = (LinearLayout) findViewById(R.id.linearLayout_id);        isfinalCheck = (CheckBox) findViewById(R.id.isfinalcheck_text_id);        phaseText = (TextView) findViewById(R.id.phase_text_id);        checkeText = (TextView) findViewById(R.id.checke_text_id);        checkdateText = (TextView) findViewById(R.id.checkdate_text_id);        enterbyText = (TextView) findViewById(R.id.reportedbyname_text_id);        enterdateText = (TextView) findViewById(R.id.reportdate1_text_id);        cudeptText = (TextView) findViewById(R.id.cudept_text_id);        cucrewText = (TextView) findViewById(R.id.cucrew_text_id);        pc1Text = (TextView) findViewById(R.id.pc1_text_id);        pc2Text = (TextView) findViewById(R.id.pc2_text_id);        ysmxImageView = (ImageView) findViewById(R.id.ysmx_image_id);        doclinksImageView = (ImageView) findViewById(R.id.doclinks_imageview_id);        sqjlImageView = (ImageView) findViewById(R.id.sqjl_imageview_id);        workflowBtn = (Button) findViewById(R.id.workflow_btn_id);        workflowRelativeLayout = (RelativeLayout) findViewById(R.id.relavtivelayout_btn_id);        if (null == appid) {            showData();        } else {            if (mark == HomeActivity.DB_CODE) { //待办任务                workflowRelativeLayout.setVisibility(View.VISIBLE);                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);                layoutParams.setMargins(0, 0, 0, getHeight(workflowRelativeLayout));//4个参数按顺序分别是左上右下                scrollView.setLayoutParams(layoutParams);            }            showLoadingDialog(getString(R.string.loading_hint));            getNetWorkPAYCHECK();        }    }    //展示界面数据    private void showData() {        if (JsonUnit.convertStrToArray(paycheck.getDESCRIPTION())[0].isEmpty()) {            paychecknumText.setText(JsonUnit.convertStrToArray(paycheck.getPAYCHECKNUM())[0]);        } else {            SpannableStringBuilder builder = new SpannableStringBuilder(JsonUnit.convertStrToArray(paycheck.getPAYCHECKNUM())[0] + "," + JsonUnit.convertStrToArray(paycheck.getDESCRIPTION())[0]);            ForegroundColorSpan redSpan = new ForegroundColorSpan(getResources().getColor(R.color.black));            builder.setSpan(redSpan, 0, JsonUnit.convertStrToArray(paycheck.getPAYCHECKNUM())[0].length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);            paychecknumText.setText(builder);        }        checktypeText.setText(JsonUnit.convertStrToArray(paycheck.getCHECKTYPE())[0]);        totalcostText.setText(JsonUnit.convertStrToArray(paycheck.getTOTALCOST())[0]);        statusText.setText(JsonUnit.convertStrToArray(paycheck.getSTATUSDESC())[0]);        if (JsonUnit.convertStrToArray(paycheck.getCONTRACTDESC())[0].isEmpty()) {            contractnumText.setText(JsonUnit.convertStrToArray(paycheck.getCONTRACTNUM())[0]);        } else {            SpannableStringBuilder builder = new SpannableStringBuilder(JsonUnit.convertStrToArray(paycheck.getCONTRACTNUM())[0] + "," + JsonUnit.convertStrToArray(paycheck.getCONTRACTDESC())[0]);            ForegroundColorSpan redSpan = new ForegroundColorSpan(getResources().getColor(R.color.black));            builder.setSpan(redSpan, 0, JsonUnit.convertStrToArray(paycheck.getCONTRACTNUM())[0].length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);            contractnumText.setText(builder);        }        if (JsonUnit.convertStrToArray(paycheck.getPRDESC())[0].isEmpty()) {            prnumText.setText(JsonUnit.convertStrToArray(paycheck.getPRNUM())[0]);        } else {            SpannableStringBuilder builder = new SpannableStringBuilder(JsonUnit.convertStrToArray(paycheck.getPRNUM())[0] + "," + JsonUnit.convertStrToArray(paycheck.getPRDESC())[0]);            ForegroundColorSpan redSpan = new ForegroundColorSpan(getResources().getColor(R.color.black));            builder.setSpan(redSpan, 0, JsonUnit.convertStrToArray(paycheck.getPRNUM())[0].length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);            prnumText.setText(builder);        }        if (JsonUnit.convertStrToArray(paycheck.getWORKORDERDESC())[0].isEmpty()) {            wonumText.setText(JsonUnit.convertStrToArray(paycheck.getWONUM())[0]);        } else {            SpannableStringBuilder builder = new SpannableStringBuilder(JsonUnit.convertStrToArray(paycheck.getWONUM())[0] + "," + JsonUnit.convertStrToArray(paycheck.getWORKORDERDESC())[0]);            ForegroundColorSpan redSpan = new ForegroundColorSpan(getResources().getColor(R.color.black));            builder.setSpan(redSpan, 0, JsonUnit.convertStrToArray(paycheck.getWONUM())[0].length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);            wonumText.setText(builder);        }        vendornameText.setText(JsonUnit.convertStrToArray(paycheck.getVENDORNAME())[0]);        if (JsonUnit.convertStrToArray(paycheck.getFINCNTRLDESC())[0].isEmpty()) {            projectidText.setText(JsonUnit.convertStrToArray(paycheck.getPROJECTID())[0]);        } else {            SpannableStringBuilder builder = new SpannableStringBuilder(JsonUnit.convertStrToArray(paycheck.getPROJECTID())[0] + "," + JsonUnit.convertStrToArray(paycheck.getFINCNTRLDESC())[0]);            ForegroundColorSpan redSpan = new ForegroundColorSpan(getResources().getColor(R.color.black));            builder.setSpan(redSpan, 0, JsonUnit.convertStrToArray(paycheck.getPROJECTID())[0].length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);            projectidText.setText(builder);        }        Log.e(TAG,"WFPERSON1="+JsonUnit.convertStrToArray(paycheck.getWFPERSON1())[0]);        pmText.setText(JsonUnit.convertStrToArray(paycheck.getWFPERSON1())[0]);        checkeText.setText(JsonUnit.convertStrToArray(paycheck.getCHECKE())[0]);        checkdateText.setText(JsonUnit.convertStrToArray(paycheck.getCHECKDATE())[0]);        if (JsonUnit.convertStrToArray(paycheck.getISFINALCHECK())[0].equals("0")) {            isfinalCheck.setChecked(false);        } else {            isfinalCheck.setChecked(true);        }        phaseText.setText(JsonUnit.convertStrToArray(paycheck.getPHASE())[0]);        enterbyText.setText(JsonUnit.convertStrToArray(paycheck.getENTERBY())[0]);        enterdateText.setText(JsonUnit.convertStrToArray(paycheck.getENTERDATE())[0]);        cudeptText.setText(JsonUnit.convertStrToArray(paycheck.getCUDEPT())[0]);        cucrewText.setText(JsonUnit.convertStrToArray(paycheck.getCUCREW())[0]);        pc1Text.setText(JsonUnit.convertStrToArray(paycheck.getPC1())[0]);        pc2Text.setText(JsonUnit.convertStrToArray(paycheck.getPC2())[0]);        rotate = AnimationUtils.loadAnimation(this, R.anim.arrow_rotate);//创建动画        qtxxImageView.setOnClickListener(jbxxImageViewOnClickListener);        ysmxImageView.setOnClickListener(ysmxImageViewOnClickListener);        doclinksImageView.setOnClickListener(doclinksImageViewOnClickListener);        sqjlImageView.setOnClickListener(sqjlImageViewOnClickListener);        workflowBtn.setOnClickListener(workflowBtnOnClickListener);        onClickListener();    }    //设置事件监听    private void onClickListener() {        if (JsonUnit.convertStrToArray(paycheck.getCHECKDATE())[1].equals(GlobalConfig.NOTREADONLY) && appid != null) {            Drawable nav_up = getResources().getDrawable(R.drawable.ic_data);            nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());            checkdateText.setCompoundDrawablesWithIntrinsicBounds(null, null, nav_up, null);            checkdateText.setOnClickListener(DataOnClickListener);        }    }    //验收时间    private View.OnClickListener DataOnClickListener = new View.OnClickListener() {        @Override        public void onClick(final View view) {            final AlertDialog dialog = new AlertDialog.Builder(PaycheckActivity.this).create();            dialog.show();            DatePicker picker = new DatePicker(PaycheckActivity.this);            picker.setDate(DataUtils.getYear(), DataUtils.getMonths());            picker.setMode(DPMode.SINGLE);            picker.setFestivalDisplay(false);            picker.setOnDatePickedListener(new DatePicker.OnDatePickedListener() {                @Override                public void onDatePicked(String date) {                    checkdateText.setText(date);                    dialog.dismiss();                }            });            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);            dialog.getWindow().setContentView(picker, params);            dialog.getWindow().setGravity(Gravity.CENTER);        }    };    @Override    protected String getSubTitle() {        return getString(R.string.fkysmx1_text);    }    //基本信息    private View.OnClickListener jbxxImageViewOnClickListener = new View.OnClickListener() {        @Override        public void onClick(View view) {            if (startAnaim()) {                qtLinearLayout.setVisibility(View.GONE);            } else {                qtLinearLayout.setVisibility(View.VISIBLE);            }        }    };    //启动动画    private boolean startAnaim() {        rotate.setInterpolator(new LinearInterpolator());//设置为线性旋转        rotate.setFillAfter(!rotate.getFillAfter());//每次都取相反值，使得可以不恢复原位的旋转        qtxxImageView.startAnimation(rotate);        return rotate.getFillAfter();    }    //验收明细    private View.OnClickListener ysmxImageViewOnClickListener = new View.OnClickListener() {        @Override        public void onClick(View view) {            Intent intent = new Intent(PaycheckActivity.this, PaychecklineActivity.class);            intent.putExtra("appid", GlobalConfig.PAYCHECK_APPID);            intent.putExtra("paychecknum", JsonUnit.convertStrToArray(paycheck.getPAYCHECKNUM())[0]);            startActivityForResult(intent, 0);        }    };    //附件    private View.OnClickListener doclinksImageViewOnClickListener = new View.OnClickListener() {        @Override        public void onClick(View view) {            Intent intent = new Intent(PaycheckActivity.this, DoclinksActivity.class);            intent.putExtra("ownertable", GlobalConfig.PAYCHECK_NAME);            intent.putExtra("ownerid", JsonUnit.convertStrToArray(paycheck.getPAYCHECKID())[0]);            intent.putExtra("title", getResources().getString(R.string.fj_text));            startActivityForResult(intent, 0);        }    };    //审批记录    private View.OnClickListener sqjlImageViewOnClickListener = new View.OnClickListener() {        @Override        public void onClick(View view) {            Intent intent = new Intent(PaycheckActivity.this, WftransactionActivity.class);            intent.putExtra("ownertable", GlobalConfig.PAYCHECK_NAME);            intent.putExtra("ownerid", JsonUnit.convertStrToArray(paycheck.getPAYCHECKID())[0]);            startActivityForResult(intent, 0);        }    };    //审批    private View.OnClickListener workflowBtnOnClickListener = new View.OnClickListener() {        @Override        public void onClick(View view) {            update(JsonUnit.PaycheckData(JsonUnit.convertStrToArray(paycheck.getPAYCHECKID())[0], checkdateText.getText().toString(), AccountUtils.getpersonId(PaycheckActivity.this), appid));        }    };    //流程启动2017CG00193    private void PostStart(String ownertable, String ownerid, String appid, String userid) {        Rx2AndroidNetworking.post(GlobalConfig.HTTP_URL_START_WORKFLOW)                .addBodyParameter("ownertable", ownertable)                .addBodyParameter("ownerid", ownerid)                .addBodyParameter("appid", appid)                .addBodyParameter("userid", userid)                .build().getStringObservable()                .subscribeOn(Schedulers.io())        // 在io线程进行网络请求                .observeOn(AndroidSchedulers.mainThread()) // 在主线程处理获取数据列表的请求结果                .doOnNext(new Consumer<String>() {                    @Override                    public void accept(@NonNull String string) throws Exception {                    }                })                .subscribeOn(Schedulers.io())                .observeOn(AndroidSchedulers.mainThread())                .subscribe(new Consumer<String>() {                    @Override                    public void accept(@NonNull String s) throws Exception {                        R_WORKFLOW workflow = new Gson().fromJson(s, R_WORKFLOW.class);                        if (workflow.getErrcode().equals(GlobalConfig.WORKFLOW_106)) {                            R_APPROVE r_approve = new Gson().fromJson(s, R_APPROVE.class);                            showDialog(r_approve.getResult());                        } else {                            showMiddleToast(PaycheckActivity.this, workflow.getErrmsg());                        }                    }                }, new Consumer<Throwable>() {                    @Override                    public void accept(@NonNull Throwable throwable) throws Exception {                        showMiddleToast(PaycheckActivity.this, getString(R.string.spsb_text));                    }                });    }    private void PostApprove(String ownertable, String ownerid, String memo, String selectWhat, String userid) {        Rx2AndroidNetworking                .post(GlobalConfig.HTTP_URL_APPROVE_WORKFLOW)                .addBodyParameter("ownertable", ownertable)                .addBodyParameter("ownerid", ownerid)                .addBodyParameter("memo", memo)                .addBodyParameter("selectWhat", selectWhat)                .addBodyParameter("userid", userid)                .build()                .getStringObservable()                .subscribeOn(Schedulers.io())        // 在io线程进行网络请求                .observeOn(AndroidSchedulers.mainThread()) // 在主线程处理获取数据列表的请求结果                .doOnNext(new Consumer<String>() {                    @Override                    public void accept(@NonNull String string) throws Exception {                    }                })                .subscribeOn(Schedulers.io())                .observeOn(AndroidSchedulers.mainThread())                .subscribe(new Consumer<String>() {                    @Override                    public void accept(@NonNull String s) throws Exception {                        R_WORKFLOW workflow = new Gson().fromJson(s, R_WORKFLOW.class);                        showMiddleToast(PaycheckActivity.this, workflow.getErrmsg());                        if (workflow.getErrcode().equals(GlobalConfig.WORKFLOW_103)) {                            setResult(ActiveTaskActivity.TASK_RESULTCODE);                            finish();                        }                    }                }, new Consumer<Throwable>() {                    @Override                    public void accept(@NonNull Throwable throwable) throws Exception {                        showMiddleToast(PaycheckActivity.this, getString(R.string.spsb_text));                    }                });    }    //弹出对话框    public void showDialog(List<R_APPROVE.Result> results) {//        ConfirmDialog.Builder dialog = new ConfirmDialog.Builder(this);        dialog.setTitle("审批")                .setData(results)                .setPositiveButton("确定", new ConfirmDialog.Builder.cOnClickListener() {                    @Override                    public void cOnClickListener(DialogInterface dialogInterface, R_APPROVE.Result result, String memo) {                        dialogInterface.dismiss();                        PostApprove(ownertable, JsonUnit.convertStrToArray(paycheck.getPAYCHECKID())[0], memo, result.getIspositive(), AccountUtils.getpersonId(PaycheckActivity.this));                    }                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {            @Override            public void onClick(DialogInterface dialogInterface, int i) {                dialogInterface.dismiss();            }        }).create().show();    }    @Override    protected void onActivityResult(int requestCode, int resultCode, Intent data) {        super.onActivityResult(requestCode, resultCode, data);        switch (requestCode) {            case GlobalConfig.PERSON_REQUESTCODE: //执行人                if (resultCode == GlobalConfig.CUDEPT_REQUESTCODE) {                    PERSON persion = (PERSON) data.getSerializableExtra("person");                    personnum = JsonUnit.convertStrToArray(persion.getPERSONID())[0];                }                break;            case GlobalConfig.RDCHEAD_REQUESTCODE: //中心分管领导                if (resultCode == GlobalConfig.CUDEPT_REQUESTCODE) {                    PERSON persion = (PERSON) data.getSerializableExtra("person");                    rdchead = JsonUnit.convertStrToArray(persion.getPERSONID())[0];                }                break;        }    }    //更新操作    private void update(String data) {        Rx2AndroidNetworking                .post(GlobalConfig.HTTP_URL_SEARCH)                .addBodyParameter("data", data) //数据                .build()                .getStringObservable()                .subscribeOn(Schedulers.io())        // 在io线程进行网络请求                .observeOn(AndroidSchedulers.mainThread()) // 在主线程处理获取数据列表的请求结果                .doOnNext(new Consumer<String>() {                    @Override                    public void accept(@NonNull String string) throws Exception {                    }                })                .subscribeOn(Schedulers.io())                .observeOn(AndroidSchedulers.mainThread())                .subscribe(new Consumer<String>() {                    @Override                    public void accept(@NonNull String s) throws Exception {                        R_WORKFLOW r = new Gson().fromJson(s, R_WORKFLOW.class);                        if (r.getErrcode().equals(GlobalConfig.GETDATASUCCESS)) {                            PostStart(ownertable, JsonUnit.convertStrToArray(paycheck.getPAYCHECKID())[0], appid, AccountUtils.getpersonId(PaycheckActivity.this));                        } else {                            showMiddleToast(PaycheckActivity.this, r.getErrmsg());                        }                    }                }, new Consumer<Throwable>() {                    @Override                    public void accept(@NonNull Throwable throwable) throws Exception {                        showMiddleToast(PaycheckActivity.this, getString(R.string.spsb_text));                    }                });    }    //根据appid,grnum,objctname获取国内出差信息    private void getNetWorkPAYCHECK() {        String data = HttpManager.getPAYCHECKUrl(appid, ownernum, AccountUtils.getpersonId(this), 1, 10);        Rx2AndroidNetworking.post(GlobalConfig.HTTP_URL_SEARCH)                .addBodyParameter("data", data)                .build()                .getObjectObservable(R_PAYCHECK.class) // 发起获取数据列表的请求，并解析到FootList                .subscribeOn(Schedulers.io())        // 在io线程进行网络请求                .observeOn(AndroidSchedulers.mainThread()) // 在主线程处理获取数据列表的请求结果                .doOnNext(new Consumer<R_PAYCHECK>() {                    @Override                    public void accept(@NonNull R_PAYCHECK r_paycheck) throws Exception {                    }                })                .map(new Function<R_PAYCHECK, R_PAYCHECK.ResultBean>() {                    @Override                    public R_PAYCHECK.ResultBean apply(@NonNull R_PAYCHECK r_paycheck) throws Exception {                        return r_paycheck.getResult();                    }                })                .map(new Function<R_PAYCHECK.ResultBean, List<PAYCHECK>>() {                    @Override                    public List<PAYCHECK> apply(@NonNull R_PAYCHECK.ResultBean resultBean) throws Exception {                        return resultBean.getResultlist();                    }                })                .subscribeOn(Schedulers.io())                .observeOn(AndroidSchedulers.mainThread())                .subscribe(new Consumer<List<PAYCHECK>>() {                    @Override                    public void accept(@NonNull List<PAYCHECK> paychecks) throws Exception {                        dismissLoadingDialog();                        if (paychecks == null || paychecks.isEmpty()) {                        } else {                            paycheck = paychecks.get(0);                            showData();                        }                    }                }, new Consumer<Throwable>() {                    @Override                    public void accept(@NonNull Throwable throwable) throws Exception {                        dismissLoadingDialog();                    }                });    }}
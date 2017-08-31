package com.hqxh.fiamproperty.ui.activity;import android.app.AlertDialog;import android.content.DialogInterface;import android.content.Intent;import android.graphics.drawable.Drawable;import android.os.Bundle;import android.util.Log;import android.view.Gravity;import android.view.View;import android.view.ViewGroup;import android.view.animation.Animation;import android.view.animation.AnimationUtils;import android.view.animation.LinearInterpolator;import android.widget.AdapterView;import android.widget.Button;import android.widget.ImageView;import android.widget.LinearLayout;import android.widget.RelativeLayout;import android.widget.ScrollView;import android.widget.TextView;import com.google.gson.Gson;import com.hqxh.fiamproperty.R;import com.hqxh.fiamproperty.animation.BaseAnimatorSet;import com.hqxh.fiamproperty.animation.BounceTopEnter;import com.hqxh.fiamproperty.animation.SlideBottomExit;import com.hqxh.fiamproperty.api.HttpManager;import com.hqxh.fiamproperty.base.BaseTitleActivity;import com.hqxh.fiamproperty.bean.R_APPROVE;import com.hqxh.fiamproperty.bean.R_WORKFLOW;import com.hqxh.fiamproperty.constant.GlobalConfig;import com.hqxh.fiamproperty.dateviews.cons.DPMode;import com.hqxh.fiamproperty.listener.OnOperItemClickL;import com.hqxh.fiamproperty.model.R_CUDEPT.CUDEPT;import com.hqxh.fiamproperty.model.R_Workorder;import com.hqxh.fiamproperty.model.R_Workorder.Workorder;import com.hqxh.fiamproperty.model.R_ZXPERSON.PERSON;import com.hqxh.fiamproperty.ui.widget.ConfirmDialog;import com.hqxh.fiamproperty.ui.widget.DatePicker;import com.hqxh.fiamproperty.ui.widget.NormalListDialog;import com.hqxh.fiamproperty.unit.AccountUtils;import com.hqxh.fiamproperty.unit.DataUtils;import com.hqxh.fiamproperty.unit.JsonUnit;import com.rx2androidnetworking.Rx2AndroidNetworking;import java.util.List;import io.reactivex.android.schedulers.AndroidSchedulers;import io.reactivex.annotations.NonNull;import io.reactivex.functions.Consumer;import io.reactivex.functions.Function;import io.reactivex.schedulers.Schedulers;/** * 试制任务单 **/public class SzrwdWorkorderActivity extends BaseTitleActivity {    private static final String TAG = "SzrwdWorkorderActivity";    private ScrollView scrollView;    /**     * 信息展示     **/    private TextView wonumText; //任务单    private TextView udtodescText; //任务单描述    private TextView udtotype3Text; //试制类型    private TextView cuquantityText; //试制数量    private TextView udpaythisyearText; //本年度是否结算    private TextView statusText; //状态    private TextView udszdate1Text; //任务到达时间    private TextView projectidText; //费用号    private TextView supervisorText; //子项目经理    private TextView projectdescText; //项目名称    private TextView pmText; //项目经理    private TextView reportedbynameText; //提单人    private TextView reportedbycudeptText; //部门    private TextView reportedbycucrewText; //科室    private TextView reportdateText; //提单日期    private TextView phonenumText; //电话    private ImageView qtxxImageView;  //其它信息    private LinearLayout qtLinearLayout;//其它布局    private TextView udtargstartdateText; //建议完成时间    private TextView udtargcompdateText; //要求完成时间    private TextView actfinishText; //任务完成时间    private TextView cudeptText; //执行部门    private TextView cucrewText; //执行科室    private TextView ownernameText; //子项目负责人    private ImageView sqjlImageView; //审批记录    private Button workflowBtn;    private RelativeLayout workflowRelativeLayout;//布局    private Workorder workorder;    private Animation rotate;    private String deptnum; //部门编码    private String cudept; //部门    private String cucrew; //科室    private String personnum;//人员编号    private int mark = 0;//跳转标识    private String appid; //appid    private String ownernum;//ownernum    private String ownertable;//ownertable    private String[] optionList = new String[]{"否", "是"};    private BaseAnimatorSet mBasIn;    private BaseAnimatorSet mBasOut;    @Override    protected void beforeInit() {        super.beforeInit();        if (getIntent().hasExtra("workorder")) {            workorder = (Workorder) getIntent().getSerializableExtra("workorder");        }        if (getIntent().hasExtra("mark")) {            mark = getIntent().getExtras().getInt("mark");        }        if (getIntent().hasExtra("appid")) {            appid = getIntent().getExtras().getString("appid");        }        if (getIntent().hasExtra("ownernum")) {            ownernum = getIntent().getExtras().getString("ownernum");        }        if (getIntent().hasExtra("ownertable")) {            ownertable = getIntent().getExtras().getString("ownertable");        }    }    @Override    protected int getContentViewLayoutID() {        return R.layout.activity_szrwd_workorder;    }    @Override    protected void initView(Bundle savedInstanceState) {        scrollView = (ScrollView) findViewById(R.id.scrollView_id);        wonumText = (TextView) findViewById(R.id.wonum_text_id);        udtodescText = (TextView) findViewById(R.id.udtodesc_text_id);        udtotype3Text = (TextView) findViewById(R.id.udtotype3_text_id);        cuquantityText = (TextView) findViewById(R.id.cuquantity_text_id);        udpaythisyearText = (TextView) findViewById(R.id.udpaythisyear_text_id);        statusText = (TextView) findViewById(R.id.status_text_id);        udszdate1Text = (TextView) findViewById(R.id.udszdate1_text_id);        projectidText = (TextView) findViewById(R.id.projectid_text_id);        supervisorText = (TextView) findViewById(R.id.supervisor_text_id);        projectdescText = (TextView) findViewById(R.id.projectdesc_text_id);        pmText = (TextView) findViewById(R.id.pm_text_id);        reportedbynameText = (TextView) findViewById(R.id.reportedbyname_text_id);        reportedbycudeptText = (TextView) findViewById(R.id.reportedbycudept_text_id);        reportedbycucrewText = (TextView) findViewById(R.id.reportedbycucrew_text_id);        reportdateText = (TextView) findViewById(R.id.reportdate_text_id);        phonenumText = (TextView) findViewById(R.id.phonenum_text_id);        qtxxImageView = (ImageView) findViewById(R.id.jbxx_kz_imageview_id);        qtLinearLayout = (LinearLayout) findViewById(R.id.linearLayout_id);        udtargstartdateText = (TextView) findViewById(R.id.udtargstartdate_text_id);        udtargcompdateText = (TextView) findViewById(R.id.udtargcompdate_text_id);        actfinishText = (TextView) findViewById(R.id.actfinish_text_id);        cudeptText = (TextView) findViewById(R.id.cudept_text_id);        cucrewText = (TextView) findViewById(R.id.cucrew_text_id);        ownernameText = (TextView) findViewById(R.id.ownername_text_id);        sqjlImageView = (ImageView) findViewById(R.id.sqjl_imageview_id);        workflowBtn = (Button) findViewById(R.id.workflow_btn_id);        mBasIn = new BounceTopEnter();        mBasOut = new SlideBottomExit();        workflowRelativeLayout = (RelativeLayout) findViewById(R.id.relavtivelayout_btn_id);        if (null == appid) {            showData();        } else {            if (mark == HomeActivity.DB_CODE) { //待办任务                workflowRelativeLayout.setVisibility(View.VISIBLE);                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);                layoutParams.setMargins(0, 0, 0, getHeight(workflowRelativeLayout));//4个参数按顺序分别是左上右下                scrollView.setLayoutParams(layoutParams);            }            showLoadingDialog(getResources().getString(R.string.loading_hint));            getNetWorkWorkOrder();        }    }    //展示界面数据    private void showData() {        wonumText.setText(JsonUnit.convertStrToArray(workorder.getWONUM())[0] + "," + JsonUnit.convertStrToArray(workorder.getDESCRIPTION())[0]);        udtodescText.setText(JsonUnit.convertStrToArray(workorder.getUDTODESC())[0]);        udtotype3Text.setText(JsonUnit.convertStrToArray(workorder.getUDTOTYPE3())[0]);        cuquantityText.setText(JsonUnit.convertStrToArray(workorder.getCUQUANTITY())[0]);        udpaythisyearText.setText(JsonUnit.convertStrToArray(workorder.getUDPAYTHISYEAR())[0]);        statusText.setText(JsonUnit.convertStrToArray(workorder.getSTATUSDESC())[0]);        udszdate1Text.setText(JsonUnit.convertStrToArray(workorder.getUDSZDATE1())[0]);        projectidText.setText(JsonUnit.convertStrToArray(workorder.getPROJECTID())[0]);        supervisorText.setText(JsonUnit.convertStrToArray(workorder.getSUPERVISOR())[0]);        projectdescText.setText(JsonUnit.convertStrToArray(workorder.getPROJECTDESC())[0]);        pmText.setText(JsonUnit.convertStrToArray(workorder.getPM())[0]);        reportedbynameText.setText(JsonUnit.convertStrToArray(workorder.getREPORTEDBYNAME())[0]);        reportedbycudeptText.setText(JsonUnit.convertStrToArray(workorder.getREPORTEDBYCUDEPT())[0]);        reportedbycucrewText.setText((JsonUnit.convertStrToArray(workorder.getREPORTEDBYCUCREW())[0]));        reportdateText.setText(JsonUnit.convertStrToArray(workorder.getREPORTDATE())[0]);        phonenumText.setText(JsonUnit.convertStrToArray(workorder.getREPORTEDBYPHONE())[0]);        udtargstartdateText.setText(JsonUnit.strToDateString(JsonUnit.convertStrToArray(workorder.getUDTARGSTARTDATE())[0]));        udtargcompdateText.setText(JsonUnit.strToDateString(JsonUnit.convertStrToArray(workorder.getUDTARGCOMPDATE())[0]));        actfinishText.setText(JsonUnit.strToDateString(JsonUnit.convertStrToArray(workorder.getACTFINISH())[0]));        cudeptText.setText(JsonUnit.convertStrToArray(workorder.getCUDEPT())[0]);        cucrewText.setText(JsonUnit.convertStrToArray(workorder.getCUCREW())[0]);        ownernameText.setText(JsonUnit.convertStrToArray(workorder.getOWNERNAME())[0]);        deptnum = JsonUnit.convertStrToArray(workorder.getDEPTNUM())[0];        cudept = JsonUnit.convertStrToArray(workorder.getCUDEPT())[0];        cucrew = JsonUnit.convertStrToArray(workorder.getCUCREW())[0];        personnum = JsonUnit.convertStrToArray(workorder.getOWNER())[0];        rotate = AnimationUtils.loadAnimation(this, R.anim.arrow_rotate);//创建动画        qtxxImageView.setOnClickListener(jbxxImageViewOnClickListener);        sqjlImageView.setOnClickListener(sqjlImageViewOnClickListener);        workflowBtn.setOnClickListener(workflowBtnOnClickListener);        onClickListener();    }    //设置事件监听    private void onClickListener() {        if (JsonUnit.convertStrToArray(workorder.getUDPAYTHISYEAR())[1].equals(GlobalConfig.NOTREADONLY)) {            Drawable nav_up = getResources().getDrawable(R.drawable.ic_keyboard_arrow_down_black);            nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());            udpaythisyearText.setCompoundDrawablesWithIntrinsicBounds(null, null, nav_up, null);            udpaythisyearText.setOnClickListener(udpaythisyearTextOnClickListener);        }        if (JsonUnit.convertStrToArray(workorder.getUDTARGCOMPDATE())[1].equals(GlobalConfig.NOTREADONLY)) {            Drawable nav_up = getResources().getDrawable(R.drawable.ic_data);            nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());            udtargcompdateText.setCompoundDrawablesWithIntrinsicBounds(null, null, nav_up, null);            udtargcompdateText.setOnClickListener(udtargcompdateTextOnClickListener);        }        if (JsonUnit.convertStrToArray(workorder.getCUDEPT())[1].equals(GlobalConfig.NOTREADONLY)) {            Drawable nav_up = getResources().getDrawable(R.drawable.ic_bm);            nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());            cudeptText.setCompoundDrawablesWithIntrinsicBounds(null, null, nav_up, null);            cudeptText.setOnClickListener(cudeptTextOnClickListener);        }        if (JsonUnit.convertStrToArray(workorder.getCUCREW())[1].equals(GlobalConfig.NOTREADONLY)) {            Drawable nav_up = getResources().getDrawable(R.drawable.ic_ks);            nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());            cucrewText.setCompoundDrawablesWithIntrinsicBounds(null, null, nav_up, null);            cucrewText.setOnClickListener(cucrewTextOnClickListener);        }        if (JsonUnit.convertStrToArray(workorder.getOWNERNAME())[1].equals(GlobalConfig.NOTREADONLY)) {            Drawable nav_up = getResources().getDrawable(R.drawable.ic_person_black);            nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());            ownernameText.setCompoundDrawablesWithIntrinsicBounds(null, null, nav_up, null);            ownernameText.setOnClickListener(ownernameTextOnClickListener);        }    }    //年度预算选择    private View.OnClickListener udpaythisyearTextOnClickListener = new View.OnClickListener() {        @Override        public void onClick(View view) {            final NormalListDialog normalListDialog = new NormalListDialog(SzrwdWorkorderActivity.this, optionList);            normalListDialog.title("选择项")                    .showAnim(mBasIn)//                    .dismissAnim(mBasOut)//                    .show();            normalListDialog.setOnOperItemClickL(new OnOperItemClickL() {                @Override                public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {                    udpaythisyearText.setText(optionList[position]);                    normalListDialog.dismiss();                }            });        }    };    //日期选择    private View.OnClickListener udtargcompdateTextOnClickListener = new View.OnClickListener() {        @Override        public void onClick(View view) {            final AlertDialog dialog = new AlertDialog.Builder(SzrwdWorkorderActivity.this).create();            dialog.show();            DatePicker picker = new DatePicker(SzrwdWorkorderActivity.this);            picker.setDate(DataUtils.getYear(), DataUtils.getMonths());            picker.setMode(DPMode.SINGLE);            picker.setFestivalDisplay(false);            picker.setOnDatePickedListener(new DatePicker.OnDatePickedListener() {                @Override                public void onDatePicked(String date) {                    udtargcompdateText.setText(date);                    dialog.dismiss();                }            });            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);            dialog.getWindow().setContentView(picker, params);            dialog.getWindow().setGravity(Gravity.CENTER);        }    };    //执行部门    private View.OnClickListener cudeptTextOnClickListener = new View.OnClickListener() {        @Override        public void onClick(View view) {            Intent intent = new Intent(SzrwdWorkorderActivity.this, CudeptActivity.class);            intent.putExtra("title", getResources().getString(R.string.cucrew_text));            intent.putExtra("appid", GlobalConfig.USER_APPID);            startActivityForResult(intent, GlobalConfig.CUDEPT_REQUESTCODE);        }    };    //执行科室    private View.OnClickListener cucrewTextOnClickListener = new View.OnClickListener() {        @Override        public void onClick(View view) {            if (null == deptnum) {                showMiddleToast(SzrwdWorkorderActivity.this, "请选择执行部门");            } else {                Intent intent = new Intent(SzrwdWorkorderActivity.this, CudeptActivity.class);                intent.putExtra("title", getResources().getString(R.string.cucrew_text));                intent.putExtra("appid", GlobalConfig.ROLE_APPID);                intent.putExtra("deptnum", deptnum);                startActivityForResult(intent, GlobalConfig.CUDEPTKS_REQUESTCODE);            }        }    };    //执行人    private View.OnClickListener ownernameTextOnClickListener = new View.OnClickListener() {        @Override        public void onClick(View view) {            Log.e(TAG, "cudept=" + cudept + ",cucrew=" + cucrew + ",cont=" + cucrew.length());            if (cudept.isEmpty() || cucrew.isEmpty() || cucrew.length() == 0) {                showMiddleToast(SzrwdWorkorderActivity.this, "请选择执行部门或执行科室");            } else {                Intent intent = new Intent(SzrwdWorkorderActivity.this, PersonActivity.class);                intent.putExtra("appid", GlobalConfig.ROLE_APPID);                intent.putExtra("cudept", cudept);                intent.putExtra("cucrew", cucrew);                startActivityForResult(intent, GlobalConfig.PERSON_REQUESTCODE);            }        }    };    @Override    protected String getSubTitle() {        return getString(R.string.szrwdxq_text);    }    //基本信息    private View.OnClickListener jbxxImageViewOnClickListener = new View.OnClickListener() {        @Override        public void onClick(View view) {            if (startAnaim()) {                qtLinearLayout.setVisibility(View.GONE);            } else {                qtLinearLayout.setVisibility(View.VISIBLE);            }        }    };    //启动动画    private boolean startAnaim() {        rotate.setInterpolator(new LinearInterpolator());//设置为线性旋转        rotate.setFillAfter(!rotate.getFillAfter());//每次都取相反值，使得可以不恢复原位的旋转        qtxxImageView.startAnimation(rotate);        return rotate.getFillAfter();    }    //审批记录    private View.OnClickListener sqjlImageViewOnClickListener = new View.OnClickListener() {        @Override        public void onClick(View view) {            Intent intent = new Intent(SzrwdWorkorderActivity.this, WftransactionActivity.class);            intent.putExtra("ownertable", GlobalConfig.WORKORDER_NAME);            intent.putExtra("ownerid", JsonUnit.convertStrToArray(workorder.getWORKORDERID())[0]);            startActivityForResult(intent, 0);        }    };    //审批    private View.OnClickListener workflowBtnOnClickListener = new View.OnClickListener() {        @Override        public void onClick(View view) {            update(JsonUnit.workorderData(JsonUnit.convertStrToArray(workorder.getWORKORDERID())[0], cudeptText.getText().toString(), udtargcompdateText.getText().toString(), personnum, cucrewText.getText().toString(), udpaythisyearText.getText().toString(), AccountUtils.getpersonId(SzrwdWorkorderActivity.this), GlobalConfig.TOSZ_APPID));        }    };    //流程启动    private void PostStart(String ownertable, String ownerid, String appid, String userid) {        Log.e(TAG, "ownertable=" + ownertable + ",ownerid=" + ownerid + ",appid=" + appid + ",userid=" + userid);        Rx2AndroidNetworking.post(GlobalConfig.HTTP_URL_START_WORKFLOW)                .addBodyParameter("ownertable", ownertable)                .addBodyParameter("ownerid", ownerid)                .addBodyParameter("appid", appid)                .addBodyParameter("userid", userid)                .build().getStringObservable()                .subscribeOn(Schedulers.io())        // 在io线程进行网络请求                .observeOn(AndroidSchedulers.mainThread()) // 在主线程处理获取数据列表的请求结果                .doOnNext(new Consumer<String>() {                    @Override                    public void accept(@NonNull String string) throws Exception {                    }                })                .subscribeOn(Schedulers.io())                .observeOn(AndroidSchedulers.mainThread())                .subscribe(new Consumer<String>() {                    @Override                    public void accept(@NonNull String s) throws Exception {                        R_WORKFLOW workflow = new Gson().fromJson(s, R_WORKFLOW.class);                        if (workflow.getErrcode().equals(GlobalConfig.WORKFLOW_106)) {                            R_APPROVE r_approve = new Gson().fromJson(s, R_APPROVE.class);                            showDialog(r_approve.getResult());                        } else {                            showMiddleToast(SzrwdWorkorderActivity.this, workflow.getErrmsg());                        }                    }                }, new Consumer<Throwable>() {                    @Override                    public void accept(@NonNull Throwable throwable) throws Exception {                        showMiddleToast(SzrwdWorkorderActivity.this, getString(R.string.spsb_text));                    }                });    }    private void PostApprove(String ownertable, String ownerid, String memo, String selectWhat, String userid) {        Rx2AndroidNetworking                .post(GlobalConfig.HTTP_URL_APPROVE_WORKFLOW)                .addBodyParameter("ownertable", ownertable)                .addBodyParameter("ownerid", ownerid)                .addBodyParameter("memo", memo)                .addBodyParameter("selectWhat", selectWhat)                .addBodyParameter("userid", userid)                .build()                .getStringObservable()                .subscribeOn(Schedulers.io())        // 在io线程进行网络请求                .observeOn(AndroidSchedulers.mainThread()) // 在主线程处理获取数据列表的请求结果                .doOnNext(new Consumer<String>() {                    @Override                    public void accept(@NonNull String string) throws Exception {                    }                })                .subscribeOn(Schedulers.io())                .observeOn(AndroidSchedulers.mainThread())                .subscribe(new Consumer<String>() {                    @Override                    public void accept(@NonNull String s) throws Exception {                        Log.e(TAG, "审批=" + s);                        R_WORKFLOW workflow = new Gson().fromJson(s, R_WORKFLOW.class);                        showMiddleToast(SzrwdWorkorderActivity.this, workflow.getErrmsg());                        if (workflow.getErrcode().equals(GlobalConfig.WORKFLOW_103)) {                            setResult(ActiveTaskActivity.TASK_RESULTCODE);                            finish();                        }                    }                }, new Consumer<Throwable>() {                    @Override                    public void accept(@NonNull Throwable throwable) throws Exception {                        showMiddleToast(SzrwdWorkorderActivity.this, getString(R.string.spsb_text));                    }                });    }    //弹出对话框    public void showDialog(List<R_APPROVE.Result> results) {//        ConfirmDialog.Builder dialog = new ConfirmDialog.Builder(this);        dialog.setTitle("审批")                .setData(results)                .setPositiveButton("确定", new ConfirmDialog.Builder.cOnClickListener() {                    @Override                    public void cOnClickListener(DialogInterface dialogInterface, R_APPROVE.Result result, String memo) {                        dialogInterface.dismiss();                        PostApprove(ownertable, JsonUnit.convertStrToArray(workorder.getWORKORDERID())[0], memo, result.getIspositive(), AccountUtils.getpersonId(SzrwdWorkorderActivity.this));                    }                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {            @Override            public void onClick(DialogInterface dialogInterface, int i) {                dialogInterface.dismiss();            }        }).create().show();    }    @Override    protected void onActivityResult(int requestCode, int resultCode, Intent data) {        super.onActivityResult(requestCode, resultCode, data);        switch (requestCode) {            case GlobalConfig.CUDEPT_REQUESTCODE: //部门                if (resultCode == GlobalConfig.CUDEPT_REQUESTCODE) {                    CUDEPT cudeptt = (CUDEPT) data.getSerializableExtra("cudept");                    cudeptText.setText(JsonUnit.convertStrToArray(cudeptt.getDESCRIPTION())[0]);                    deptnum = JsonUnit.convertStrToArray(cudeptt.getDEPTNUM())[0];                    cudept = JsonUnit.convertStrToArray(cudeptt.getDESCRIPTION())[0];                }                break;            case GlobalConfig.CUDEPTKS_REQUESTCODE: //科室                if (resultCode == GlobalConfig.CUDEPT_REQUESTCODE) {                    CUDEPT cudept1 = (CUDEPT) data.getSerializableExtra("cudept");                    cucrewText.setText(JsonUnit.convertStrToArray(cudept1.getDESCRIPTION())[0]);                    cucrew = JsonUnit.convertStrToArray(cudept1.getDESCRIPTION())[0];                }                break;            case GlobalConfig.PERSON_REQUESTCODE: //子项目负责人                if (resultCode == GlobalConfig.CUDEPT_REQUESTCODE) {                    PERSON persion = (PERSON) data.getSerializableExtra("person");                    personnum = JsonUnit.convertStrToArray(persion.getPERSONID())[0];                    ownernameText.setText(JsonUnit.convertStrToArray(persion.getDISPLAYNAME())[0]);                }                break;        }    }    //更新操作    private void update(String data) {        Rx2AndroidNetworking                .post(GlobalConfig.HTTP_URL_SEARCH)                .addBodyParameter("data", data) //数据                .build()                .getStringObservable()                .subscribeOn(Schedulers.io())        // 在io线程进行网络请求                .observeOn(AndroidSchedulers.mainThread()) // 在主线程处理获取数据列表的请求结果                .doOnNext(new Consumer<String>() {                    @Override                    public void accept(@NonNull String string) throws Exception {                    }                })                .subscribeOn(Schedulers.io())                .observeOn(AndroidSchedulers.mainThread())                .subscribe(new Consumer<String>() {                    @Override                    public void accept(@NonNull String s) throws Exception {                        R_WORKFLOW r = new Gson().fromJson(s, R_WORKFLOW.class);                        if (r.getErrcode().equals(GlobalConfig.GETDATASUCCESS)) {                            PostStart(ownertable, JsonUnit.convertStrToArray(workorder.getWORKORDERID())[0], GlobalConfig.TOSZ_APPID, AccountUtils.getpersonId(SzrwdWorkorderActivity.this));                        } else {                            showMiddleToast(SzrwdWorkorderActivity.this, r.getErrmsg());                        }                    }                }, new Consumer<Throwable>() {                    @Override                    public void accept(@NonNull Throwable throwable) throws Exception {                        showMiddleToast(SzrwdWorkorderActivity.this, getString(R.string.spsb_text));                    }                });    }    //根据appid,grnum,objctname获取国内出差信息    private void getNetWorkWorkOrder() {        String data = HttpManager.getWORKORDERUrl(appid, ownertable, ownernum, AccountUtils.getpersonId(SzrwdWorkorderActivity.this), 1, 10);        Rx2AndroidNetworking.post(GlobalConfig.HTTP_URL_SEARCH)                .addBodyParameter("data", data)                .build()                .getObjectObservable(R_Workorder.class) // 发起获取数据列表的请求，并解析到FootList                .subscribeOn(Schedulers.io())        // 在io线程进行网络请求                .observeOn(AndroidSchedulers.mainThread()) // 在主线程处理获取数据列表的请求结果                .doOnNext(new Consumer<R_Workorder>() {                    @Override                    public void accept(@NonNull R_Workorder r_workorder) throws Exception {                    }                })                .map(new Function<R_Workorder, R_Workorder.ResultBean>() {                    @Override                    public R_Workorder.ResultBean apply(@NonNull R_Workorder r_workorder) throws Exception {                        return r_workorder.getResult();                    }                })                .map(new Function<R_Workorder.ResultBean, List<R_Workorder.Workorder>>() {                    @Override                    public List<R_Workorder.Workorder> apply(@NonNull R_Workorder.ResultBean resultBean) throws Exception {                        return resultBean.getResultlist();                    }                })                .subscribeOn(Schedulers.io())                .observeOn(AndroidSchedulers.mainThread())                .subscribe(new Consumer<List<R_Workorder.Workorder>>() {                    @Override                    public void accept(@NonNull List<R_Workorder.Workorder> workorders) throws Exception {                        dismissLoadingDialog();                        if (workorders == null || workorders.isEmpty()) {                        } else {                            workorder = workorders.get(0);                            showData();                        }                    }                }, new Consumer<Throwable>() {                    @Override                    public void accept(@NonNull Throwable throwable) throws Exception {                        dismissLoadingDialog();                    }                });    }}
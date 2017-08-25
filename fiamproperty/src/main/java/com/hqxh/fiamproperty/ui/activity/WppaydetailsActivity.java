package com.hqxh.fiamproperty.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hqxh.fiamproperty.R;
import com.hqxh.fiamproperty.api.HttpManager;
import com.hqxh.fiamproperty.base.BaseTitleActivity;
import com.hqxh.fiamproperty.bean.R_APPROVE;
import com.hqxh.fiamproperty.bean.R_WORKFLOW;
import com.hqxh.fiamproperty.constant.GlobalConfig;
import com.hqxh.fiamproperty.model.R_GR;
import com.hqxh.fiamproperty.model.R_PR;
import com.hqxh.fiamproperty.model.R_PR.PR;
import com.hqxh.fiamproperty.ui.widget.ConfirmDialog;
import com.hqxh.fiamproperty.unit.AccountUtils;
import com.hqxh.fiamproperty.unit.JsonUnit;
import com.rx2androidnetworking.Rx2AndroidNetworking;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


/**
 * 外培采购详情
 */

public class WppaydetailsActivity extends BaseTitleActivity {
    private static final String TAG = "WppaydetailsActivity";

    private ScrollView scrollView;
    /*  展示*/
    private TextView prnum_text;//申请单
    private TextView projectid_text;//费用号
    private TextView status_text;//状态
    private TextView udpcowner_text;//参加人
    private TextView udremarks_text;//培训班编号
    private TextView udremarks1_text;//培训内容

    private ImageView jbxx_text;//其它信息
    private LinearLayout jbxxlinearlayout;

    private TextView sqr_text;//申请人
    private TextView reportdate_text;//申请日期
    private TextView phone_text;//电话
    private TextView uddate1_text;//开始日期
    private TextView uddate2_text;//结束日期

    private TextView udremarks4_text;//地点及单位
    // private TextView linecost_text;//培训预算
    private TextView udremark_text;//从事本专业岗位工作及承担项目情况
    private TextView udremarks2_text;//本专业技术现状，培训理由及目的

    private ImageView spjl_text;//审批记录

    private Button workflowBtn;//审批按钮
    private RelativeLayout workflowRelativeLayout;//布局


    private PR pr;

    private Animation rotate;

    private int mark = 0;//跳转标识
    private String appid; //appid
    private String ownernum;//ownernum
    private String ownertable;//ownertable

    protected void beforeInit() {
        super.beforeInit();
        if (getIntent().hasExtra("pr")) {
            pr = (PR) getIntent().getExtras().getSerializable("pr");
        }
        if (getIntent().hasExtra("mark")) {
            mark = getIntent().getExtras().getInt("mark");
        }
        if (getIntent().hasExtra("appid")) {
            appid = getIntent().getExtras().getString("appid");
        }
        if (getIntent().hasExtra("ownernum")) {
            ownernum = getIntent().getExtras().getString("ownernum");
        }
        if (getIntent().hasExtra("ownertable")) {
            ownertable = getIntent().getExtras().getString("ownertable");
        }


    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_wppaydetails;

    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        scrollView = (ScrollView) findViewById(R.id.scrollView_id);
        prnum_text = (TextView) findViewById(R.id.prnum_text);
        projectid_text = (TextView) findViewById(R.id.projectid_text);
        status_text = (TextView) findViewById(R.id.status_text);
        udpcowner_text = (TextView) findViewById(R.id.udpcowner_text);
        udremarks_text = (TextView) findViewById(R.id.udremarks_text);
        udremarks1_text = (TextView) findViewById(R.id.udremarks1_text);
        jbxx_text = (ImageView) findViewById(R.id.jbxx_text);
        sqr_text = (TextView) findViewById(R.id.sqr_text);
        reportdate_text = (TextView) findViewById(R.id.reportdate_text);
        phone_text = (TextView) findViewById(R.id.phone_text);
        uddate1_text = (TextView) findViewById(R.id.uddate1_text);
        uddate2_text = (TextView) findViewById(R.id.uddate2_text);
        udremarks4_text = (TextView) findViewById(R.id.udremarks4_text);
        // linecost_text=(TextView)findViewById(R.id.linecost_text);
        udremark_text = (TextView) findViewById(R.id.udremark_text);
        udremarks2_text = (TextView) findViewById(R.id.udremarks2_text);
        jbxxlinearlayout = (LinearLayout) findViewById(R.id.jbxx_text_id);

        spjl_text = (ImageView) findViewById(R.id.spjl_text);

        workflowBtn = (Button) findViewById(R.id.workflow_btn_id);
        workflowRelativeLayout = (RelativeLayout) findViewById(R.id.relavtivelayout_btn_id);
        if (null == appid) {
            showData();
        } else {
            if (mark == HomeActivity.DB_CODE) { //待办任务
                workflowRelativeLayout.setVisibility(View.VISIBLE);
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                layoutParams.setMargins(0, 0, 0, getHeight(workflowRelativeLayout));//4个参数按顺序分别是左上右下
                scrollView.setLayoutParams(layoutParams);
            }
            getNetWorkPR();
        }
    }

    private void showData() {
        prnum_text.setText(JsonUnit.convertStrToArray(pr.getPRNUM())[0]);
        projectid_text.setText(JsonUnit.convertStrToArray(pr.getPROJECTID())[0]);
        status_text.setText(JsonUnit.convertStrToArray(pr.getSTATUSDESC())[0]);
        udpcowner_text.setText(JsonUnit.convertStrToArray(pr.getPCOWNER())[0]);
        udremarks_text.setText(JsonUnit.convertStrToArray(pr.getUDREMARK2())[0]);
        udremarks1_text.setText(JsonUnit.convertStrToArray(pr.getUDREMARK3())[0]);
        sqr_text.setText(JsonUnit.convertStrToArray(pr.getREQBYPERSON())[0]);
        reportdate_text.setText(JsonUnit.strToDateString(JsonUnit.convertStrToArray(pr.getISSUEDATE())[0]));
        phone_text.setText(JsonUnit.convertStrToArray(pr.getREQBYPHONE())[0]);
        uddate1_text.setText(JsonUnit.strToDateString(JsonUnit.convertStrToArray(pr.getUDDATE1())[0]));
        uddate2_text.setText(JsonUnit.strToDateString(JsonUnit.convertStrToArray(pr.getUDDATE2())[0]));
        udremarks4_text.setText(JsonUnit.convertStrToArray(pr.getUDREMARK4())[0]);

        udremark_text.setText(JsonUnit.convertStrToArray(pr.getUDREMARK())[0]);
        udremarks2_text.setText(JsonUnit.convertStrToArray(pr.getUDREMARK1())[0]);

        rotate = AnimationUtils.loadAnimation(this, R.anim.arrow_rotate);//创建动画

        jbxx_text.setOnClickListener(jbxx_textOnClickListener);
        spjl_text.setOnClickListener(sqjlImageViewOnClickListener);
        workflowBtn.setOnClickListener(workflowBtnOnClickListener);


    }

    private View.OnClickListener jbxx_textOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (startAnaim()) {
                jbxxlinearlayout.setVisibility(View.GONE);
            } else {
                jbxxlinearlayout.setVisibility(View.VISIBLE);
            }

        }
    };
    /*
    审批记录
    */
    private View.OnClickListener sqjlImageViewOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(WppaydetailsActivity.this, WftransactionActivity.class);
            intent.putExtra("ownertable", GlobalConfig.PR_NAME);
            intent.putExtra("ownerid", JsonUnit.convertStrToArray(pr.getPRID())[0]);
            startActivityForResult(intent, 0);
        }
    };

    //启动动画
    private boolean startAnaim() {

        rotate.setInterpolator(new LinearInterpolator());//设置为线性旋转

        Log.e(TAG, "b=" + !rotate.getFillAfter());
        rotate.setFillAfter(!rotate.getFillAfter());//每次都取相反值，使得可以不恢复原位的旋转

        jbxx_text.startAnimation(rotate);
        return rotate.getFillAfter();
    }

    @Override
    protected String getSubTitle() {
        return getString(R.string.wpcgsqxq_text);
    }

    //审批
    private View.OnClickListener workflowBtnOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            PostStart(GlobalConfig.PR_NAME, JsonUnit.convertStrToArray(pr.getPRID())[0], GlobalConfig.WPPR_APPID, AccountUtils.getpersonId(WppaydetailsActivity.this));
        }
    };

    //流程启动
//http://10.60.12.98/maximo/mobile/wf/start?ownertable=GR&ownerid=77129&processname=GR-WZMAIN&userid=yanghongwei
    private void PostStart(String ownertable, String ownerid, String appid, String userid) {
        Log.e(TAG, "ownertable=" + ownertable + ",ownerid=" + ownerid + ",appid=" + appid + ",userid=" + userid);
        Rx2AndroidNetworking.post(GlobalConfig.HTTP_URL_START_WORKFLOW)
                .addBodyParameter("ownertable", ownertable)
                .addBodyParameter("ownerid", ownerid)
                .addBodyParameter("appid", appid)
                .addBodyParameter("userid", userid)
                .build().getStringObservable()
                .subscribeOn(Schedulers.io())        // 在io线程进行网络请求
                .observeOn(AndroidSchedulers.mainThread()) // 在主线程处理获取数据列表的请求结果
                .doOnNext(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String string) throws Exception {

                    }
                })


                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String s) throws Exception {
                        Log.i(TAG, "s=" + s);


                        R_WORKFLOW workflow = new Gson().fromJson(s, R_WORKFLOW.class);
                        if (workflow.getErrcode().equals(GlobalConfig.WORKFLOW_106)) {
                            R_APPROVE r_approve = new Gson().fromJson(s, R_APPROVE.class);
                            for (int i = 0; i < r_approve.getResult().size(); i++) {
                                R_APPROVE.Result result = r_approve.getResult().get(i);
                                Log.e(TAG, "instruction=" + result.getInstruction() + ",ispositive=" + result.getIspositive());
                            }

                            showDialog(r_approve.getResult());
                        } else {
                            showMiddleToast(WppaydetailsActivity.this, workflow.getErrmsg());
                        }
                    }


                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        showMiddleToast(WppaydetailsActivity.this, getString(R.string.spsb_text));
                    }
                });
    }


    //http://10.60.12.98/maximo/mobile/wf/approve?ownertable=GR&ownerid=77128&memo=驳回&selectWhat=0&userid=zhuyinan
    private void PostApprove(String ownertable, String ownerid, String memo, String selectWhat, String userid) {
        Log.e(TAG, "ownertable=" + ownertable + ",ownerid=" + ownerid + ",memo=" + memo + ",selectWhat=" + selectWhat + ",userid=" + userid);
        Rx2AndroidNetworking.post(GlobalConfig.HTTP_URL_APPROVE_WORKFLOW)
                .addBodyParameter("ownertable", ownertable)
                .addBodyParameter("ownerid", ownerid)
                .addBodyParameter("memo", memo)
                .addBodyParameter("selectWhat", selectWhat)
                .addBodyParameter("userid", userid)
                .build().getStringObservable()
                .subscribeOn(Schedulers.io())        // 在io线程进行网络请求
                .observeOn(AndroidSchedulers.mainThread()) // 在主线程处理获取数据列表的请求结果
                .doOnNext(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String string) throws Exception {

                    }
                })


                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String s) throws Exception {
                        Log.i(TAG, "审批s=" + s);
                        R_WORKFLOW workflow = new Gson().fromJson(s, R_WORKFLOW.class);
                        showMiddleToast(WppaydetailsActivity.this, workflow.getErrmsg());
                    }


                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        showMiddleToast(WppaydetailsActivity.this, getString(R.string.spsb_text));
                    }
                });
    }


    //弹出对话框
    public void showDialog(List<R_APPROVE.Result> results) {//

        ConfirmDialog.Builder dialog = new ConfirmDialog.Builder(this);
        dialog.setTitle("审批")
                .setData(results)
                .setPositiveButton("确定", new ConfirmDialog.Builder.cOnClickListener() {
                    @Override
                    public void cOnClickListener(DialogInterface dialogInterface, R_APPROVE.Result result, String memo) {
                        dialogInterface.dismiss();
                        Log.e(TAG, "result" + result.getInstruction());
                        PostApprove(GlobalConfig.PR_NAME, JsonUnit.convertStrToArray(pr.getPRID())[0], memo, result.getIspositive(), AccountUtils.getpersonId(WppaydetailsActivity.this));
                    }


                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).create().show();
    }


    //根据appid,grnum,objctname获取国内出差信息
    private void getNetWorkPR() {
        String data = HttpManager.getPRUrl(appid, ownernum, AccountUtils.getpersonId(this), 1, 10);
        Rx2AndroidNetworking.post(GlobalConfig.HTTP_URL_SEARCH)
                .addBodyParameter("data", data)
                .build()
                .getObjectObservable(R_PR.class) // 发起获取数据列表的请求，并解析到FootList
                .subscribeOn(Schedulers.io())        // 在io线程进行网络请求
                .observeOn(AndroidSchedulers.mainThread()) // 在主线程处理获取数据列表的请求结果
                .doOnNext(new Consumer<R_PR>() {
                    @Override
                    public void accept(@NonNull R_PR r_pr) throws Exception {
                    }
                })

                .map(new Function<R_PR, R_PR.ResultBean>() {
                    @Override
                    public R_PR.ResultBean apply(@NonNull R_PR r_pr) throws Exception {

                        return r_pr.getResult();
                    }
                })
                .map(new Function<R_PR.ResultBean, List<R_PR.PR>>() {
                    @Override
                    public List<R_PR.PR> apply(@NonNull R_PR.ResultBean resultBean) throws Exception {
                        return resultBean.getResultlist();
                    }

                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<PR>>() {
                    @Override
                    public void accept(@NonNull List<PR> prs) throws Exception {

                        if (prs == null || prs.isEmpty()) {
                        } else {
                            pr = prs.get(0);
                            showData();
                        }
                    }


                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                    }
                });
    }
}

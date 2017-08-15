package com.hqxh.fiamproperty.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hqxh.fiamproperty.R;
import com.hqxh.fiamproperty.base.BaseTitleActivity;
import com.hqxh.fiamproperty.bean.R_APPROVE;
import com.hqxh.fiamproperty.bean.R_WORKFLOW;
import com.hqxh.fiamproperty.constant.GlobalConfig;
import com.hqxh.fiamproperty.dateviews.cons.DPMode;
import com.hqxh.fiamproperty.model.R_CUDEPT.CUDEPT;
import com.hqxh.fiamproperty.model.R_Workorder.Workorder;
import com.hqxh.fiamproperty.ui.widget.ConfirmDialog;
import com.hqxh.fiamproperty.ui.widget.DatePicker;
import com.hqxh.fiamproperty.unit.AccountUtils;
import com.hqxh.fiamproperty.unit.DataUtils;
import com.hqxh.fiamproperty.unit.JsonUnit;
import com.rx2androidnetworking.Rx2AndroidNetworking;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 试验任务单
 **/
public class SyrwdWorkorderActivity extends BaseTitleActivity {

    private static final String TAG = "SyrwdWorkorderActivity";
    /**
     * 信息展示
     **/
    private TextView wonumText; //任务单
    private TextView udtodescText; //任务单描述
    private TextView udtotype2Text; //试验类型
    private TextView statusText; //状态
    private TextView projectidText; //费用号
    private TextView projectdescText; //项目名称
    private TextView pmText; //项目经理
    private TextView reportedbynameText; //接单人
    private TextView reportedbycudeptText; //部门
    private TextView reportedbycucrewText; //科室
    private TextView reportdateText; //提单日期
    private TextView phonenumText; //电话
    private TextView udtargstartdateText; //建议完成时间
    private TextView udtargcompdateText; //要求完成时间
    private TextView actfinishText; //任务完成时间


    private ImageView qtxxImageView;  //其它信息
    private LinearLayout qtLinearLayout;//其它布局

    private TextView supervisorText; //子项目经理
    private TextView cudeptText; //执行部门
    private TextView cucrewText; //执行科室
    private TextView ownernameText; //执行人
    private TextView udtoquestionText; //问题描述
    private TextView udremark1Text; //试验目的
    private TextView udremark2Text; //样品名称
    private TextView udqty1Text; //样品数量
    private TextView udremark3Text; //试验.项目.标准方法
    private TextView udremark4Text; //车辆及总成基本信息

    private ImageView sqjlImageView; //审批记录

    private Button workflowBtn;

    private Workorder workorder;

    private Animation rotate;


    private String deptnum; //部门编码

    @Override
    protected void beforeInit() {
        super.beforeInit();
        workorder = (Workorder) getIntent().getSerializableExtra("workorder");
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_syrwd_workorder;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        wonumText = (TextView) findViewById(R.id.wonum_text_id);
        udtodescText = (TextView) findViewById(R.id.udtodesc_text_id);
        udtotype2Text = (TextView) findViewById(R.id.udtotype2_text_id);
        statusText = (TextView) findViewById(R.id.status_text_id);
        projectidText = (TextView) findViewById(R.id.projectid_text_id);
        projectdescText = (TextView) findViewById(R.id.projectdesc_text_id);
        pmText = (TextView) findViewById(R.id.pm_text_id);
        reportedbynameText = (TextView) findViewById(R.id.reportedbyname_text_id);
        reportedbycudeptText = (TextView) findViewById(R.id.deptnum_text_id);
        reportedbycucrewText = (TextView) findViewById(R.id.reportedbycucrew_text_id);
        reportdateText = (TextView) findViewById(R.id.reportdate_text_id);
        phonenumText = (TextView) findViewById(R.id.phonenum_text_id);
        udtargstartdateText = (TextView) findViewById(R.id.udtargstartdate_text_id);
        udtargcompdateText = (TextView) findViewById(R.id.udtargcompdate_text_id);
        actfinishText = (TextView) findViewById(R.id.actfinish_text_id);

        qtxxImageView = (ImageView) findViewById(R.id.jbxx_kz_imageview_id);
        qtLinearLayout = (LinearLayout) findViewById(R.id.linearLayout_id);

        supervisorText = (TextView) findViewById(R.id.supervisor_text_id);
        cudeptText = (TextView) findViewById(R.id.cudept_text_id);
        cucrewText = (TextView) findViewById(R.id.cucrew_text_id);
        ownernameText = (TextView) findViewById(R.id.ownername_text_id);
        udtoquestionText = (TextView) findViewById(R.id.udtoquestion_text_id);
        udremark1Text = (TextView) findViewById(R.id.udremark1_text_id);
        udremark2Text = (TextView) findViewById(R.id.udremark2_text_id);
        udqty1Text = (TextView) findViewById(R.id.udqty1_text_id);
        udremark3Text = (TextView) findViewById(R.id.udremark3_text_id);
        udremark4Text = (TextView) findViewById(R.id.udremark4_text_id);


        sqjlImageView = (ImageView) findViewById(R.id.sqjl_imageview_id);

        workflowBtn = (Button) findViewById(R.id.workflow_btn_id);

        showData();
    }

    //展示界面数据
    private void showData() {
        wonumText.setText(JsonUnit.convertStrToArray(workorder.getWONUM())[0] + "," + JsonUnit.convertStrToArray(workorder.getDESCRIPTION())[0]);
        udtodescText.setText(JsonUnit.convertStrToArray(workorder.getUDTODESC())[0]);
        udtotype2Text.setText(JsonUnit.convertStrToArray(workorder.getUDTOTYPE2())[0]);
        statusText.setText(JsonUnit.convertStrToArray(workorder.getSTATUSDESC())[0]);
        projectidText.setText(JsonUnit.convertStrToArray(workorder.getPROJECTID())[0]);
        projectdescText.setText(JsonUnit.convertStrToArray(workorder.getPROJECTDESC())[0]);
        pmText.setText(JsonUnit.convertStrToArray(workorder.getPM())[0]);
        reportedbynameText.setText(JsonUnit.convertStrToArray(workorder.getREPORTEDBYNAME())[0]);
        reportedbycudeptText.setText(JsonUnit.convertStrToArray(workorder.getREPORTEDBYCUDEPT())[0]);
        reportedbycucrewText.setText((JsonUnit.convertStrToArray(workorder.getREPORTEDBYCUCREW())[0]));
        reportdateText.setText(JsonUnit.convertStrToArray(workorder.getREPORTDATE())[0]);
        phonenumText.setText(JsonUnit.convertStrToArray(workorder.getPHONENUM())[0]);

        udtargstartdateText.setText(JsonUnit.strToDateString(JsonUnit.convertStrToArray(workorder.getUDTARGSTARTDATE())[0]));
        udtargcompdateText.setText(JsonUnit.strToDateString(JsonUnit.convertStrToArray(workorder.getUDTARGCOMPDATE())[0]));
        actfinishText.setText(JsonUnit.strToDateString(JsonUnit.convertStrToArray(workorder.getACTFINISH())[0]));

        supervisorText.setText(JsonUnit.convertStrToArray(workorder.getSUPERVISOR())[0]);
        cudeptText.setText(JsonUnit.convertStrToArray(workorder.getCUDEPT())[0]);
        cucrewText.setText(JsonUnit.convertStrToArray(workorder.getCUCREW())[0]);
        ownernameText.setText(JsonUnit.convertStrToArray(workorder.getOWNERNAME())[0]);
        udtoquestionText.setText(JsonUnit.convertStrToArray(workorder.getUDTOQUESTION())[0]);
        udremark1Text.setText(JsonUnit.convertStrToArray(workorder.getUDREMARK1())[0]);
        udremark2Text.setText(JsonUnit.convertStrToArray(workorder.getUDREMARK2())[0]);
        udqty1Text.setText(JsonUnit.convertStrToArray(workorder.getUDQTY1())[0]);
        udremark3Text.setText(JsonUnit.convertStrToArray(workorder.getUDREMARK3())[0]);
        udremark4Text.setText(JsonUnit.convertStrToArray(workorder.getUDREMARK4())[0]);
        deptnum = JsonUnit.convertStrToArray(workorder.getDEPTNUM())[0];
        rotate = AnimationUtils.loadAnimation(this, R.anim.arrow_rotate);//创建动画

        qtxxImageView.setOnClickListener(jbxxImageViewOnClickListener);
        sqjlImageView.setOnClickListener(sqjlImageViewOnClickListener);
        workflowBtn.setOnClickListener(workflowBtnOnClickListener);

        onClickListener();

    }

    //设置事件监听
    private void onClickListener() {
        udtargcompdateText.setOnClickListener(udtargcompdateTextOnClickListener);
        cudeptText.setOnClickListener(cudeptTextOnClickListener);
        cucrewText.setOnClickListener(cucrewTextOnClickListener);
    }

    //日期选择
    private View.OnClickListener udtargcompdateTextOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final AlertDialog dialog = new AlertDialog.Builder(SyrwdWorkorderActivity.this).create();
            dialog.show();
            DatePicker picker = new DatePicker(SyrwdWorkorderActivity.this);
            picker.setDate(DataUtils.getYear(), DataUtils.getMonths());
            picker.setMode(DPMode.SINGLE);
            picker.setFestivalDisplay(false);
            picker.setOnDatePickedListener(new DatePicker.OnDatePickedListener() {
                @Override
                public void onDatePicked(String date) {
                    udtargcompdateText.setText(date);
                    dialog.dismiss();
                }
            });
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setContentView(picker, params);
            dialog.getWindow().setGravity(Gravity.CENTER);
        }
    };

    //执行部门
    private View.OnClickListener cudeptTextOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(SyrwdWorkorderActivity.this, CudeptActivity.class);
            intent.putExtra("title", getResources().getString(R.string.cucrew_text));
            intent.putExtra("appid", GlobalConfig.USER_APPID);
            startActivityForResult(intent, GlobalConfig.CUDEPT_REQUESTCODE);

        }
    };

    //执行科室
    private View.OnClickListener cucrewTextOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (null == deptnum) {
                showMiddleToast(SyrwdWorkorderActivity.this, "请选择执行部门");
            } else {
                Intent intent = new Intent(SyrwdWorkorderActivity.this, CudeptActivity.class);
                intent.putExtra("title", getResources().getString(R.string.cucrew_text));
                intent.putExtra("appid", GlobalConfig.ROLE_APPID);
                intent.putExtra("deptnum", deptnum);
                startActivityForResult(intent, GlobalConfig.CUDEPTKS_REQUESTCODE);
            }

        }
    };


    @Override
    protected String getSubTitle() {
        return getString(R.string.syrwdxq_text);
    }


    //基本信息
    private View.OnClickListener jbxxImageViewOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (startAnaim()) {
                qtLinearLayout.setVisibility(View.GONE);
            } else {
                qtLinearLayout.setVisibility(View.VISIBLE);
            }

        }
    };


    //启动动画
    private boolean startAnaim() {

        rotate.setInterpolator(new LinearInterpolator());//设置为线性旋转

        rotate.setFillAfter(!rotate.getFillAfter());//每次都取相反值，使得可以不恢复原位的旋转

        qtxxImageView.startAnimation(rotate);
        return rotate.getFillAfter();
    }

    //审批记录
    private View.OnClickListener sqjlImageViewOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(SyrwdWorkorderActivity.this, WftransactionActivity.class);
            intent.putExtra("ownertable", GlobalConfig.WORKORDER_NAME);
            intent.putExtra("ownerid", JsonUnit.convertStrToArray(workorder.getWORKORDERID())[0]);
            startActivityForResult(intent, 0);
        }
    };


    //审批
    private View.OnClickListener workflowBtnOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            PostStart(GlobalConfig.WORKORDER_NAME, JsonUnit.convertStrToArray(workorder.getWORKORDERID())[0], GlobalConfig.TRAVELS_APPID, AccountUtils.getpersonId(SyrwdWorkorderActivity.this));
        }
    };

    //流程启动
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

                        R_WORKFLOW workflow = new Gson().fromJson(s, R_WORKFLOW.class);
                        if (workflow.getErrcode().equals(GlobalConfig.WORKFLOW_106)) {
                            R_APPROVE r_approve = new Gson().fromJson(s, R_APPROVE.class);
                            for (int i = 0; i < r_approve.getResult().size(); i++) {
                                R_APPROVE.Result result = r_approve.getResult().get(i);
                                Log.e(TAG, "instruction=" + result.getInstruction() + ",ispositive=" + result.getIspositive());
                            }

                            showDialog(r_approve.getResult());
                        } else {
                            showMiddleToast(SyrwdWorkorderActivity.this, workflow.getErrmsg());
                        }
                    }


                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        showMiddleToast(SyrwdWorkorderActivity.this, getString(R.string.spsb_text));
                    }
                });
    }


    private void PostApprove(String ownertable, String ownerid, String memo, String selectWhat, String userid) {
        Log.e(TAG, "ownertable=" + ownertable + ",ownerid=" + ownerid + ",memo=" + memo + ",selectWhat=" + selectWhat + ",userid=" + userid);

        Rx2AndroidNetworking
                .post(GlobalConfig.HTTP_URL_APPROVE_WORKFLOW)
                .addBodyParameter("ownertable", ownertable)
                .addBodyParameter("ownerid", ownerid)
                .addBodyParameter("memo", memo)
                .addBodyParameter("selectWhat", selectWhat)
                .addBodyParameter("userid", userid)
                .build()
                .getStringObservable()
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
                        R_WORKFLOW workflow = new Gson().fromJson(s, R_WORKFLOW.class);
                        showMiddleToast(SyrwdWorkorderActivity.this, workflow.getErrmsg());
                    }


                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        showMiddleToast(SyrwdWorkorderActivity.this, getString(R.string.spsb_text));
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
                        PostApprove(GlobalConfig.WORKORDER_NAME, JsonUnit.convertStrToArray(workorder.getWORKORDERID())[0], memo, result.getIspositive(), AccountUtils.getpersonId(SyrwdWorkorderActivity.this));
                    }


                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).create().show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case GlobalConfig.CUDEPT_REQUESTCODE: //部门
                if (resultCode == GlobalConfig.CUDEPT_REQUESTCODE) {
                    CUDEPT cudept = (CUDEPT) data.getSerializableExtra("cudept");
                    cudeptText.setText(JsonUnit.convertStrToArray(cudept.getDESCRIPTION())[0]);
                    deptnum = JsonUnit.convertStrToArray(cudept.getDEPTNUM())[0];
                }
                break;
            case GlobalConfig.CUDEPTKS_REQUESTCODE: //科室
                if (resultCode == GlobalConfig.CUDEPT_REQUESTCODE) {

                    CUDEPT cudept1 = (CUDEPT) data.getSerializableExtra("cudept");
                    cucrewText.setText(JsonUnit.convertStrToArray(cudept1.getDESCRIPTION())[0]);
                }
                break;
        }
    }

}

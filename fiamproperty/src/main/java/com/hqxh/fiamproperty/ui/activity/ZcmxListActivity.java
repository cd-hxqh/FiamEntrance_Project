package com.hqxh.fiamproperty.ui.activity;

import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.hqxh.fiamproperty.R;
import com.hqxh.fiamproperty.api.HttpManager;
import com.hqxh.fiamproperty.base.BaseListActivity;
import com.hqxh.fiamproperty.constant.GlobalConfig;
import com.hqxh.fiamproperty.model.R_GRLINE;
import com.hqxh.fiamproperty.model.R_GRLINE.ResultBean;
import com.hqxh.fiamproperty.model.R_GRLINE.GRLINE;
import com.hqxh.fiamproperty.ui.adapter.BaseQuickAdapter;
import com.hqxh.fiamproperty.ui.adapter.WfassignmentAdapter;
import com.hqxh.fiamproperty.ui.adapter.WzmxAdapter;
import com.hqxh.fiamproperty.ui.adapter.ZcmxAdapter;
import com.hqxh.fiamproperty.unit.AccountUtils;
import com.hqxh.fiamproperty.unit.JsonUnit;
import com.rx2androidnetworking.Rx2AndroidNetworking;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 整车明细
 **/
public class ZcmxListActivity extends BaseListActivity {
    private static final String TAG = "ZcmxListActivity";



    private ZcmxAdapter zcmxadapter;

    private int curpage = 1;
    private int showcount = 20;
    private int totalpage;

    private String grnum;;

    @Override
    protected String getSubTitle() {

        return getString(R.string.zcmx_text);
    }


    /**
     * 获取数据
     **/
    private void getData() {
        String data= HttpManager.getZCMXUrl(AccountUtils.getpersonId(this),grnum, curpage, showcount);
        Log.e(TAG,"data="+data);
        Rx2AndroidNetworking.post(GlobalConfig.HTTP_URL_SEARCH)
                .addQueryParameter("data", data)
                .build()
                .getObjectObservable(R_GRLINE.class) // 发起获取数据列表的请求，并解析到R_Wfassignemt
                .subscribeOn(Schedulers.io())        // 在io线程进行网络请求
                .observeOn(AndroidSchedulers.mainThread()) // 在主线程处理获取数据列表的请求结果
                .doOnNext(new Consumer<R_GRLINE>() {
                    @Override
                    public void accept(@NonNull R_GRLINE r_grline) throws Exception {
                    }
                })

                .map(new Function<R_GRLINE, ResultBean>() {
                    @Override
                    public ResultBean apply(@NonNull R_GRLINE r_grline) throws Exception {

                        return r_grline.getResult();
                    }
                })
                .map(new Function<ResultBean, List<GRLINE>>() {
                    @Override
                    public List<GRLINE> apply(@NonNull ResultBean resultBean) throws Exception {
                        return resultBean.getResultlist();
                    }

                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<GRLINE>>() {
                    @Override
                    public void accept(@NonNull List<GRLINE> grline) throws Exception {
                        mPullLoadMoreRecyclerView.setRefreshing(false);
                        mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();

                        if (grline == null || grline.isEmpty()) {

                        } else {

                            addData(grline);


                        }
                    }


                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {

                        mPullLoadMoreRecyclerView.setRefreshing(false);
                    }
                });
    }


    @Override
    public void onRefresh() {
        curpage = 1;
        zcmxadapter.removeAll(zcmxadapter.getData());
        getData();

    }

    @Override
    public void onLoadMore() {
        if (totalpage == curpage) {
            getLoadMore();
            showMiddleToast(ZcmxListActivity.this,getResources().getString(R.string.all_data_hint));
        } else {
            curpage++;
            getData();
        }


    }


    private void getLoadMore() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();
                    }
                });

            }
        }, 1000);

    }


    @Override
    protected void fillData() {
        grnum=getIntent().getExtras().getString("grnum");
        initAdapter(new ArrayList<GRLINE>());
        getData();

    }

    @Override
    protected void setOnClick() {

    }


    /**
     * 获取数据*
     */
    private void initAdapter(final List<GRLINE> list) {
        zcmxadapter = new ZcmxAdapter(ZcmxListActivity.this, R.layout.list_item_zcmx, list);
        mRecyclerView.setAdapter(zcmxadapter);
        zcmxadapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        });
    }

    /**
     * 添加数据*
     */
    private void addData(final List<GRLINE> list) {
        zcmxadapter.addData(list);
    }




}

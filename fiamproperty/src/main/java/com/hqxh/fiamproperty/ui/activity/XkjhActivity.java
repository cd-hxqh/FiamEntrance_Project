package com.hqxh.fiamproperty.ui.activity;

import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.hqxh.fiamproperty.R;
import com.hqxh.fiamproperty.api.HttpManager;
import com.hqxh.fiamproperty.base.BaseListActivity;
import com.hqxh.fiamproperty.constant.GlobalConfig;
import com.hqxh.fiamproperty.model.R_PAYPLAN;
import com.hqxh.fiamproperty.model.R_PAYPLAN.PAYPLAN;
import com.hqxh.fiamproperty.model.R_PAYPLAN.ResultBean;
import com.hqxh.fiamproperty.ui.adapter.BaseQuickAdapter;
import com.hqxh.fiamproperty.ui.adapter.FkAdapter;
import com.hqxh.fiamproperty.ui.adapter.XkjhAdapter;
import com.hqxh.fiamproperty.unit.AccountUtils;
import com.rx2androidnetworking.Rx2AndroidNetworking;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 需款计划的Activity
 **/
public class XkjhActivity extends BaseListActivity {
    private static final String TAG = "XkjhActivity";


    private XkjhAdapter xkjhAdapter;

    private int curpage = 1;
    private int showcount = 20;
    private int totalpage;


    @Override
    protected String getSubTitle() {

        return getString(R.string.xkjh_text);
    }


    /**
     * 获取数据
     **/
    private void getData() {
        String data = HttpManager.getPAYPLANUrl(AccountUtils.getpersonId(this),  curpage, showcount);
        Log.i(TAG, "data=" + data);
        Log.i(TAG, "url=" + GlobalConfig.HTTP_URL_SEARCH);
        Rx2AndroidNetworking.post(GlobalConfig.HTTP_URL_SEARCH)
                .addQueryParameter("data", data)
                .build()
                .getObjectObservable(R_PAYPLAN.class) // 发起获取数据列表的请求，并解析到FootList
                .subscribeOn(Schedulers.io())        // 在io线程进行网络请求
                .observeOn(AndroidSchedulers.mainThread()) // 在主线程处理获取数据列表的请求结果
                .doOnNext(new Consumer<R_PAYPLAN>() {
                    @Override
                    public void accept(@NonNull R_PAYPLAN r_payplan) throws Exception {
                    }
                })

                .map(new Function<R_PAYPLAN, ResultBean>() {
                    @Override
                    public ResultBean apply(@NonNull R_PAYPLAN r_payplan) throws Exception {

                        return r_payplan.getResult();
                    }
                })
                .map(new Function<ResultBean, List<PAYPLAN>>() {
                    @Override
                    public List<PAYPLAN> apply(@NonNull ResultBean resultBean) throws Exception {
                        totalpage = Integer.valueOf(resultBean.getTotalpage());
                        Log.e(TAG, "Totalresult=" + resultBean.getTotalresult());
                        return resultBean.getResultlist();
                    }

                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<PAYPLAN>>() {
                    @Override
                    public void accept(@NonNull List<PAYPLAN> payplan) throws Exception {
                        mPullLoadMoreRecyclerView.setRefreshing(false);
                        mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();

                        if (payplan == null || payplan.isEmpty()) {

                        } else {

                            addData(payplan);


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
        xkjhAdapter.removeAll(xkjhAdapter.getData());
        getData();

    }

    @Override
    public void onLoadMore() {
        if (totalpage == curpage) {
            getLoadMore();
            showMiddleToast(XkjhActivity.this, getResources().getString(R.string.all_data_hint));
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
        initAdapter(new ArrayList<PAYPLAN>());
        getData();

    }


    /**
     * 获取数据*
     */
    private void initAdapter(final List<PAYPLAN> list) {
        xkjhAdapter = new XkjhAdapter(XkjhActivity.this, R.layout.list_item_travel, list);
        mRecyclerView.setAdapter(xkjhAdapter);
        xkjhAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        });
    }

    /**
     * 添加数据*
     */
    private void addData(final List<PAYPLAN> list) {
        xkjhAdapter.addData(list);
    }


}

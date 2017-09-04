package com.hqxh.fiamproperty.ui.activity;

import android.os.Handler;
import android.view.View;

import com.hqxh.fiamproperty.R;
import com.hqxh.fiamproperty.api.HttpManager;
import com.hqxh.fiamproperty.base.BaseListActivity;
import com.hqxh.fiamproperty.constant.GlobalConfig;
import com.hqxh.fiamproperty.model.R_PRLINE;
import com.hqxh.fiamproperty.model.R_PRLINE.PRLINE;
import com.hqxh.fiamproperty.model.R_PRLINE.ResultBean;
import com.hqxh.fiamproperty.ui.adapter.PrlineAdapter;
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
 * 试制采购申请-申请明细
 **/
public class PrlineActivity extends BaseListActivity {
    private static final String TAG = "PrlineActivity";


    private PrlineAdapter prlineadapter;

    private int curpage = 1;
    private int showcount = 20;
    private int totalpage;

    private String appid; //appid
    private String prnum; //prnum


    @Override
    protected void beforeInit() {
        super.beforeInit();
        if (getIntent().hasExtra("appid")) {
            appid = getIntent().getExtras().getString("appid");
        }
        if (getIntent().hasExtra("prnum")) {
            prnum = getIntent().getExtras().getString("prnum");
        }


    }

    @Override
    protected String getSubTitle() {

        return getResources().getString(R.string.sqmx_text);

    }


    /**
     * 获取数据
     **/
    private void getData() {
        String data = HttpManager.getPRLINEUrl(appid, prnum, AccountUtils.getpersonId(this), curpage, showcount);
        Rx2AndroidNetworking.post(GlobalConfig.HTTP_URL_SEARCH)
                .addBodyParameter("data", data)
                .build()
                .getObjectObservable(R_PRLINE.class) // 发起获取数据列表的请求，并解析到FootList
                .subscribeOn(Schedulers.io())        // 在io线程进行网络请求
                .observeOn(AndroidSchedulers.mainThread()) // 在主线程处理获取数据列表的请求结果
                .doOnNext(new Consumer<R_PRLINE>() {
                    @Override
                    public void accept(@NonNull R_PRLINE r_prline) throws Exception {
                    }
                })

                .map(new Function<R_PRLINE, ResultBean>() {
                    @Override
                    public ResultBean apply(@NonNull R_PRLINE r_prline) throws Exception {

                        return r_prline.getResult();
                    }
                })
                .map(new Function<ResultBean, List<PRLINE>>() {
                    @Override
                    public List<PRLINE> apply(@NonNull ResultBean resultBean) throws Exception {
                        totalpage = Integer.valueOf(resultBean.getTotalpage());
                        return resultBean.getResultlist();
                    }

                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<PRLINE>>() {
                    @Override
                    public void accept(@NonNull List<PRLINE> prline) throws Exception {
                        mPullLoadMoreRecyclerView.setRefreshing(false);
                        mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();
                        if (prline == null || prline.isEmpty()) {
                            notLinearLayout.setVisibility(View.VISIBLE);
                        } else {

                            addData(prline);


                        }
                    }


                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        notLinearLayout.setVisibility(View.VISIBLE);
                        mPullLoadMoreRecyclerView.setRefreshing(false);
                    }
                });
    }


    @Override
    public void onRefresh() {
        curpage = 1;
        prlineadapter.removeAll(prlineadapter.getData());
        getData();

    }

    @Override
    public void onLoadMore() {
        if (totalpage == curpage) {
            getLoadMore();
            showMiddleToast(PrlineActivity.this, getResources().getString(R.string.all_data_hint));
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

        initAdapter(new ArrayList<PRLINE>());
        getData();

    }

    @Override
    protected void setOnClick() {
        searchText.setVisibility(View.GONE);

    }


    /**
     * 获取数据*
     */
    private void initAdapter(final List<PRLINE> list) {
        prlineadapter = new PrlineAdapter(PrlineActivity.this, R.layout.list_item_sz_sqmx, list);
        prlineadapter.setAppid(appid);
        mRecyclerView.setAdapter(prlineadapter);
    }

    /**
     * 添加数据*
     */
    private void addData(final List<PRLINE> list) {
        prlineadapter.addData(list);
    }


}

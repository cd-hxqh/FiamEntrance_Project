package com.hqxh.fiamproperty.ui.activity;import android.os.Handler;import android.util.Log;import android.view.View;import android.widget.ImageView;import com.hqxh.fiamproperty.R;import com.hqxh.fiamproperty.api.HttpManager;import com.hqxh.fiamproperty.base.BaseListActivity;import com.hqxh.fiamproperty.constant.GlobalConfig;import com.hqxh.fiamproperty.model.R_CONTRACTLINE;import com.hqxh.fiamproperty.model.R_CONTRACTLINE.CONTRACTLINE;import com.hqxh.fiamproperty.model.R_CONTRACTLINE.ResultBean;import com.hqxh.fiamproperty.ui.adapter.ContractlineAdapter;import com.hqxh.fiamproperty.ui.adapter.ContractpayAdapter;import com.hqxh.fiamproperty.unit.AccountUtils;import com.rx2androidnetworking.Rx2AndroidNetworking;import java.util.ArrayList;import java.util.List;import io.reactivex.android.schedulers.AndroidSchedulers;import io.reactivex.annotations.NonNull;import io.reactivex.functions.Consumer;import io.reactivex.functions.Function;import io.reactivex.schedulers.Schedulers;/** * 合同-合同行 **/public class ContractlineActivity extends BaseListActivity {    private static final String TAG = "ContractlineActivity";    private ContractlineAdapter contractlineAdapter;    private int curpage = 1;    private int showcount = 20;    private int totalpage;    private String contractnum; //    private String contractrev;//    private String title;    @Override    protected void beforeInit() {        super.beforeInit();        if (getIntent().hasExtra("contractnum")) {            contractnum = getIntent().getExtras().getString("contractnum");        }        if (getIntent().hasExtra("contractrev")) {            contractrev = getIntent().getExtras().getString("contractrev");        }        if (getIntent().hasExtra("title")) {            title = getIntent().getExtras().getString("title");        }    }    @Override    protected String getSubTitle() {        return title;    }    /**     * 获取数据     **/    private void getData() {        String data = HttpManager.getCONTRACTLINEURL(AccountUtils.getpersonId(this), contractnum,contractrev,curpage, showcount);        Log.i(TAG, "data=" + data);        Log.i(TAG, "url=" + GlobalConfig.HTTP_URL_SEARCH);        Rx2AndroidNetworking.post(GlobalConfig.HTTP_URL_SEARCH)                .addQueryParameter("data", data)                .build()                .getObjectObservable(R_CONTRACTLINE.class) // 发起获取数据列表的请求，并解析到FootList                .subscribeOn(Schedulers.io())        // 在io线程进行网络请求                .observeOn(AndroidSchedulers.mainThread()) // 在主线程处理获取数据列表的请求结果                .doOnNext(new Consumer<R_CONTRACTLINE>() {                    @Override                    public void accept(@NonNull R_CONTRACTLINE r_contractline) throws Exception {                    }                })                .map(new Function<R_CONTRACTLINE, ResultBean>() {                    @Override                    public ResultBean apply(@NonNull R_CONTRACTLINE r_contractline) throws Exception {                        return r_contractline.getResult();                    }                })                .map(new Function<ResultBean, List<CONTRACTLINE>>() {                    @Override                    public List<CONTRACTLINE> apply(@NonNull ResultBean resultBean) throws Exception {                        totalpage = Integer.valueOf(resultBean.getTotalpage());                        return resultBean.getResultlist();                    }                })                .subscribeOn(Schedulers.io())                .observeOn(AndroidSchedulers.mainThread())                .subscribe(new Consumer<List<CONTRACTLINE>>() {                    @Override                    public void accept(@NonNull List<CONTRACTLINE> contractline) throws Exception {                        mPullLoadMoreRecyclerView.setRefreshing(false);                        mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();                        if (contractline == null || contractline.isEmpty()) {                        } else {                            addData(contractline);                        }                    }                }, new Consumer<Throwable>() {                    @Override                    public void accept(@NonNull Throwable throwable) throws Exception {                        mPullLoadMoreRecyclerView.setRefreshing(false);                    }                });    }    @Override    public void onRefresh() {        curpage = 1;        contractlineAdapter.removeAll(contractlineAdapter.getData());        getData();    }    @Override    public void onLoadMore() {        if (totalpage == curpage) {            getLoadMore();            showMiddleToast(ContractlineActivity.this, getResources().getString(R.string.all_data_hint));        } else {            curpage++;            getData();        }    }    private void getLoadMore() {        new Handler().postDelayed(new Runnable() {            @Override            public void run() {                runOnUiThread(new Runnable() {                    @Override                    public void run() {                        mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();                    }                });            }        }, 1000);    }    @Override    protected void fillData() {        searchText.setVisibility(View.GONE);        initAdapter(new ArrayList<CONTRACTLINE>());        getData();    }    @Override    protected void setOnClick() {    }    /**     * 获取数据*     */    private void initAdapter(final List<CONTRACTLINE> list) {        contractlineAdapter = new ContractlineAdapter(ContractlineActivity.this, R.layout.list_item_contractline, list);        mRecyclerView.setAdapter(contractlineAdapter);    }    /**     * 添加数据*     */    private void addData(final List<CONTRACTLINE> list) {        contractlineAdapter.addData(list);    }}
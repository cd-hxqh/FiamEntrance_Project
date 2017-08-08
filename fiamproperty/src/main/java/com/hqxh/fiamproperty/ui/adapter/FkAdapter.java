package com.hqxh.fiamproperty.ui.adapter;

import android.animation.Animator;
import android.content.Context;

import com.hqxh.fiamproperty.R;
import com.hqxh.fiamproperty.model.R_PAYCHECK.PAYCHECK;
import com.hqxh.fiamproperty.ui.widget.BaseViewHolder;
import com.hqxh.fiamproperty.unit.JsonUnit;

import java.util.List;


/**
 * Created by apple on 15/10/26
 * 付款验收
 */
public class FkAdapter extends BaseQuickAdapter<PAYCHECK> {

    public FkAdapter(Context context, int layoutResId, List data) {
        super(context, layoutResId, data);
    }

    @Override
    protected void startAnim(Animator anim, int index) {
        super.startAnim(anim, index);
        if (index < 5)
            anim.setStartDelay(index * 150);
    }

    @Override
    protected void convert(BaseViewHolder helper, PAYCHECK item) {
        helper.setText(R.id.wonum_text_id, JsonUnit.convertStrToArray(item.getPAYCHECKNUM())[0]);
        helper.setText(R.id.description_text_id, JsonUnit.convertStrToArray(item.getDESCRIPTION())[0]);
        helper.setText(R.id.reportedby_text_id, "类型:"+JsonUnit.convertStrToArray(item.getCHECKTYPE())[0]);
        helper.setText(R.id.fincntrldesc_text_id, "状态:"+JsonUnit.convertStrToArray(item.getSTATUSDESC())[0]);
        switch (helper.getPosition()%2){
            case 0:
                helper.setBackgroundRes(R.id.wonum_text_id,R.drawable.design_0_point);
                break;
            case 1:
                helper.setBackgroundRes(R.id.wonum_text_id,R.drawable.design_1_point);
                break;

        }
    }





}

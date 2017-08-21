package cn.hqxh.fiam.mobile;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hqxh.fiamproperty.base.BaseActivity;
import com.hqxh.fiamproperty.constant.GlobalConfig;
import com.hqxh.fiamproperty.model.R_PERSONS;
import com.hqxh.fiamproperty.ui.activity.HomeActivity;
import com.hqxh.fiamproperty.unit.AccountUtils;
import com.rx2androidnetworking.Rx2AndroidNetworking;

import org.json.JSONObject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


public class LoginActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 0; // 请求码

    private AutoCompleteTextView mEmailView;

    private EditText mPasswordView;

    // 所需的全部权限
    static final String[] PERMISSIONS = new String[]{
            Manifest.permission.READ_PHONE_STATE,
//            Manifest.permission.CAMERA,
            Manifest.permission.INTERNET,
    };

    private PermissionsChecker mPermissionsChecker; // 权限检测器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPermissionsChecker = new PermissionsChecker(this);
        setContentView(R.layout.activity_login);
        initView();
    }


    private void initView() {

        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);

        mPasswordView = (EditText) findViewById(R.id.password);

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);

        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Login();
            }
        });

    }


    private void Login() {

        String imei = ((TelephonyManager) getSystemService(TELEPHONY_SERVICE))
                .getDeviceId();

        Log.e("TAG", "imei=" + imei + ",usename=" + mEmailView.getText().toString());
        Log.e("TAG", "HTTP_URL_LOGIN=" + GlobalConfig.HTTP_URL_LOGIN);

        Rx2AndroidNetworking.post(GlobalConfig.HTTP_URL_LOGIN)
                .addQueryParameter("username", mEmailView.getText().toString())
                .addQueryParameter("imei", imei)
                .build()
                .getObjectObservable(R_PERSONS.class) // 发起获取数据列表的请求，并解析到R_Person.class
                .subscribeOn(Schedulers.io())        // 在io线程进行网络请求
                .observeOn(AndroidSchedulers.mainThread()) // 在主线程处理获取数据列表的请求结果
                .doOnNext(new Consumer<R_PERSONS>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull R_PERSONS r_PERSON) throws Exception {
                        Toast.makeText(LoginActivity.this, r_PERSON.getErrmsg(), Toast.LENGTH_SHORT).show();
                        if (r_PERSON.getErrcode().equals(GlobalConfig.LOGINSUCCESS)) {//登录成功

                        } else if (r_PERSON.getErrcode().equals(GlobalConfig.CHANGEIMEI)) {//登录成功,检测到用户更换手机登录

                        } else if (r_PERSON.getErrcode().equals(GlobalConfig.USERNAMEERROR)) {//用户名密码错误
                            return;
                        } else {
                            return;
                        }
                    }
                })

                .map(new Function<R_PERSONS, String>() {
                    @Override
                    public String apply(@io.reactivex.annotations.NonNull R_PERSONS r_PERSON) throws Exception {
                        return r_PERSON.getResult();
                    }

                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull String persion) throws Exception {
                        Log.e("TAG", "persion" + persion);
                        if (null != persion) {
                            JSONObject object = new JSONObject(persion);
                            R_PERSONS.PERSION persion1 = new R_PERSONS.PERSION();
                            persion1.setDEFSITE(object.getString("DEFSITE"));
                            persion1.setDISPLAYNAME(object.getString("DISPLAYNAME"));
                            persion1.setEMAILADDRESS(object.getString("EMAILADDRESS"));
                            persion1.setMYAPPS(object.getString("MYAPPS"));
                            persion1.setPERSONID(object.getString("PERSONID"));
                            AccountUtils.setLoginDetails(LoginActivity.this, persion1);
                            Log.e("TAG", "咋不跳呢");
                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivityForResult(intent, 0);
                        }


                    }


                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        Log.e("TAG", "throwable=" + throwable.getMessage());
                        Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                    }
                });


    }


    @Override
    protected void onResume() {
        super.onResume();

        // 缺少权限时, 进入权限配置页面
        if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
            startPermissionsActivity();
        } else {
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            Handler x = new Handler();
        }
    }

    private void startPermissionsActivity() {
        PermissionsActivity.startActivityForResult(this, REQUEST_CODE, PERMISSIONS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 拒绝时, 关闭页面, 缺少主要权限, 无法运行
        if (requestCode == REQUEST_CODE && resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
            finish();
        }
    }


}

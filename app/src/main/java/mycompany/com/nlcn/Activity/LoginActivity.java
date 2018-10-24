package mycompany.com.nlcn.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import mycompany.com.nlcn.Constant;
import mycompany.com.nlcn.Data.ConnectServer;
import mycompany.com.nlcn.MainActivity;
import mycompany.com.nlcn.Model.Message;
import mycompany.com.nlcn.Model.ResLogin;
import mycompany.com.nlcn.R;
import mycompany.com.nlcn.utils.SharedPreferencesHandler;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText mEdtUserName;
    private EditText mEdtPassword;
    private Context mContext;
    private CheckBox mCBRememberMe;
    private AlertDialog mAlertDialog;
    private Snackbar mSnackbar;

    private String mCookies = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mEdtUserName = (EditText) findViewById(R.id.user_search);
        mEdtPassword = (EditText) findViewById(R.id.pswd);
        mCBRememberMe = (CheckBox) findViewById(R.id.cb_remember);
        mContext = this.getApplicationContext();

        mCookies = SharedPreferencesHandler.getString(mContext,Constant.PREF_COOKIES);

        mEdtUserName.setText(SharedPreferencesHandler.getString(mContext, "username"));
        mEdtPassword.setText(SharedPreferencesHandler.getString(mContext, "password"));


    }

    public void login(final View v) {

        if (checkNullDangNhap()) {

            Call<ResLogin> call = ConnectServer.getInstance(mContext).getApi().signInAcc(mCookies,
                    mEdtUserName.getText().toString(),
                    mEdtPassword.getText().toString());

            call.enqueue(new Callback<ResLogin>() {
                @Override
                public void onResponse(Call<ResLogin> call, Response<ResLogin> response) {

                    try {
                        if (response.body().getSERVERRESPONSE() == 1) {

                            if (mCBRememberMe.isChecked()) {

                                SharedPreferencesHandler.writeString(mContext, "username", mEdtUserName.getText().toString());
                                SharedPreferencesHandler.writeString(mContext, "password", mEdtPassword.getText().toString());
                                SharedPreferencesHandler.writeBoolean(mContext, "remember_me", true);

                            }

//                        Toast.makeText(mContext, response.body().getSERVERMESSAGE(), Toast.LENGTH_SHORT).show();

                            Log.e("LOGIN", "onResponse: COOKIE: " + response.headers().get("Set-Cookie") );


                            SharedPreferencesHandler.writeString(mContext, Constant.PREF_COOKIES, response.headers().get("Set-Cookie"));

                            viewSucc(mCBRememberMe, "Đã đăng nhập thành công");
                            SharedPreferencesHandler.writeString(mContext, "id", response.body().getID());
                            Intent i = new Intent(mContext, MainActivity.class);
                            startActivity(i);
                            finish();
                        } else if (response.body().getSERVERRESPONSE() == 0) {

                            viewError(response.body().getSERVERMESSAGE());

                            v.setEnabled(true);
                        }
                    } catch (NullPointerException e) {
                        Log.e("TAG", "onResponse: " + "response null");
                    }

                }

                @Override
                public void onFailure(Call<ResLogin> call, Throwable t) {
                    viewError("Lỗi không thể kết nối đến máy chủ !");
                }
            });
        }
    }

    public void register(View v) {
        startActivity(new Intent(this, RegisterActivity.class));
    }



    private void viewError(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cảnh báo");
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        mAlertDialog = builder.create();
        mAlertDialog.show();
    }

    private void viewSucc(View view, String message) {
        mSnackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);

        mSnackbar.setAction("Đi đến giỏ hàng", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                intent.putExtra("position", 1);
                startActivity(intent);
                finish();
            }
        });
        mSnackbar.show();
    }

    private boolean checkNullDangNhap() {
        if (mEdtUserName.getText().toString().equals("")) {
            mEdtUserName.setError("Chưa nhập tên đăng nhập");
            return false;
        } else if (mEdtPassword.getText().toString().equals("")) {
            mEdtPassword.setError("Chưa nhập mật khẩu");
            return false;
        } else return true;
    }

    private void kiemTraDangNhap() {


        ConnectServer.getInstance(getApplicationContext()).getApi().kiemTraTrangThaiDangNhap(mCookies).enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                Log.e("TAG", "onResponse: " + response.code());
                if (response.code() == 200) {
                    Intent i = new Intent(getBaseContext(), MainActivity.class);
                    startActivity(i);
                    viewSucc(mEdtUserName, "Đã đăng nhập");
                    finish();

                } else if (response.code() == 401) {
                    SharedPreferences.Editor memes = PreferenceManager.getDefaultSharedPreferences(mContext).edit();
                    memes.clear();
                    memes.apply();
                    viewError("Đã hết phiên đăng nhập");
                }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {

                Log.e("TAG", "onFailure: " + t.getMessage());
                viewSucc(mEdtUserName, "Không thể kết nối đến server!");

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            String messsage = bundle.getString("message", null);
            viewError(messsage);
        } else if (SharedPreferencesHandler.getBoolean(mContext, "remember_me")) {
            kiemTraDangNhap();
        }

    }


    @Override
    protected void onStop() {
        super.onStop();
//        if (mAlertDialog.isShowing()) mAlertDialog.dismiss();
//        if (mSnackbar.isShown()) mSnackbar.dismiss();
    }
}

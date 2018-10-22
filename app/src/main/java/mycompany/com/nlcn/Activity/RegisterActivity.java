package mycompany.com.nlcn.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import mycompany.com.nlcn.Data.ConnectServer;
import mycompany.com.nlcn.MainActivity;
import mycompany.com.nlcn.Model.Message;
import mycompany.com.nlcn.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private EditText mName, mUserName, mPassword, mConfirmPassword, mSDT, mDiaChi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
            setSupportActionBar(toolbar);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }

        mName = (EditText) findViewById(R.id.edt_name);
        mUserName = (EditText) findViewById(R.id.edt_username);
        mPassword = (EditText) findViewById(R.id.edt_password);
        mConfirmPassword = (EditText) findViewById(R.id.edt_confirmpassword);
        mSDT = (EditText) findViewById(R.id.edt_sdt);
        mDiaChi = (EditText) findViewById(R.id.edt_dia_chi);


    }

    public void register(View view) {
        if (checkEditText()) {
            Call<Message> call = ConnectServer.getInstance(this).CreateApi().registerAcc(mName.getText().toString(),
                    mUserName.getText().toString(),
                    mPassword.getText().toString(),
                    mSDT.getText().toString(),
                    mDiaChi.getText().toString());

            call.enqueue(new Callback<Message>() {
                @Override
                public void onResponse(Call<Message> call, Response<Message> response) {
                    Toast.makeText(getBaseContext(), "Dang ky thanh cong", Toast.LENGTH_LONG).show();

                    Log.e("TAG", "onResponse: " + response.headers().toString());
                    Log.e("TAG", "onResponse: " + response.body());

                    Intent i = new Intent(getBaseContext(), MainActivity.class);
                    startActivity(i);
                }

                @Override
                public void onFailure(Call<Message> call, Throwable t) {
                    Toast.makeText(getBaseContext(), "Loi ket noi Server", Toast.LENGTH_LONG).show();
                    Log.e("TAG", "onResponse: " + "Loi ket noi Server");
                }
            });
        }
    }

    private boolean checkEditText() {

        if (mName.getText().toString().equals("")) {
            mName.setError("Chua nhap ten");
        } else if (mUserName.getText().toString().equals("")) {
            mUserName.setError("Chua nhap Username");
        } else if (mSDT.getText().toString().equals("")) {
            mSDT.setError("Email chua nhap");
        } else if (mDiaChi.getText().toString().equals("")) {
            mDiaChi.setError("Địa chỉ chưa nhập");
        }        else if (mPassword.getText().toString().length() < 7) {
            mPassword.setError("Do dai mat khau phai tren 7 ky tu");
        } else if (!mPassword.getText().toString().equals(mConfirmPassword.getText().toString())) {
            mConfirmPassword.setError("Nhap lai mat khau chua dung");
        } else return true;

        return false;
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
package mycompany.com.nlcn.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import java.util.ArrayList;
import java.util.List;
import mycompany.com.nlcn.Adapter.SanPhamRecyclerViewAdapter;
import mycompany.com.nlcn.Constant;
import mycompany.com.nlcn.Data.ConnectServer;
import mycompany.com.nlcn.MainActivity;
import mycompany.com.nlcn.Model.DataSanPham;
import mycompany.com.nlcn.Model.ItemSanpham;
import mycompany.com.nlcn.Model.Message;
import mycompany.com.nlcn.R;
import mycompany.com.nlcn.utils.SharedPreferencesHandler;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TimKiemActivity extends AppCompatActivity implements SanPhamRecyclerViewAdapter.onScrollListener,
        SanPhamRecyclerViewAdapter.onClickListener{

    private RecyclerView mRecyclerView;
    private SanPhamRecyclerViewAdapter mSanPhamRecyclerViewAdapter;
    private List<ItemSanpham> mSanPhams = new ArrayList<>();
    private int mPageCurrent;
    private int mNumPage;
    private AlertDialog mAlertDialog;
    private String idNguoiDung = "";

    private String mCookies;
    private String mKeyWord;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tim_kiem);

        mCookies = SharedPreferencesHandler.getString(this, Constant.PREF_COOKIES);
        idNguoiDung = SharedPreferencesHandler.getString(this, "id");

        mRecyclerView = findViewById(R.id.recyclerview);

        mSanPhamRecyclerViewAdapter = new SanPhamRecyclerViewAdapter(this, mSanPhams);
        mSanPhamRecyclerViewAdapter.setOnScrollListener(this);
        mSanPhamRecyclerViewAdapter.setOnClickListener(this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mSanPhamRecyclerViewAdapter);


    }




    private void timKiem(String keyWord,int page) {

        ConnectServer.getInstance(this).getApi().timKiem(keyWord, page).enqueue(new Callback<DataSanPham>() {
            @Override
            public void onResponse(Call<DataSanPham> call, Response<DataSanPham> response) {
                if (null != response) {
                    mPageCurrent = Integer.parseInt(response.body().getPage());
                    mNumPage = response.body().getNumpages();

                    for (ItemSanpham sp : response.body().getItemSanphams()) {
                        if (sp.getId() != null) mSanPhams.add(sp);

                    }
                    mSanPhamRecyclerViewAdapter.notifyDataSetChanged();
                    Log.e("HOME_FRAG", "onResponse: Succ");
                }


            }

            @Override
            public void onFailure(Call<DataSanPham> call, Throwable t) {
                Log.e("HOME_FRAG", "onFailure: " + t.getMessage());
                viewError("Lỗi kết nối đến máy chủ!");
            }
        });

    }

    @Override
    public void onScroll(int position) {
//        Log.e("TAG", "onScroll: "+position +"_"+  mSanPhams.size() );
        if (mPageCurrent < mNumPage && (position + 3) == mSanPhams.size()) {
            timKiem(mKeyWord,mPageCurrent + 1);
        }
    }


    @Override
    public void onItemClick(int position, String idSanPham) {
        Intent intent = new Intent(this, ChiTietSPActivity.class);
        intent.putExtra("idSP", idSanPham);
        startActivity(intent);

    }

    @Override
    public void onItemAddClick(int position, String idSanPham) {
        themVaoGioHang(idSanPham);
    }

    private void themVaoGioHang(final String mIdSP) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_them_san_luong_sp, null);
        Button btnXacNhan = dialogView.findViewById(R.id.btn_xacnhan);
        Button btnHuy = dialogView.findViewById(R.id.btn_huy);
        final EditText edtSanLuong = dialogView.findViewById(R.id.edt_sanluongmua);

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAlertDialog.isShowing()) mAlertDialog.dismiss();
            }
        });

        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int sanLuongMua = Integer.parseInt(edtSanLuong.getText().toString());
                ConnectServer.getInstance(TimKiemActivity.this)
                        .getApi()
                        .themSPVaoGioHang(mCookies, mIdSP, idNguoiDung, sanLuongMua)
                        .enqueue(new Callback<Message>() {
                            @Override
                            public void onResponse(Call<Message> call, Response<Message> response) {
                                mAlertDialog.dismiss();
                                try {
                                    if (response.code() == 401) {
                                        Intent intent = new Intent(TimKiemActivity.this, LoginActivity.class);
                                        intent.putExtra("message", "Phiên làm việc hết hạn \n Vui lòng đăng nhập lại");
                                        startActivity(intent);
                                        finish();
                                    }
                                    if (response.code() == 200 && null != response.body()) {
                                        viewSucc(mRecyclerView, response.body().getMessage());
                                    }
                                    if (response.code() == 400 && null != response.errorBody()) {
                                        String err = "";
                                        err += response.errorBody().string();
                                        viewError(err);
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(Call<Message> call, Throwable t) {
                                mAlertDialog.dismiss();
                                viewError("Lỗi kết nối đến máy chủ!");
                            }
                        });
            }
        });
        dialogBuilder.setView(dialogView);
        dialogBuilder.setTitle("Đặt hàng");
        mAlertDialog = dialogBuilder.create();
        mAlertDialog.show();
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
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void viewSucc(View view, String message) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);

        snackbar.setAction("Đi đến giỏ hàng", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TimKiemActivity.this, MainActivity.class);
                intent.putExtra("position", 1);
                startActivity(intent);
                finish();
            }
        });
        snackbar.show();
    }

}

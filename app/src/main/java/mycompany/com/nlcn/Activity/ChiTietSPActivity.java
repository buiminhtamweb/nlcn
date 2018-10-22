package mycompany.com.nlcn.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import mycompany.com.nlcn.Constant;
import mycompany.com.nlcn.Data.ConnectServer;
import mycompany.com.nlcn.MainActivity;
import mycompany.com.nlcn.Model.ChiTietSanPham;
import mycompany.com.nlcn.Model.Message;
import mycompany.com.nlcn.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChiTietSPActivity extends AppCompatActivity {

    String idNguoiDung = "5bbc731072b68a180cd61ff2";
    private String mIdSP;
    private ImageView mImageView;
    private TextView mTenSanPham, mGiaSanPham, mSanLuongSP, mChiTietSP;
    private AlertDialog alertDialog = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_sp);

        Intent intent = getIntent();
        if (null != intent) {
            mIdSP = intent.getExtras().getString("idSP", "");
        } else finish();

        mImageView = (ImageView) findViewById(R.id.img_agri);
        mTenSanPham = (TextView) findViewById(R.id.tv_ten_ns);
        mGiaSanPham = (TextView) findViewById(R.id.tv_gia);
        mSanLuongSP = (TextView) findViewById(R.id.tv_sl_conlai);
        mChiTietSP = (TextView) findViewById(R.id.tv_nd_chitiet_ns);

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

        ConnectServer.getInstance(getApplicationContext()).CreateApi().getChiTietSanPham(mIdSP).enqueue(new Callback<ChiTietSanPham>() {
            @Override
            public void onResponse(Call<ChiTietSanPham> call, Response<ChiTietSanPham> response) {

                if (null != response.body()) {
                    ChiTietSanPham chiTietSanPham = response.body();
                    Picasso.get().load(Constant.URL_SERVER + chiTietSanPham.getImgurl()).fit().centerCrop().into(mImageView);
                    mTenSanPham.setText(chiTietSanPham.getTensp());
                    mGiaSanPham.setText("Giá: " + chiTietSanPham.getGiasp().toString());
                    mSanLuongSP.setText("Tồn kho: " + chiTietSanPham.getSanluong().toString());
                    mChiTietSP.setText(chiTietSanPham.getChitietsp().toString());
                }
            }

            @Override
            public void onFailure(Call<ChiTietSanPham> call, Throwable t) {
                finish();
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }


    //Sự kiện nút Thêm vào giỏ hàng
    public void themVaoGioHang(View view) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_them_san_luong_sp, null);
        Button btnXacNhan = dialogView.findViewById(R.id.btn_xacnhan);
        Button btnHuy = dialogView.findViewById(R.id.btn_huy);
        final EditText edtSanLuong = dialogView.findViewById(R.id.edt_sanluongmua);

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (alertDialog.isShowing()) alertDialog.dismiss();
            }
        });

        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int sanLuongMua = Integer.parseInt(edtSanLuong.getText().toString());
                ConnectServer.getInstance(getApplicationContext())
                        .CreateApi()
                        .themSPVaoGioHang(mIdSP, idNguoiDung, sanLuongMua)
                        .enqueue(new Callback<Message>() {
                            @Override
                            public void onResponse(Call<Message> call, Response<Message> response) {

                                if (null != response.body()) {
                                    if (response.body().getMessage().equals("successful")) {
                                        alertDialog.dismiss();
                                        viewSucc(mImageView, "Đã đặt hàng thành công");
                                    } else
                                        viewError(getCurrentFocus(), response.body().getMessage());
                                }
                            }

                            @Override
                            public void onFailure(Call<Message> call, Throwable t) {

                            }
                        });
            }
        });
        dialogBuilder.setView(dialogView);
        dialogBuilder.setTitle("Đặt hàng");
        alertDialog = dialogBuilder.create();
        alertDialog.show();
    }


    private void viewError(View view, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
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
                   Intent intent = new Intent(getBaseContext(), MainActivity.class);
                   intent.putExtra("position", 1);
                   startActivity(intent);
                   finish();
               }
           });
           snackbar.show();
    }
}

package mycompany.com.nlcn.Activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import mycompany.com.nlcn.Constant;
import mycompany.com.nlcn.Data.ConnectServer;
import mycompany.com.nlcn.Model.Message;
import mycompany.com.nlcn.Model.UserAcc;
import mycompany.com.nlcn.R;
import mycompany.com.nlcn.utils.SharedPreferencesHandler;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CaiDatTaiKhoanActivity extends AppCompatActivity {

    private final int HO_TEN = 1;
    private final int NGAY_SINH = 2;
    private final int GIOI_TINH = 3;
    private final int SDT = 4;
    private final int DIA_CHI = 5;
    private final int ANH_DAI_DIEN = 6;

    private CircleImageView mImgAnhDaiDien;
    private Button mBtnHoTen, mBtnDoiMK, mBtnNamSinh, mBtnGioiTinh, mBtnSDT, mBtnDiaChi;

    private String mIdNguoiDung;
    private String mCookies;

    private ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cai_dat_tai_khoan);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
            setSupportActionBar(toolbar);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }

        mImgAnhDaiDien = (CircleImageView) findViewById(R.id.img_anhdaidien);
        mBtnHoTen = (Button) findViewById(R.id.btn_td_hoten);
        mBtnDoiMK = (Button) findViewById(R.id.btn_td_matkhau);
        mBtnNamSinh = (Button) findViewById(R.id.btn_td_namsinh);
        mBtnGioiTinh = (Button) findViewById(R.id.btn_td_gioitinh);
        mBtnSDT = (Button) findViewById(R.id.btn_td_sdt);
        mBtnDiaChi = (Button) findViewById(R.id.btn_td_diachi);

        mCookies = SharedPreferencesHandler.getString(this, Constant.PREF_COOKIES);

        eventClick();
        loadData();


    }

    private void eventClick() {
        mImgAnhDaiDien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });

        mBtnHoTen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doiHoTen(mBtnHoTen.getText().toString());
            }
        });

        mBtnDoiMK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doiMatKhau();
            }
        });

        mBtnNamSinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doiNamSinh();
            }
        });

        mBtnGioiTinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doiGioiTinh(mBtnGioiTinh.getText().toString());
            }
        });

        mBtnSDT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doiSDT(mBtnSDT.getText().toString());
            }
        });

        mBtnDiaChi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doiDiaChi(mBtnDiaChi.getText().toString());
            }
        });
    }

    private void doiHoTen(String oldFullName) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();

        //Tham chieu layout
        final View dialogView = inflater.inflate(R.layout.dialog_doi_hoten, null);
        dialogBuilder.setView(dialogView);

        final EditText mEdtHoTen = (EditText) dialogView.findViewById(R.id.edt_hoten);
        mEdtHoTen.setText(oldFullName);

        dialogBuilder.setTitle("Đổi họ tên");
        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                updateUser(HO_TEN, mEdtHoTen.getText().toString());
            }
        });

        dialogBuilder.setNegativeButton("Cancel", null);
        AlertDialog b = dialogBuilder.create();
        b.show();

    }

    private void doiMatKhau() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();

        //Tham chieu layout
        final View dialogView = inflater.inflate(R.layout.dialog_doi_matkhau, null);
        dialogBuilder.setView(dialogView);

        final EditText mEdtMatKhauCu = (EditText) dialogView.findViewById(R.id.edt_mk_cu);
        final EditText mEdtMatKhau = (EditText) dialogView.findViewById(R.id.edt_mk);
        final EditText mEdtNhapLaiMK = (EditText) dialogView.findViewById(R.id.edt_nhaplaimk);

        dialogBuilder.setTitle("Đổi mật khẩu");
        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                mEdtMatKhau.getText().toString();
                if (TextUtils.equals(mEdtMatKhau.getText().toString(), mEdtNhapLaiMK.getText().toString())) {
                    changePassWd(mEdtMatKhauCu.getText().toString(), mEdtNhapLaiMK.getText().toString());
                } else mEdtNhapLaiMK.setError("Mật khẩu mới chưa khớp");
            }
        });
        dialogBuilder.setNegativeButton("Cancel", null);
        AlertDialog b = dialogBuilder.create();
        b.show();

    }

    private void doiNamSinh() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();

        //Tham chieu layout
        final View dialogView = inflater.inflate(R.layout.dialog_datepicker, null);
        dialogBuilder.setView(dialogView);

        final DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.date_picker);

        dialogBuilder.setTitle("Đổi năm sinh");
        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
//                mBtnNamSinh.setText(datePicker.getDayOfMonth() + "-" + datePicker.getMonth() + "-" + datePicker.getYear());
                updateUser(NGAY_SINH, datePicker.getDayOfMonth() + "/" + datePicker.getMonth() + "/" + datePicker.getYear());

            }
        });
        dialogBuilder.setNegativeButton("Cancel", null);
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    private void doiGioiTinh(String oldSex) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();

        //Tham chieu layout
        final View dialogView = inflater.inflate(R.layout.dialog_doi_gioitinh, null);
        dialogBuilder.setView(dialogView);

        String sex[] = {
                "Nam",
                "Nữ",
                "Khác"};
        final Spinner spinGioiTinh = (Spinner) dialogView.findViewById(R.id.spinner_gioitinh);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sex);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spinGioiTinh.setAdapter(adapter);

        for (int i = 0; i < 3; i++) {
            if (sex[i].equals(oldSex)) {
                spinGioiTinh.setSelection(i);
            }
        }

        dialogBuilder.setTitle("Giới tính");
        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                updateUser(GIOI_TINH, spinGioiTinh.getSelectedItem().toString());
            }
        });
        dialogBuilder.setNegativeButton("Cancel", null);
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    private void doiSDT(String oldTel) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();

        //Tham chieu layout
        final View dialogView = inflater.inflate(R.layout.dialog_doi_sdt, null);
        dialogBuilder.setView(dialogView);

        final EditText mEdtSDT = (EditText) dialogView.findViewById(R.id.edt_sdt);
        mEdtSDT.setText(oldTel);

        dialogBuilder.setTitle("Đổi Số điện thoại");
        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                updateUser(SDT, mEdtSDT.getText().toString());
            }
        });


        dialogBuilder.setNegativeButton("Cancel", null);
        AlertDialog b = dialogBuilder.create();
        b.show();

    }

    private void doiDiaChi(String oldAddress) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();

        //Tham chieu layout
        final View dialogView = inflater.inflate(R.layout.dialog_doi_diachi, null);
        dialogBuilder.setView(dialogView);

        final EditText mEdtAddress = (EditText) dialogView.findViewById(R.id.edt_diachi);
        mEdtAddress.setText(oldAddress);

        dialogBuilder.setTitle("Đổi địa chỉ");
        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                updateUser(DIA_CHI, mEdtAddress.getText().toString());
            }
        });


        dialogBuilder.setNegativeButton("Cancel", null);
        AlertDialog b = dialogBuilder.create();
        b.show();

    }

    //Data
    private void loadData() {
        //Lấy ID từ SharedPreferences
        mIdNguoiDung = SharedPreferencesHandler.getString(this, "id");

        //Lấy thông tin từ Server
        ConnectServer.getInstance(this).getApi().layThongTinNguoiDung(mCookies, mIdNguoiDung).enqueue(new Callback<UserAcc>() {
            @Override
            public void onResponse(Call<UserAcc> call, Response<UserAcc> response) {

                if (response.isSuccessful() && response.code() == 200) {
                    UserAcc userAcc = response.body();
                    Picasso.get().load(Constant.URL_SERVER + userAcc.getAvatar())
                            .error(R.drawable.add)
                            .fit()
                            .centerCrop()
                            .into(mImgAnhDaiDien);
                    mBtnHoTen.setText(userAcc.getName());
                    mBtnDiaChi.setText(userAcc.getDiachi());
                    mBtnSDT.setText(userAcc.getSdt());
                    if (userAcc.getNamsinh() != null)
                        mBtnNamSinh.setText(userAcc.getNamsinh());
                    if (userAcc.getGioitinh() != null)
                        mBtnGioiTinh.setText(userAcc.getGioitinh());
                }
                if (response.code() == 400) {
                    try {
                        viewErr(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<UserAcc> call, Throwable t) {
                viewErrorExitApp();
            }
        });


    }


    private void updateUser(final int type, final String data) {

        ConnectServer.getInstance(this).getApi().capNhatThongTinNguoiDung(mCookies, mIdNguoiDung, type, data).enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {

                if (response.code() == 401) {
                    Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                    intent.putExtra("message", "Phiên làm việc hết hạn \nVui lòng đăng nhập lại");
                    startActivity(intent);
                    finish();
                }

                if (response.isSuccessful() && response.code() == 200) {
                    viewSucc(mBtnDiaChi, response.body().getMessage());
                    switch (type) {
                        case HO_TEN:
                            mBtnHoTen.setText(data);
                            break;
                        case SDT:
                            mBtnSDT.setText(data);
                            break;
                        case DIA_CHI:
                            mBtnDiaChi.setText(data);
                            break;
                        case NGAY_SINH:
                            mBtnNamSinh.setText(data);
                            break;
                        case GIOI_TINH:
                            mBtnGioiTinh.setText(data);
                            break;
                    }


                }
                if (response.isSuccessful() && response.code() == 400) {
                    try {
                        viewErr(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (response.isSuccessful() && response.code() == 413) {
                    viewErr("Tập tin ảnh quá lớn");
                }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                viewErrorExitApp();

            }
        });


    }

    private void changePassWd(String oldPass, String newConfirmPass) {
        ConnectServer.getInstance(this).getApi().capNhatMatKhau(mCookies, mIdNguoiDung, oldPass, newConfirmPass).enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {

                if (response.code() == 401) {
                    Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                    intent.putExtra("message", "Phiên làm việc hết hạn \nVui lòng đăng nhập lại");
                    startActivity(intent);
                    finish();
                }

                if (response.isSuccessful() && response.code() == 200) {
                    viewSucc(mBtnDiaChi, response.body().getMessage());
                } else if (response.isSuccessful() && response.code() == 400) {
                    try {
                        viewErr(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                viewErr("Không thể kết nối đến máy chủ");
            }
        });

    }

    private void viewErr(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getBaseContext());
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
        snackbar.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void checkPermistion() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);
            }
        }
    }

    public void chooseImage() {
        checkPermistion();
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap bitmap = null;
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == this.RESULT_OK) {
            Uri uri = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                // bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                uploadImage(uri, bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Load image
//            Glide.with(MainActivity.this).load(uri).override(420, 594).centerCrop().into(imageView);
            //Toast.makeText(this, PATH, Toast.LENGTH_LONG).show();


        }
    }

    // Encode bitmap to String
    public String getBitMap(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    public void uploadImage(final Uri uri, final Bitmap bitmap) {

        viewProgressDialog("Đang upload ảnh ...");

        String imgCode = getBitMap(bitmap);
        ConnectServer.getInstance(this).getApi().capNhatThongTinNguoiDung(mCookies, mIdNguoiDung, ANH_DAI_DIEN, imgCode).enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {

                hideProgressDialog();
                if (response.code() == 200) {

                    Picasso.get().load(uri)
                            .error(R.drawable.add)
                            .fit()
                            .centerCrop()
                            .into(mImgAnhDaiDien);
                    viewSucc(mBtnDiaChi, "Đã cập nhật ảnh đại diện thành công");
                }
                if (response.code() == 400) {
                    viewErr("Lỗi !  Chưa cập nhập ảnh đại diện");
                }
                if (response.isSuccessful() && response.code() == 413) {
                    viewErr("Tập tin ảnh quá lớn");
                }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                Log.e("UPLOAD_IMG", t.getMessage());
                viewErrorExitApp();
            }
        });
    }

    private void viewProgressDialog(String message) {
        if (null == mProgressDialog) {
            mProgressDialog = new ProgressDialog(this);
        }
        mProgressDialog.setMessage(message);
        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (null != mProgressDialog) {
            mProgressDialog.dismiss();
        }
    }

    private void viewErrorExitApp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cảnh báo");
        builder.setMessage("Không thể kết nối đến máy chủ ! \nThoát ứng dụng.");
        builder.setCancelable(false);
        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                System.exit(1);
            }
        });
        AlertDialog mAlertDialog = builder.create();
        mAlertDialog.show();
    }
}

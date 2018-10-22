package mycompany.com.nlcn.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import mycompany.com.nlcn.Activity.CaiDatTaiKhoanActivity;
import mycompany.com.nlcn.Activity.DonHangDangXyLyActivity;
import mycompany.com.nlcn.Activity.LichSuDatHangActivity;
import mycompany.com.nlcn.Activity.LoginActivity;
import mycompany.com.nlcn.Constant;
import mycompany.com.nlcn.Data.ConnectServer;
import mycompany.com.nlcn.Model.Message;
import mycompany.com.nlcn.Model.UserAcc;
import mycompany.com.nlcn.R;
import mycompany.com.nlcn.utils.SharedPreferencesHandler;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserFrag extends Fragment {
    private String mIdNguoiDung = "";

    private CircleImageView mCircleImageView;
    private TextView mTvHoTen, mTvSDT, mTvDiaChi;
    private TextView mTvDonHangDangXuLy, mTvLichSuDatHang, mTvCaiDatTaiKhoan, mTvDangXuat, mTvPhanHoi;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_user, container, false);

        mIdNguoiDung = SharedPreferencesHandler.getString(getActivity(), "id");

        mCircleImageView = (CircleImageView) view.findViewById(R.id.circleImageView);
        mTvHoTen = (TextView) view.findViewById(R.id.tv_ho_ten);
        mTvSDT = (TextView) view.findViewById(R.id.tv_sdt);
        mTvDiaChi = (TextView)view.findViewById(R.id.tv_dia_chi);

        mTvDonHangDangXuLy = (TextView) view.findViewById(R.id.tv_don_hang_dag_xu_ly);
        mTvLichSuDatHang = (TextView) view.findViewById(R.id.tv_ls_dathang);
        mTvCaiDatTaiKhoan = (TextView) view.findViewById(R.id.tv_caidat);
        mTvDangXuat = (TextView) view.findViewById(R.id.tv_dangxuat);

        mTvPhanHoi = (TextView) view.findViewById(R.id.tv_phanhoi);

        setClickForView();
        layThongTinCaNhan();

        return view;
    }

    private void setClickForView(){
        mTvDonHangDangXuLy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), DonHangDangXyLyActivity.class);
                startActivity(intent);
            }
        });
        mTvLichSuDatHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LichSuDatHangActivity.class);
                startActivity(intent);
            }
        });

        mTvCaiDatTaiKhoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CaiDatTaiKhoanActivity.class);
                startActivity(intent);
            }
        });

        mTvDangXuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dangXuat();
            }
        });

        mTvPhanHoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(Intent.ACTION_VIEW)
                        .setType("plain/text")
                        .setData(Uri.parse("tamb1401088@student.etu.edu.vn"))
                        .setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail")
                        .putExtra(Intent.EXTRA_EMAIL, new String[]{"tamb1401088@student.etu.edu.vn"})
                        .putExtra(Intent.EXTRA_SUBJECT, "Phản hồi ứng dụng Niên luận chuyên ngành")
                        .putExtra(Intent.EXTRA_TEXT, "");
                startActivity(intent);
            }
        });
    }

    private void layThongTinCaNhan(){
        ConnectServer.getInstance(getActivity()).CreateApi().layThongTinNguoiDung(mIdNguoiDung).enqueue(new Callback<UserAcc>() {
            @Override
            public void onResponse(Call<UserAcc> call, @NonNull Response<UserAcc> response) {
                if(response.isSuccessful() && response.code() == 200){
                    UserAcc userAcc = response.body();
                    assert userAcc != null;
                    Picasso.get().load(Constant.URL_SERVER+userAcc.getAvatar()).centerCrop().fit().into(mCircleImageView);
                    mTvHoTen.setText(userAcc.getName());
                    mTvSDT.setText(userAcc.getSdt());
                    mTvDiaChi.setText(userAcc.getDiachi());
                }
            }

            @Override
            public void onFailure(Call<UserAcc> call, Throwable t) {

            }
        });
    }

    private void dangXuat() {
        ConnectServer.getInstance(getActivity()).CreateApi().dangXuat().enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                if (response.isSuccessful()) {
                    Snackbar.make(mCircleImageView, response.body().getMessage(), Snackbar.LENGTH_LONG).show();
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {

            }
        });
    }


}

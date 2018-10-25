package mycompany.com.nlcn.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import mycompany.com.nlcn.Adapter.DonHangRecyclerViewAdapter;
import mycompany.com.nlcn.Constant;
import mycompany.com.nlcn.Data.ConnectServer;
import mycompany.com.nlcn.Model.DSDonHang;
import mycompany.com.nlcn.Model.ItemDonhang;
import mycompany.com.nlcn.R;
import mycompany.com.nlcn.utils.SharedPreferencesHandler;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LichSuDatHangActivity extends AppCompatActivity implements DonHangRecyclerViewAdapter.onClickListener,
        DonHangRecyclerViewAdapter.onScrollListener {


    private RecyclerView mRecyclerView;
    private List<ItemDonhang> mDonHangs = new ArrayList<>();
    private DonHangRecyclerViewAdapter mDonHangRecyclerViewAdapter;

    private String mIDNguoiDung;
    private int mPageCurrent;
    private int mNumPage;

    private String mCookies;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lich_su_dat_hang);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mDonHangRecyclerViewAdapter = new DonHangRecyclerViewAdapter(mDonHangs);
        mDonHangRecyclerViewAdapter.setOnScrollListener(this);
        mDonHangRecyclerViewAdapter.setOnClickListener(this);
        mRecyclerView.setAdapter(mDonHangRecyclerViewAdapter);

        mCookies = SharedPreferencesHandler.getString(getBaseContext(), Constant.PREF_COOKIES);
        layDonHang(1);
    }

    private void layDonHang(int page) {
        mIDNguoiDung = SharedPreferencesHandler.getString(this, "id");
        ConnectServer.getInstance(this).getApi().layDSDonHang(mCookies, mIDNguoiDung, true, page).enqueue(new Callback<DSDonHang>() {
            @Override
            public void onResponse(Call<DSDonHang> call, @NonNull Response<DSDonHang> response) {
                if (response.isSuccessful() && response.code() == 200) {

                    if (response.code() == 401) {
                        Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                        intent.putExtra("message", "Phiên làm việc hết hạn \n Vui lòng đăng nhập lại");
                        startActivity(intent);
                        finish();
                    }

                    for (ItemDonhang item : response.body().getDonhangs()
                            ) {

                        Log.e("LS_DH", "onResponse: " + item.getId());
                        mDonHangs.add(item);

                    }

                    assert response.body() != null;
                    mPageCurrent = Integer.parseInt(response.body().getPage());
                    mNumPage = response.body().getNumpages();

                    mDonHangRecyclerViewAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<DSDonHang> call, Throwable t) {

            }
        });
    }


    @Override
    public void onItemClick(int position, String idDonHang) {

    }

    @Override
    public void onScroll(int position) {
        if (mPageCurrent < mNumPage && (position + 3) == mDonHangs.size()) {
            layDonHang(mPageCurrent + 1);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}

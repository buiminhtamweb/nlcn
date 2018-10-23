package mycompany.com.nlcn.Activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import mycompany.com.nlcn.Adapter.DonHangRecyclerViewAdapter;
import mycompany.com.nlcn.Data.ConnectServer;
import mycompany.com.nlcn.Model.DSDonHang;
import mycompany.com.nlcn.R;
import mycompany.com.nlcn.utils.SharedPreferencesHandler;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LichSuDatHangActivity extends AppCompatActivity implements DonHangRecyclerViewAdapter.onClickListener,
        DonHangRecyclerViewAdapter.onScrollListener {


    private RecyclerView mRecyclerView;
    private DSDonHang mDSDonHang;
    private DonHangRecyclerViewAdapter mDonHangRecyclerViewAdapter;

    private String mIDNguoiDung;
    private int mPageCurrent;
    private int mNumPage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lich_su_dat_hang);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mDonHangRecyclerViewAdapter = new DonHangRecyclerViewAdapter(mDSDonHang.getDonhangs());
        mRecyclerView.setAdapter(mDonHangRecyclerViewAdapter);


        layDonHang(1);
    }

    private void layDonHang(int page){
        mIDNguoiDung = SharedPreferencesHandler.getString(this, "id");
        ConnectServer.getInstance(this).CreateApi().layDSDonHang(mIDNguoiDung,false,1).enqueue(new Callback<DSDonHang>() {
            @Override
            public void onResponse(Call<DSDonHang> call, @NonNull Response<DSDonHang> response) {
                if (response.isSuccessful() && response.code()== 200){
                    mDSDonHang = response.body();
                    assert response.body() != null;
                    mPageCurrent =Integer.parseInt(response.body().getPage());
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
        if (mPageCurrent < mNumPage && (position + 3) == mDSDonHang.getDonhangs().size()) {
            layDonHang(mPageCurrent + 1);
        }
    }
}

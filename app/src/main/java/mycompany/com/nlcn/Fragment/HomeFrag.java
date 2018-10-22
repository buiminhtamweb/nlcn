package mycompany.com.nlcn.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import mycompany.com.nlcn.Activity.ChiTietSPActivity;
import mycompany.com.nlcn.Adapter.SanPhamRecyclerViewAdapter;
import mycompany.com.nlcn.Data.API;
import mycompany.com.nlcn.Data.ConnectServer;
import mycompany.com.nlcn.MainActivity;
import mycompany.com.nlcn.Model.DataSanPham;
import mycompany.com.nlcn.Model.Message;
import mycompany.com.nlcn.Model.ItemSanpham;
import mycompany.com.nlcn.R;
import mycompany.com.nlcn.utils.SharedPreferencesHandler;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFrag extends Fragment implements SanPhamRecyclerViewAdapter.onScrollListener, SanPhamRecyclerViewAdapter.onClickListener {
    private RecyclerView mRecyclerView;
    private SanPhamRecyclerViewAdapter mSanPhamRecyclerViewAdapter;
    private List<ItemSanpham> mSanPhams = new ArrayList<>();
    private int mPageCurrent;
    private int mNumPage;
    private API mApi;
    private AlertDialog mAlertDialog;
    private String idNguoiDung = "5bbc731072b68a180cd61ff2";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_home, container, false);

        idNguoiDung = SharedPreferencesHandler.getString(getActivity(),"id");

        mRecyclerView = v.findViewById(R.id.recyclerview);

        mSanPhamRecyclerViewAdapter = new SanPhamRecyclerViewAdapter(getContext(), mSanPhams);
        mSanPhamRecyclerViewAdapter.setOnScrollListener(this);
        mSanPhamRecyclerViewAdapter.setOnClickListener(this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mSanPhamRecyclerViewAdapter);

        mApi = ConnectServer.getInstance(getContext()).CreateApi();
        layDSSanPham(1);

        return v;
    }

    private void layDSSanPham(int page) {

        Call<DataSanPham> call = mApi.getDSSanPham(page);

        call.enqueue(new Callback<DataSanPham>() {
            @Override
            public void onResponse(Call<DataSanPham> call, Response<DataSanPham> response) {
                if (null != response) {
                    mPageCurrent = Integer.parseInt(response.body().getPage());
                    mNumPage = response.body().getNumpages();

                    for (ItemSanpham sp : response.body().getItemSanphams()) {
                        if (sp.getId() != null) mSanPhams.add(sp);

                    }
                    mSanPhamRecyclerViewAdapter.notifyDataSetChanged();
                    Log.e("HOME_FRAG", "onResponse: Succ" );
                }


            }

            @Override
            public void onFailure(Call<DataSanPham> call, Throwable t) {
                Log.e("HOME_FRAG", "onFailure: "+ t.getMessage() );
            }
        });

    }

    @Override
    public void onScroll(int position) {
//        Log.e("TAG", "onScroll: "+position +"_"+  mSanPhams.size() );
        if (mPageCurrent < mNumPage && (position + 3) == mSanPhams.size()) {
            layDSSanPham(mPageCurrent + 1);
        }
    }


    @Override
    public void onItemClick(int position, String idSanPham) {
        Intent intent = new Intent(getActivity(), ChiTietSPActivity.class);
        intent.putExtra("idSP", idSanPham);
        startActivity(intent);

    }

    @Override
    public void onItemAddClick(int position, String idSanPham) {
        themVaoGioHang(idSanPham);
    }


    private void themVaoGioHang(final String mIdSP) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
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
                ConnectServer.getInstance(getActivity())
                        .CreateApi()
                        .themSPVaoGioHang(mIdSP, idNguoiDung, sanLuongMua)
                        .enqueue(new Callback<Message>() {
                            @Override
                            public void onResponse(Call<Message> call, Response<Message> response) {

                                if (null != response.body()) {
                                    if (response.body().getMessage().equals("successful")) {
                                        mAlertDialog.dismiss();
                                        viewSucc(mRecyclerView, "Đã đặt hàng thành công");
                                    } else
                                        viewError(mRecyclerView, response.body().getMessage());
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
        mAlertDialog = dialogBuilder.create();
        mAlertDialog.show();
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
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra("position", 1);
                startActivity(intent);
                getActivity().finish();
            }
        });
        snackbar.show();
    }
}

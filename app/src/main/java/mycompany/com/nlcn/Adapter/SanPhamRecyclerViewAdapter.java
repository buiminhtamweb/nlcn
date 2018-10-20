package mycompany.com.nlcn.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import mycompany.com.nlcn.Constant;
import mycompany.com.nlcn.Model.Sanpham;
import mycompany.com.nlcn.R;

public class SanPhamRecyclerViewAdapter extends RecyclerView.Adapter<SanPhamRecyclerViewAdapter.Holder> {
    private Context mContext;
    private onClickListener onClickListener;
    private onScrollListener onScrollListener;
    private List<Sanpham> mSanPhams;


    public SanPhamRecyclerViewAdapter(Context mContext, List<Sanpham> mSanPhams) {
        this.mContext = mContext;
        this.mSanPhams = mSanPhams;
    }

    @NonNull
    @Override
    public SanPhamRecyclerViewAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_sanpham, parent, false);

        return new Holder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {


        Picasso.get().load(Constant.URL_SERVER + mSanPhams.get(position).getImgurl()).fit().centerCrop().into(holder.mImageView);

        holder.mNameAgri.setText(mSanPhams.get(position).getTensp());
        holder.mPrice.setText(mSanPhams.get(position).getGiasp() + " VND");
        holder.mSanLuong.setText(mSanPhams.get(position).getSanluong() + " Gam");

        onScrollListener.onScroll(position);
    }

    @Override
    public int getItemCount() {
        return mSanPhams.size();
    }

    public void setOnClickListener(SanPhamRecyclerViewAdapter.onClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setOnScrollListener(SanPhamRecyclerViewAdapter.onScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }

    public interface onClickListener {
        void onItemClick(int position, String idSanPham);

        void onItemAddClick(int position, String idSanPham);
    }

    public interface onScrollListener {
        void onScroll(int position);
    }

    class Holder extends RecyclerView.ViewHolder {

        ImageView mImageView;
        TextView mNameAgri;
        TextView mSanLuong;
        TextView mPrice;
        ImageButton imageButton;

        public Holder(View view) {
            super(view);
            mImageView = (ImageView) view.findViewById(R.id.imageView);
            mNameAgri = (TextView) view.findViewById(R.id.textView_tensp);
            mSanLuong = (TextView) view.findViewById(R.id.textView_giasp);
            mPrice = (TextView) view.findViewById(R.id.textView_sanluong);
            imageButton = (ImageButton) view.findViewById(R.id.imageButton);


            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickListener.onItemAddClick(getAdapterPosition(), mSanPhams.get(getAdapterPosition()).getId());
                }
            });

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.onItemClick(getAdapterPosition(), mSanPhams.get(getAdapterPosition()).getId());
                }
            });


        }
    }
}

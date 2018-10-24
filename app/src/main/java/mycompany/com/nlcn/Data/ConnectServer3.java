//package mycompany.com.nlcn.Data;
//
//import android.content.Context;
//import android.util.Log;
//
//import java.util.List;
//
//import mycompany.com.nlcn.Constant;
//import mycompany.com.nlcn.Model.ChiTietSanPham;
//import mycompany.com.nlcn.Model.DSDonHang;
//import mycompany.com.nlcn.Model.DataSanPham;
//import mycompany.com.nlcn.Model.DonHang;
//import mycompany.com.nlcn.Model.DonHangRes;
//import mycompany.com.nlcn.Model.ItemSPDonHang;
//import mycompany.com.nlcn.Model.Message;
//import mycompany.com.nlcn.Model.ResLogin;
//import mycompany.com.nlcn.Model.SPGioHang;
//import mycompany.com.nlcn.Model.UserAcc;
//import okhttp3.OkHttpClient;
//import retrofit2.Call;
//import retrofit2.Retrofit;
//import retrofit2.converter.gson.GsonConverterFactory;
//
//public class ConnectServer {
//
//    interface CallBack{
//        void viewSucc(String succ);
//        void ViewErr(String err);
//    }
//
//    private static ConnectServer mConnectServer;
//    private ConnectServer(){
//        mApi = Retrofit2.getInstance().getApi();
//    }
//
//
//    public static ConnectServer getInstance() {
//
//        if (mConnectServer == null) {
//            Log.e("SV", "getInstance: new ConnectServer "  );
//            mConnectServer = new ConnectServer();
//        }
//        return mConnectServer;
//    }
//
//    private API mApi;
//
//    public void createConn() {
//
//    }
//
//    public void getDSSanPham(int page, CallBack callBack) {
//
//    }
//
//
//    public ChiTietSanPham getChiTietSanPham(String idSP) {
//        return null;
//    }
//
//    @Override
//    public Call<Message> registerAcc(String name, String username, String password, String sdt, String diachi) {
//        return null;
//    }
//
//    @Override
//    public Call<ResLogin> signInAcc(String username, String password) {
//        return null;
//    }
//
//    @Override
//    public Call<Message> kiemTraTrangThaiDangNhap() {
//        return null;
//    }
//
//    @Override
//    public Call<UserAcc> layThongTinNguoiDung(String idNguoiDung) {
//        return null;
//    }
//
//    @Override
//    public Call<Message> capNhatThongTinNguoiDung(String idNguoiDung, int type, String data) {
//        return null;
//    }
//
//    @Override
//    public Call<Message> capNhatMatKhau(String idNguoiDung, String matKhauCu, String matKhauMoi) {
//        return null;
//    }
//
//    @Override
//    public Call<Message> dangXuat() {
//        return null;
//    }
//
//    @Override
//    public Call<Message> themSPVaoGioHang(String idSP, String idNguoiDung, int sanLuongMua) {
//        return null;
//    }
//
//    @Override
//    public Call<List<SPGioHang>> layGioHang(String idNguoiDung) {
//        return null;
//    }
//
//    @Override
//    public Call<Message> capNhatSanLuongMuaSP(String idSP, String idNguoiDung, int sanLuongMua) {
//        return null;
//    }
//
//    @Override
//    public Call<Message> xoaSPGioHang(String idNguoiDung, String idSP) {
//        return null;
//    }
//
//    @Override
//    public Call<Message> datHang(DonHang donHang) {
//        return null;
//    }
//
//    @Override
//    public Call<DSDonHang> layDSDonHang(String idNguoiMua, Boolean daDuyet, int page) {
//        return null;
//    }
//
//    @Override
//    public Call<DonHangRes> layChiTietDonHang(String idDonHang) {
//        return null;
//    }
//
//    @Override
//    public Call<ItemSPDonHang> layItemSPDonHang(String idSP) {
//        return null;
//    }
//}

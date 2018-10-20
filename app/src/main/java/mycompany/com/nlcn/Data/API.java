package mycompany.com.nlcn.Data;

import java.util.List;

import mycompany.com.nlcn.Model.ChiTietSanPham;
import mycompany.com.nlcn.Model.DataSanPham;
import mycompany.com.nlcn.Model.DonHang;
import mycompany.com.nlcn.Model.Message;
import mycompany.com.nlcn.Model.ResLogin;
import mycompany.com.nlcn.Model.SPGioHang;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface API {


    //Sản phẩm
    @GET("sanphams")
    Call<DataSanPham> getDSSanPham(@Query("page") int page);

    @GET("sanpham")
    Call<ChiTietSanPham> getChiTietSanPham(@Query("idSP") String idSP);

    //Tài khoản người dùng
    @FormUrlEncoded
    @POST("signup")
    Call<Message> registerAcc(@Field("name") String name, @Field("username") String username, @Field("password") String password,
                             @Field("email") String email);

    @FormUrlEncoded
    @POST("login")
    Call<ResLogin> signInAcc(@Field("username") String username, @Field("password") String password);

    @GET("checkTrangThai")
    Call<Message> kiemTraTrangThaiDangNhap();

    @GET("user")
    Call<?> layThongTin(@Query("idNguoiDung") String idNguoiDung);

    @FormUrlEncoded
    @PUT("user")
    Call<?> capNhatThongTin(@Field("idNguoiDung") String idNguoiDung);



    //Giỏ hàng
    @FormUrlEncoded
    @POST("giohang")
    Call<Message> themSPVaoGioHang(@Field("idSP") String idSP,
                                  @Field("idNguoiDung") String idNguoiDung,
                                  @Field("sanLuongMua") int sanLuongMua);

    @GET("giohang")
    Call<List<SPGioHang>> layGioHang(@Query("idNguoiDung") String idNguoiDung);

    @FormUrlEncoded
    @PUT("giohang")
    Call<Message> capNhatSanLuongMuaSP(@Field("idSP") String idSP,
                                       @Field("idNguoiDung") String idNguoiDung,
                                       @Field("sanLuongMua") int sanLuongMua);


    @DELETE("giohang")
    Call<Message> xoaSPGioHang(@Field("idSP") String idSP,
                               @Field("idNguoiDung") String idNguoiDung);

    //Đơn hàng
    @POST("donhang")
    Call<Message> datHang(@Body DonHang donHang);

    @GET("dsdonhang")
    Call<?> layDSDonHang(@Query("idNguoiMua") String idNguoiMua, @Query("daDuyet") Boolean daDuyet, @Query("page") int page);

    @GET("donhang")
    Call<?> layChiTietDonHang(@Query("idDonHang") String idDonHang);

    @GET("itemspdonhang")
    Call<?> layItemSPDonHang(@Query("idSP") String idSP);





}

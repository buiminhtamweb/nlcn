package mycompany.com.nlcn.Data;

import java.util.List;

import mycompany.com.nlcn.Model.ChiTietSanPham;
import mycompany.com.nlcn.Model.DSDonHang;
import mycompany.com.nlcn.Model.DataSanPham;
import mycompany.com.nlcn.Model.DonHang;
import mycompany.com.nlcn.Model.DonHangRes;
import mycompany.com.nlcn.Model.ItemSPDonHang;
import mycompany.com.nlcn.Model.Message;
import mycompany.com.nlcn.Model.ResLogin;
import mycompany.com.nlcn.Model.SPGioHang;
import mycompany.com.nlcn.Model.UserAcc;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
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
    Call<Message> registerAcc(@Field("name") String name,
                              @Field("username") String username,
                              @Field("password") String password,
                              @Field("sdt") String sdt,
                              @Field("diachi") String diachi);

    @FormUrlEncoded
    @POST("login")
    Call<ResLogin> signInAcc(@Header("Cookie") String userCookie,
                             @Field("username") String username,
                             @Field("password") String password);

    @GET("checkTrangThai")
    Call<Message> kiemTraTrangThaiDangNhap(@Header("Cookie") String userCookie);

    @GET("user")
    Call<UserAcc> layThongTinNguoiDung(@Header("Cookie") String userCookie,
                                       @Query("idNguoiDung") String idNguoiDung);

    @FormUrlEncoded
    @PUT("user")
    Call<Message> capNhatThongTinNguoiDung(@Header("Cookie") String userCookie,
                                           @Field("idNguoiDung") String idNguoiDung,
                                           @Field("type") int type,
                                           @Field("data") String data);

    @FormUrlEncoded
    @PUT("user")
    Call<Message> capNhatMatKhau(@Header("Cookie") String userCookie,
                                 @Field("idNguoiDung") String idNguoiDung,
                                           @Field("matKhauCu") String matKhauCu,
                                           @Field("matKhauMoi") String matKhauMoi);

    @POST("logout")
    Call<Message> dangXuat(@Header("Cookie") String userCookie);



    //Giỏ hàng
    @FormUrlEncoded
    @POST("giohang")
    Call<Message> themSPVaoGioHang(@Header("Cookie") String userCookie,
                                   @Field("idSP") String idSP,
                                  @Field("idNguoiDung") String idNguoiDung,
                                  @Field("sanLuongMua") int sanLuongMua);

    @GET("giohang")
    Call<List<SPGioHang>> layGioHang(@Header("Cookie") String userCookie,
                                     @Query("idNguoiDung") String idNguoiDung);

    @FormUrlEncoded
    @PUT("giohang")
    Call<Message> capNhatSanLuongMuaSP(@Header("Cookie") String userCookie,
                                       @Field("idSP") String idSP,
                                       @Field("idNguoiDung") String idNguoiDung,
                                       @Field("sanLuongMua") int sanLuongMua);


    @DELETE("giohang/{idNguoiDung}/{idSP}")
    Call<Message> xoaSPGioHang(@Header("Cookie") String userCookie,
                               @Path("idNguoiDung") String idNguoiDung,
                               @Path("idSP") String idSP
                               );

    //Đơn hàng
    @POST("donhang")
    Call<Message> datHang(@Header("Cookie") String userCookie,
                          @Body DonHang donHang);

    @GET("dsdonhang")
    Call<DSDonHang> layDSDonHang(@Header("Cookie") String userCookie,
                                 @Query("idNguoiMua") String idNguoiMua, @Query("daDuyet") Boolean daDuyet, @Query("page") int page);

    @GET("donhang")
    Call<DonHangRes> layChiTietDonHang(@Header("Cookie") String userCookie,
                                       @Query("idDonHang") String idDonHang);

    @GET("itemspdonhang")
    Call<ItemSPDonHang> layItemSPDonHang(@Header("Cookie") String userCookie,
                                         @Query("idSP") String idSP);



}

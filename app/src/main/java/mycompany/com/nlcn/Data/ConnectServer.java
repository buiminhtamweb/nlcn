package mycompany.com.nlcn.Data;

import android.content.Context;

import mycompany.com.nlcn.Constant;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ConnectServer {
    private static ConnectServer mConnectServer;
    private Retrofit mRetrofit;

    private ConnectServer(Context context) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
//        builder.addInterceptor(new AddCookiesInterceptor(mContext)); // VERY VERY IMPORTANT
        builder.addInterceptor(new ReceivedCookiesInterceptor(context)); // VERY VERY IMPORTANT
        OkHttpClient mClient = builder.build();

        mRetrofit = new Retrofit.Builder()
                .baseUrl(Constant.URL_SERVER+ "android/")
                .client(mClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static ConnectServer getInstance(Context context) {

        if (mConnectServer == null) {
            mConnectServer = new ConnectServer(context);
        }
        return mConnectServer;
    }

    public static void destroy() {
        mConnectServer = null;
    }

    public API CreateApi() {
        return mRetrofit.create(API.class);
    }

}

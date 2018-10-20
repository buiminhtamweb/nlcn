package mycompany.com.nlcn.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mycompany.com.nlcn.R;
import mycompany.com.nlcn.utils.SharedPreferencesHandler;

public class UserFrag extends Fragment {
    String idNguoiDung = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_user, container, false);

        idNguoiDung = SharedPreferencesHandler.getString(getActivity(), "id");


        return view;
    }



}

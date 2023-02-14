package com.muratcivek.kampsosyalmedya;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;


public class fragment_akis_sayfasi extends Fragment {
View view;
Dialog dialog;
Button buton;
        @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       view = inflater.inflate(R.layout.fragment_ana_sayfa, container, false);



// fragment anasayfada olan butona tıklayınca deneme tasarımını açan kod blogu!!!!!!!!!!!!!
       dialog = new Dialog(getActivity());
        buton = view.findViewById(R.id.paylasdeneme);
        buton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.setContentView(R.layout.yorum_ekle_gelecek_ekran);

                dialog.show();
            }
        });




        return view;
    }



}
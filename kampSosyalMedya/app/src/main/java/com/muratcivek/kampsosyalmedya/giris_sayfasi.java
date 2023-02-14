package com.muratcivek.kampsosyalmedya;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class giris_sayfasi extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_giris_sayfasi);
    }

    public void KayitOlGit (View view){



        Intent ikincisayfagecis=new Intent(giris_sayfasi.this, kayit_ol_sayfa.class);
        startActivity(ikincisayfagecis);
}

}

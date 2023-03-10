package com.muratcivek.kampsosyalmedya;

import static android.content.ContentValues.TAG;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.webkit.PermissionRequest;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.units.qual.K;

import java.security.Permission;
import java.security.Permissions;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class kayit_ol_sayfa extends AppCompatActivity {
    EditText etKayitKullaniciAdi;
    EditText etKayitMail;
    EditText etKayitSifre;
    EditText etKayitSehir;
    Button btnKayitOl;
    Button btnFotoSec;
    CircleImageView civFoto;

   String izin[] = {Manifest.permission.READ_EXTERNAL_STORAGE};




    ProgressDialog progressDialog;

    private FirebaseAuth mAuth;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseApp.initializeApp(this);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_kayit_ol_sayfasi);

        etKayitKullaniciAdi = findViewById(R.id.etKayitKullaniciAdi);
        etKayitMail = findViewById(R.id.etKayitMail);
        etKayitSifre = findViewById(R.id.etKayitSifre);
        etKayitSehir= findViewById(R.id.etKayitSehir);
        btnKayitOl = findViewById(R.id.btnKayitOl);
        btnFotoSec = findViewById(R.id.kayit_ol_foto_ekle);
        civFoto = findViewById(R.id.profilFoto);
        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Kayd??n??z yap??l??yor, l??tfen bekleyiniz...");

         db = FirebaseFirestore.getInstance();


    }
    public void btnsecFoto (View view ) {
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE)==
                PackageManager.PERMISSION_GRANTED){
            Intent galeriyeGit = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

            startActivityForResult(galeriyeGit,100);


        }
        else{
            ActivityCompat.requestPermissions(this,izin,100);
}
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK && data !=null){
            Uri secilenFoto = data.getData();
            civFoto.setImageURI(secilenFoto);

        }
        else{
            Toast.makeText(kayit_ol_sayfa.this, "L??tfen bir foto??raf se??iniz",Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if(requestCode==100){
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(kayit_ol_sayfa.this, "??zin kabul edildi",Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(kayit_ol_sayfa.this, "??zin kabul edilmedi",Toast.LENGTH_SHORT).show();
            }

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void btnKayitTikla (View view ){
        String KullaniciAdi = etKayitKullaniciAdi.getText().toString().trim();
        String Mail = etKayitMail.getText().toString().trim();
        String Sifre= etKayitSifre.getText().toString().trim();
        String Sehir = etKayitSehir.getText().toString().trim();
        if(!Patterns.EMAIL_ADDRESS.matcher(Mail).matches()){
            etKayitMail.setError("Ge??ersiz Mail!");
            etKayitMail.setFocusable(true);

        }
        else if (Sifre.length() >10){
            etKayitSifre.setError("L??tfen 10 karakteri a??mayacak ??ekilde ??ifrenizi olu??turun.");
            etKayitSifre.setFocusable(true);
        }
        else {

            bilgiKayit(KullaniciAdi,Mail,Sifre,Sehir);


        }



    }

    private void bilgiKayit(String KullaniciAdi, String Mail, String Sifre, String Sehir) {

        Map<String, Object> kullaniciKayitBilgi = new HashMap<>();
        kullaniciKayitBilgi.put("Kullanici adi", KullaniciAdi);
        kullaniciKayitBilgi.put("Mail", Mail);
        kullaniciKayitBilgi.put("??ifre", Sifre);
        kullaniciKayitBilgi.put("??ehir", Sehir);
// Veritaban??na kay??t ekleme (Kullan???? bilgilerini)

        db.collection("KullaniciKayitBilgi").document(Mail).set(kullaniciKayitBilgi)
                .addOnSuccessListener(kayit_ol_sayfa.this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(kayit_ol_sayfa.this, "Bilgileriniz kaydedilmi??tir.",Toast.LENGTH_SHORT).show();
                        kayit(Mail,Sifre);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                Toast.makeText(kayit_ol_sayfa.this, "??uanda bilgilerinizi alam??yoruz." +
                        " L??tfen daha sonra tekrar deneyiniz.",Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void kayit(String mail, String sifre) {
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(mail, sifre)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete( Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent girisYapGecis=new Intent(kayit_ol_sayfa.this, giris_sayfasi.class);
                            startActivity(girisYapGecis);
                            finish();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(kayit_ol_sayfa.this," Kay??t i??lemi ba??ar??z oldu, l??tfen daha g????l?? bir ??ifre belirleyerek tekrar deneyiniz."
                                    +" Ayn?? sorun devam ederse daha sonra deneyiniz.",Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                progressDialog.dismiss();
                Toast.makeText(kayit_ol_sayfa.this," Kay??t i??lemi ba??ar??z oldu, l??tfen daha g????l?? bir ??ifre belirleyerek tekrar deneyiniz."
                        +" Tekrardan ayn?? sorunla kar????la????rsan??z daha sonra deneyiniz...",Toast.LENGTH_SHORT).show();

            }
        });

    }



}
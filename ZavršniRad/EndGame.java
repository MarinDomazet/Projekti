package com.example.sqltest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class EndGame extends AppCompatActivity {

    Button btnRestart, btnExit;
    TextView tvSuccessFail, tvPlayer, tvPosition, tvAge, tvShirt;
    ImageView ivPlayerImg, ivNation, ivClub;
    Boolean resResult;
    String resShirt, resAge, resPosition, resClub, resNation, resName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_game);

        btnRestart = (Button) findViewById(R.id.btnRestart);
        btnExit = (Button) findViewById(R.id.btnExit);

        tvSuccessFail = findViewById(R.id.tvSuccessFail);
        tvPlayer = findViewById(R.id.tvPlayer);
        tvPosition = findViewById(R.id.tvPosition);
        tvAge = findViewById(R.id.tvAge);
        tvShirt = findViewById(R.id.tvShirt);

        ivNation = (ImageView) findViewById(R.id.ivNation);
        ivClub = (ImageView) findViewById(R.id.ivClub);
        ivPlayerImg = (ImageView) findViewById(R.id.ivPlayerImg);

        Intent intent = getIntent();
        if (intent != null) {
            resName = intent.getStringExtra("keyName");
            resNation = intent.getStringExtra("keyNation");
            resClub = intent.getStringExtra("keyClub");
            resPosition = intent.getStringExtra("keyPosition");
            resAge = intent.getStringExtra("keyAge");
            resShirt = intent.getStringExtra("keyShirt");
            resResult = intent.getBooleanExtra("keyResult",false);

            Bitmap receivedBitmap = intent.getParcelableExtra("bitmap");
            if (receivedBitmap != null) {
                ivPlayerImg.setImageBitmap(receivedBitmap);
            }else{
                ivPlayerImg.setImageResource(R.drawable.nula);
            }

            byte[] compressedImageBytes = intent.getByteArrayExtra("compressedImage");
            if (compressedImageBytes != null) {
                Bitmap compressedBitmap = BitmapFactory.decodeByteArray(compressedImageBytes, 0, compressedImageBytes.length);
                ivPlayerImg.setImageBitmap(compressedBitmap);
            } else {
                ivPlayerImg.setImageResource(R.drawable.nula);
            }

            byte[] compressedImageBytes3 = intent.getByteArrayExtra("compressedImage3");
            if (compressedImageBytes3 != null) {
                Bitmap compressedBitmap3 = BitmapFactory.decodeByteArray(compressedImageBytes3, 0, compressedImageBytes3.length);
                ivClub.setImageBitmap(compressedBitmap3);
            } else {
                ivClub.setImageResource(R.drawable.nula);
            }

            byte[] compressedImageBytes4 = intent.getByteArrayExtra("compressedImage4");
            if (compressedImageBytes4 != null) {
                Bitmap compressedBitmap4 = BitmapFactory.decodeByteArray(compressedImageBytes4, 0, compressedImageBytes4.length);
                ivNation.setImageBitmap(compressedBitmap4);
            } else {
                ivNation.setImageResource(R.drawable.nula);
            }

            if(resResult==true){
                tvSuccessFail.setText(R.string.right);
                tvSuccessFail.setTextColor(Color.GREEN);
            }else{
                tvSuccessFail.setText(R.string.wrong);
                tvSuccessFail.setTextColor(Color.RED);
            }

            tvPlayer.setText(resName);
            tvPosition.setText(resPosition);
            tvAge.setText(resAge+" "+getString(R.string.years));
            tvShirt.setText("#"+resShirt);

        } else {
            Toast.makeText(this, R.string.fetch, Toast.LENGTH_SHORT).show();
        }

        btnRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EndGame.this, MainActivity.class);
                startActivity(intent);
            }
        });

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishAffinity();
                System.exit(0);
            }
        });
    }
}
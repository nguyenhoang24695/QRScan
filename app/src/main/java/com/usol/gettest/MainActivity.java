package com.usol.gettest;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.google.zxing.qrcode.QRCodeWriter;
import com.usol.gettest.ui.tool.ToolFragment;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                1);
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                1);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    @Override
    public void onActivityResult(int requestCode, int resultcode, Intent intent) {
        super.onActivityResult(requestCode, resultcode, intent);
        TextView tv = (TextView) this.findViewById(R.id.scan_result);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultcode, intent);
        tv.setText(result.getContents());
        ImageView qr_img = (ImageView) this.findViewById(R.id.imageView);

        QRCodeWriter barcode = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = barcode.encode(result.getContents(), BarcodeFormat.QR_CODE,1000,1000);
            int BLACK = 0xFFFFFFFF;
            int WHITE = 0xFF000000;
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            int[] pixels = new int[width*height];
            for (int y = 0; y< height; y++){
                int offset = y * width;
                for(int x = 0; x < width; x++){
                    pixels[offset + x] = bitMatrix.get(x,y)?WHITE:BLACK;
                }
            }
            Bitmap bitmap = Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels,0,width,0,0,width,height);
            ImageView imageView = (ImageView) this.findViewById(R.id.qr_img);
            imageView.setImageBitmap(bitmap);

            String path = Environment.getExternalStorageDirectory().toString();

            Long tsLong = System.currentTimeMillis()/1000;
            String ts = tsLong.toString();
            ts = "A";
            File file = new File(path,ts+".jpg");

            try (FileOutputStream out = new FileOutputStream(file)) {

                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
                // PNG is a lossless format, the compression factor (100) is ignored
                out.close();
                MediaStore.Images.Media.insertImage(getContentResolver(),file.getAbsolutePath(),file.getName(),file.getName());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (WriterException e) {
            e.printStackTrace();
        }

    }
}
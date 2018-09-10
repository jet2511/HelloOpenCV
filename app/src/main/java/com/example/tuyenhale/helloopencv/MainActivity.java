package com.example.tuyenhale.helloopencv;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView imageView;
    Button btnRgb, btnGray, btnYcrGb, btnHsv;
    Button btnOpen;

    private static int RESULT_LOAD_IMAGE = 1;
    private static final String TAG = "OpenCV";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        btnRgb.setOnClickListener(this);
        btnGray.setOnClickListener(this);
        btnYcrGb.setOnClickListener(this);
        btnHsv.setOnClickListener(this);
        btnOpen.setOnClickListener(this);
    }

    private void initView() {
        imageView = findViewById(R.id.img);

        btnGray = findViewById(R.id.btnGray);
        btnRgb = findViewById(R.id.btnRgb);
        btnYcrGb = findViewById(R.id.btnYcrCb);
        btnHsv = findViewById(R.id.btnHsv);
        btnOpen = findViewById(R.id.btnOpen);
    }

    @Override
    public void onClick(View view) {
        ProgressDialog pd = new ProgressDialog(MainActivity.this);
        pd.setTitle("OpenCV");
        pd.setMessage("Converting!");
        pd.show();
        pd.setCancelable(false);


        try {
            Mat dst = new Mat();
            Mat src = Utils.loadResource(MainActivity.this, R.drawable.chopper, Imgcodecs.CV_LOAD_IMAGE_COLOR);

            switch (view.getId()) {
                case R.id.btnRgb:
                    dst = src;
                    break;
                case R.id.btnGray:
                    Imgproc.cvtColor(src, dst, Imgproc.COLOR_RGB2GRAY);
                    break;
                case R.id.btnYcrCb:
                    Imgproc.cvtColor(src, dst, Imgproc.COLOR_RGB2YCrCb);
                    break;
                case R.id.btnHsv:
                    Imgproc.cvtColor(src, dst, Imgproc.COLOR_RGB2HSV);
                    break;
                default:
                    break;
            }
            Bitmap img = Bitmap.createBitmap(dst.cols(), dst.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(dst, img);

            imageView.setImageBitmap(img);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pd.dismiss();
        }

        if (view.getId() == R.id.btnOpen) {
            // Mo anh
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, RESULT_LOAD_IMAGE);

//            Intent gallery = new Intent(Intent.ACTION_GET_CONTENT);
//            gallery.setType("image/*");
//            startActivityForResult(gallery, RESULT_LOAD_IMAGE);
        }

    }


    static {
        if (OpenCVLoader.initDebug()) {
            Log.d(TAG, "OpenCV loaded");
        } else {
            Log.d(TAG, "OpenCV not loaded.");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }
    }
}

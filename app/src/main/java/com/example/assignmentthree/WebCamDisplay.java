package com.example.assignmentthree;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;

public class WebCamDisplay extends AppCompatActivity {
    //creating the view objects to be used
    TextView TitleText;
    TextView CityText;
    ImageView WebCamView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_cam_display);
        //setting it to the views id
        TitleText = findViewById(R.id.TitleText);
        CityText = findViewById(R.id.CityText);
        WebCamView = findViewById(R.id.WebCamView);
        //getting the information from intent
        String Title = getIntent().getStringExtra("Title");
        String CityReg = getIntent().getStringExtra("CityRegion");
        String Img = getIntent().getStringExtra("Thumbnail");
        //updating the activity with the information of the desired camera
        TitleText.setText(Title);
        CityText.setText(CityReg);
        new DownloadImageTask((ImageView) WebCamView).execute(Img);


    }


    //image downloader from https://stackoverflow.com/questions/2471935/how-to-load-an-imageview-by-url-in-android
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
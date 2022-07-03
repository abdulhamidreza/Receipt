package com.valiance.receipt;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;


public class ShowPdfActivity extends AppCompatActivity {

    ImageView imageViewPdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_pdf);
        imageViewPdf = findViewById(R.id.pdf_image);
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            imageViewPdf.setImageBitmap(new ShowPdf().showPage("Invoice34.pdf"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }


}
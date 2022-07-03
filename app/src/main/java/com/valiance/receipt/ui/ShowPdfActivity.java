package com.valiance.receipt.ui;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.valiance.receipt.R;
import com.valiance.receipt.pdf.SharePdf;
import com.valiance.receipt.pdf.ShowPdf;

import java.io.IOException;


public class ShowPdfActivity extends AppCompatActivity {

    ImageView imageViewPdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_pdf);
        imageViewPdf = findViewById(R.id.pdf_image);

        new SharePdf().provideContentUri(this);
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
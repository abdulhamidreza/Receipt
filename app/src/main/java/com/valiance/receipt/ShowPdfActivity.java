package com.valiance.receipt;

import static androidx.core.content.FileProvider.getUriForFile;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.IOException;
import java.util.List;


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


    private void provideContentUri() {
        File file = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                "Invoice34.pdf"
        );
        Uri contentUri = getUriForFile(getApplicationContext(),
                "com.valiance.receipt.provider", file);
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("application/pdf");
        sharingIntent.putExtra(Intent.EXTRA_STREAM, contentUri);


        Intent chooser = Intent.createChooser(sharingIntent, "Share File");

        List<ResolveInfo> resInfoList = this.getPackageManager().queryIntentActivities(chooser, PackageManager.MATCH_DEFAULT_ONLY);

        for (ResolveInfo resolveInfo : resInfoList) {
            String packageName = resolveInfo.activityInfo.packageName;
            this.grantUriPermission(packageName, contentUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }

        startActivity(chooser);
    }

}
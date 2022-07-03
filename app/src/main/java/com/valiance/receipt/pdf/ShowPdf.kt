package com.valiance.receipt.pdf

import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.Environment
import android.os.ParcelFileDescriptor
import java.io.File
import java.io.IOException

class ShowPdf {

    private lateinit var pdfRenderer: PdfRenderer
    private lateinit var currentPage: PdfRenderer.Page
    private lateinit var parcelFileDescriptor: ParcelFileDescriptor
    private val pageIndex = 0

    @Throws(IOException::class)
    fun showPage(file: String): Bitmap? {
        openRenderer(file)
        if (pdfRenderer.getPageCount() <= pageIndex) {
            return null
        }
        // Use `openPage` to open a specific page in PDF.
        currentPage = pdfRenderer.openPage(pageIndex)
        // Important: the destination bitmap must be ARGB (not RGB).
        val bitmap = Bitmap.createBitmap(
            currentPage.getWidth(), currentPage.getHeight(),
            Bitmap.Config.ARGB_8888
        )
        // Here, we render the page onto the Bitmap.
        // Pass either RENDER_MODE_FOR_DISPLAY or RENDER_MODE_FOR_PRINT for the last parameter.
        currentPage.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)

        closeRenderer()

        // We are ready to show the Bitmap
        return bitmap
    }

    @Throws(IOException::class)
    private fun closeRenderer() {
        currentPage.close()
        pdfRenderer.close()
        parcelFileDescriptor.close()
    }


    @Throws(IOException::class)
    private fun openRenderer(file: String) {
        val file = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            file
        )
        if (!file.exists()) {
            throw  ArithmeticException("File not exist");
        }
        parcelFileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
        // This is the PdfRenderer we use to render the PDF.
        pdfRenderer = PdfRenderer(parcelFileDescriptor)

    }

}
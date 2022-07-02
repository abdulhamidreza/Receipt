package com.valiance.receipt

import android.graphics.*
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.util.Log
import com.valiance.receipt.room.Receipt
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDateTime

object PdfGenerator {

    fun generate(bmp: Bitmap, receipt: Receipt): String {

        val Tag = "PdfGenerator"
        val pdfDocument = PdfDocument()

        val pageInfo: PdfDocument.PageInfo = PdfDocument.PageInfo.Builder(1200, 1500, 1).create()
        val pdfPage: PdfDocument.Page = pdfDocument.startPage(pageInfo)
        val canvas: Canvas = pdfPage.canvas

        val scaledBmp = Bitmap.createScaledBitmap(bmp, 1200, 180, false)
        canvas.drawBitmap(scaledBmp, 0.0f, 0.0f, Paint())

        var titlePaint = Paint()
        titlePaint.textAlign = Paint.Align.CENTER
        titlePaint.typeface = Typeface.DEFAULT_BOLD
        titlePaint.textSize = 70.0f
        canvas.drawText("RECEIPT", 1200 / 2f, 260f, titlePaint)


        pdfDocument.finishPage(pdfPage)

        val file = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            "Invoice" + LocalDateTime.now().second + ".pdf"
        )
        try {
            pdfDocument.writeTo(FileOutputStream(file))
            pdfDocument.close()
        } catch (e: Exception) {
            pdfDocument.close()
            Log.e(Tag, e.printStackTrace().toString(), e)
            return ""
        }

        return file.absolutePath

    }

}
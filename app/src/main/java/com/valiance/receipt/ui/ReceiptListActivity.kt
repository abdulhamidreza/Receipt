package com.valiance.receipt.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.valiance.receipt.R
import com.valiance.receipt.databinding.ActivityReceiptListBinding
import com.valiance.receipt.pdf.GeneratePdf
import com.valiance.receipt.room.Receipt
import com.valiance.receipt.room.ReceiptViewModel
import com.valiance.receipt.ui.MyApplication
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class ReceiptListActivity : AppCompatActivity(), ReceiptRecyclerAdapter.IGetReceiptData {

    private val STORAGE_REQUEST_CODE = 99;
    private lateinit var builder: AlertDialog
    private lateinit var binding: ActivityReceiptListBinding
    private lateinit var mAddFab: FloatingActionButton
    private val formatterDate: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy hh:mm a")

    private var receiptList: List<Receipt> = ArrayList()
    private lateinit var recyclerView: RecyclerView
    private var linearLayoutManager: LinearLayoutManager? = null
    private var recyclerAdapter: ReceiptRecyclerAdapter? = null
    private val receiptViewModel: ReceiptViewModel by viewModels {
        ReceiptViewModel.ReceiptViewModelFactory((application as MyApplication).repository)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityReceiptListBinding.inflate(layoutInflater)

        setContentView(binding.root)

        mAddFab = binding.addReceiptFab
        mAddFab.setOnClickListener(View.OnClickListener {
            alertEdit(null)
        })


        //Middle Recycler View //todo
        recyclerView = binding.receiptListRecyclerView
        recyclerView.setHasFixedSize(true)
        linearLayoutManager =
            LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = linearLayoutManager
        recyclerAdapter = ReceiptRecyclerAdapter(receiptList, this)
        recyclerView.adapter = recyclerAdapter

        receiptViewModel.allReceiptRealTime.observe(this) {
            Log.d("**********", Gson().toJson(it))
            receiptList = it
            recyclerAdapter!!.updateUserList(receiptList)

        }

        receiptViewModel.loadingStatus.observe(this) {
            if (it) builder.dismiss()
        }
        receiptViewModel.errorMessage.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }

    }


    private fun alertEdit(receipt: Receipt?) {
        builder = AlertDialog.Builder(this, R.style.CustomAlertDialog)
            .create()
        val view = layoutInflater.inflate(R.layout.receipt_add_view, null)
        builder.setView(view)
        builder.setCanceledOnTouchOutside(true)
        builder.show()

        val title = view.findViewById<EditText>(R.id.editTextQuantity1)
        val saveBtn = view.findViewById<Button>(R.id.saveBtn)

        receipt?.let {
            title.setText(receipt.priceP1.toString())
        }

        builder.setOnCancelListener {
            Toast.makeText(this, "Changes not saved", Toast.LENGTH_SHORT).show()
        }

        saveBtn.setOnClickListener {
            if (checkPermission()) {
                val s = GeneratePdf.generate(
                    BitmapFactory.decodeResource(
                        this.resources,
                        R.drawable.banner
                    ), Receipt(
                        formatterDate.format(LocalDateTime.now()), 1.1, 1,
                        3.1, 2, 2.1, 1, ""
                    )
                )
                Log.d("******** path : ", s)

            } else {
                Toast.makeText(this, "Storage permission not there", Toast.LENGTH_SHORT).show()
                requestPermission()
            }
            if (receipt == null) {
                receiptViewModel.insertReceipt(
                    Receipt(
                        formatterDate.format(LocalDateTime.now()), 1.1, 1,
                        3.1, 2, 2.1, 1, ""
                    )
                )
            }


        }
    }

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.R) {
            try {
                val intent = Intent()
                intent.action = Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
                val uri = Uri.fromParts("package", this.packageName, null)
                intent.data = uri
                storageActivityLauncher.launch(intent)
            } catch (e: Exception) {
                val intent = Intent()
                intent.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
                storageActivityLauncher.launch(intent)
            }
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ), STORAGE_REQUEST_CODE
            )
        }
    }

    private val storageActivityLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    //Todo call Save
                    Toast.makeText(this, "Save here", Toast.LENGTH_SHORT).show()

                } else {
                    Toast.makeText(this, "Storage Permission Denied", Toast.LENGTH_SHORT).show()
                }
            } else {
                //bellow 11
            }
        }

    private fun checkPermission(): Boolean {
        return if (Build.VERSION.SDK_INT > Build.VERSION_CODES.R) {
            Environment.isExternalStorageManager()
        } else {
            //bellow 11
            val write =
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            val read =
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            write == PackageManager.PERMISSION_GRANTED && read == PackageManager.PERMISSION_GRANTED
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == STORAGE_REQUEST_CODE) {
            if (grantResults.isNotEmpty()) {
                val write = grantResults[0] == PackageManager.PERMISSION_GRANTED
                val read = grantResults[1] == PackageManager.PERMISSION_GRANTED
                if (write && read) {
                    //Todo call Save
                    Toast.makeText(this, "Save here", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Storage Permission Denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onReceiptEditClicked(receipt: Receipt) {
        alertEdit(receipt)
    }

    override fun onReceiptDeleteClicked(receipt: Receipt) {
        TODO("Not yet implemented")
    }


}
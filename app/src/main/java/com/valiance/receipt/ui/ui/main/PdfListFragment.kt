package com.valiance.receipt.ui.ui.main

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.valiance.receipt.R
import com.valiance.receipt.databinding.FragmentPdfListBinding
import com.valiance.receipt.databinding.ReceiptAddViewBinding
import com.valiance.receipt.pdf.GeneratePdf
import com.valiance.receipt.room.Receipt
import com.valiance.receipt.ui.MyApplication
import com.valiance.receipt.ui.ReceiptRecyclerAdapter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class PdfListFragment : Fragment(), ReceiptRecyclerAdapter.IGetReceiptData {

    companion object {
        fun newInstance() = PdfListFragment()
    }


    private var _binding: FragmentPdfListBinding? = null

    private lateinit var builder: AlertDialog
    private lateinit var mAddFab: FloatingActionButton
    private val formatterDate: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy hh:mm a")

    private var receiptList: List<Receipt> = ArrayList()
    private lateinit var recyclerView: RecyclerView
    private var linearLayoutManager: LinearLayoutManager? = null
    private var recyclerAdapter: ReceiptRecyclerAdapter? = null
    private val receiptViewModel: ReceiptViewModel by viewModels {
        ReceiptViewModel.ReceiptViewModelFactory((requireActivity().application as MyApplication).repository)
    }

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentPdfListBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        mAddFab = binding.addReceiptFab
        mAddFab.setOnClickListener {
            addNewReceipt()
        }

        recyclerView = _binding!!.receiptListRecyclerView
        recyclerView.setHasFixedSize(true)
        linearLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = linearLayoutManager
        recyclerAdapter = ReceiptRecyclerAdapter(receiptList, this)
        recyclerView.adapter = recyclerAdapter

        receiptViewModel.allReceiptRealTime.observe(viewLifecycleOwner) {
            receiptList = it
            recyclerAdapter!!.updateUserList(receiptList)

        }

        receiptViewModel.loadingStatus.observe(viewLifecycleOwner) {
            if (it) builder.dismiss()
        }
        receiptViewModel.errorMessage.observe(viewLifecycleOwner) {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onReceiptEditClicked(receipt: Receipt) {
        if (receipt.filePath.isBlank()) {
            generatePdf(receipt)
        } else {
            val fragment = ShowPdfFragment.newInstance()
            val args = Bundle()
            args.putString("filePath", receipt.filePath)
            fragment.arguments = args
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit()
        }
    }

    private fun addNewReceipt() {
        builder = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog)
            .create()

        val _bindingAlert = ReceiptAddViewBinding.inflate(layoutInflater)

        builder.setView(_bindingAlert.root)
        builder.setCanceledOnTouchOutside(true)
        builder.show()

        builder.setOnCancelListener {
            Toast.makeText(context, "Changes not saved", Toast.LENGTH_SHORT).show()
        }

        _bindingAlert.saveBtn.setOnClickListener {
            if (_bindingAlert.editTextPrice1.text.isEmpty() ||
                _bindingAlert.editTextQuantity1.text.isEmpty() ||
                _bindingAlert.editTextPrice2.text.isEmpty() ||
                _bindingAlert.editTextQuantity2.text.isEmpty() ||
                _bindingAlert.editTextPrice3.text.isEmpty() ||
                _bindingAlert.editTextQuantity3.text.isEmpty()
            ) {
                Toast.makeText(requireContext(), "Enter all data", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val receipt = Receipt(
                formatterDate.format(LocalDateTime.now()),
                _bindingAlert.editTextPrice1.text.toString().toDouble(),
                _bindingAlert.editTextQuantity1.text.toString().toInt(),
                _bindingAlert.editTextPrice2.text.toString().toDouble(),
                _bindingAlert.editTextQuantity2.text.toString().toInt(),
                _bindingAlert.editTextPrice3.text.toString().toDouble(),
                _bindingAlert.editTextQuantity3.text.toString().toInt(),
                ""
            )

            generatePdf(receipt)

            builder.dismiss()

        }

    }

    private fun generatePdf(receipt: Receipt) {
        receiptViewModel.insertReceipt(receipt)

        val filePath = GeneratePdf.generate(
            BitmapFactory.decodeResource(
                this.resources,
                R.drawable.pizzahead
            ), receipt
        )


        receipt.filePath = filePath
        receiptViewModel.updateReceipt(receipt)

        val fragment = ShowPdfFragment.newInstance()
        val args = Bundle()
        args.putString("filePath", filePath)
        fragment.arguments = args
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .addToBackStack(null)
            .commit()

    }

}
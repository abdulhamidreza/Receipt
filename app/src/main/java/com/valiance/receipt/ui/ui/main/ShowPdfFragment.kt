package com.valiance.receipt.ui.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.valiance.receipt.databinding.FragmentShowPdfBinding
import com.valiance.receipt.pdf.SharePdf
import com.valiance.receipt.pdf.ShowPdf
import com.valiance.receipt.ui.MyApplication
import java.io.IOException

class ShowPdfFragment : Fragment() {

    companion object {
        fun newInstance() = ShowPdfFragment()
    }

    private var _binding: FragmentShowPdfBinding? = null


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val receiptViewModel: ReceiptViewModel by viewModels {
        ReceiptViewModel.ReceiptViewModelFactory((requireActivity().application as MyApplication).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentShowPdfBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle: Bundle? = this.arguments
        if (bundle != null) {
            try {
                var filePath = ShowPdf().findFilePath(
                    bundle.getString("filePath", "")
                )
                if (filePath != null) {
                    _binding!!.pdfImage.setImageBitmap(
                        ShowPdf().showPage(
                            filePath
                        )
                    )

                    _binding!!.shareDdf.setOnClickListener {
                        SharePdf().sharePdf(
                            requireActivity(),
                            filePath
                        )
                    }
                }

            } catch (e: IOException) {
                e.printStackTrace()
            }
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
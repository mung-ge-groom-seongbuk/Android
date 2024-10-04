package com.example.mungge_groom.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mungge_groom.databinding.DialogBottomSheetHomeBinding
import com.example.mungge_groom.ui.listener.onClickBottomSheetDialogListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class HomeBottomSheetDialog(
    private val onClickBottomSheetDialogListener: onClickBottomSheetDialogListener
) : BottomSheetDialogFragment() {
    lateinit var binding: DialogBottomSheetHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DialogBottomSheetHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.dialogBottomSheetHomeBt.setOnClickListener {
            onClickBottomSheetDialogListener.onClick()
            dismiss()
        }
    }

}
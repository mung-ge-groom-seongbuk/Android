package com.example.mungge_groom.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mungge_groom.data.model.RunningCrewData
import com.example.mungge_groom.databinding.DialogBottomSheetHomeBinding
import com.example.mungge_groom.extention.GlobalApplication
import com.example.mungge_groom.ui.listener.onClickBottomSheetDialogListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class HomeBottomSheetDialog(
    private val onClickBottomSheetDialogListener: onClickBottomSheetDialogListener,
    private val crewData: RunningCrewData?
) : BottomSheetDialogFragment() {
    lateinit var binding: DialogBottomSheetHomeBinding
    private var inputRunningCrewData: RunningCrewData? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DialogBottomSheetHomeBinding.inflate(inflater, container, false)
        this.inputRunningCrewData = crewData
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            runningCrewData = inputRunningCrewData
            runningCrewData?.profile?.let {
                GlobalApplication.loadProfileImage(
                    dialogBottomSheetProfileIv,
                    it
                )
            }
            dialogBottomSheetHomeBt.setOnClickListener {
                onClickBottomSheetDialogListener.onClick(dialogBottomSheetTitle.text.toString())
                dismiss()
            }
        }
    }

}
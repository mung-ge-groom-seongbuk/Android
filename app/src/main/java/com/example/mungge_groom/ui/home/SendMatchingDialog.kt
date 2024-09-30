package com.example.mungge_groom.ui.home

import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.mungge_groom.databinding.DialogSendMatchingBinding
import com.example.mungge_groom.ui.listener.onClickSendMatchListener

class SendMatchingDialog(
    private val onClickSendMatchListener: onClickSendMatchListener,
    name : String
) : DialogFragment() {

    private var _binding: DialogSendMatchingBinding? = null
    private val binding get() = _binding!!
    private var name: String? = null

    init {
        this.name = name
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogSendMatchingBinding.inflate(inflater, container, false)
        val view = binding.root
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.dialogSendMatchingTitleTv.text = "${name}님께 매칭을 보내시겠어요?"

        binding.dialogSendMatchingSendBt.setOnClickListener {
            val message = binding.dialogSendMatchingMessageEt.text.toString()
            onClickSendMatchListener.onSendMatch(message)
            dismiss()
        }

        binding.dialogSendMatchingCancelBt.setOnClickListener {
            dismiss()
        }


        return view
    }

    override fun onResume() {
        super.onResume()
        val params: ViewGroup.MarginLayoutParams = binding.root.layoutParams as ViewGroup.MarginLayoutParams

        // 원하는 마진 값 설정 (예: 좌우 50dp)
        val marginInDp = 20
        val scale = resources.displayMetrics.density
        val marginInPx = (marginInDp * scale + 0.5f).toInt()

        params.setMargins(marginInPx, 0, marginInPx, 0)

        binding.root.layoutParams = params

        // 다이얼로그 크기 설정
        val windowParams: WindowManager.LayoutParams = dialog!!.window!!.attributes
        windowParams.width = WindowManager.LayoutParams.MATCH_PARENT
        windowParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog!!.window!!.attributes = windowParams
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}


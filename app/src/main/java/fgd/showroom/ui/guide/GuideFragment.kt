package fgd.showroom.ui.guide

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import fgd.showroom.MainActivity
import fgd.showroom.R
import fgd.showroom.databinding.FragmentGuideBinding
import fgd.showroom.logic.model.Step
import fgd.showroom.ui.observeGuideRpInfo

class GuideFragment : Fragment() {
    val viewModel by lazy { ViewModelProvider(this)[GuideViewModel::class.java] }
    private var _binding: FragmentGuideBinding? = null
    private val binding get() = _binding!!
    private var stepList = mutableListOf<Step>()
    val tv_map = mutableMapOf<Int, TextView>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGuideBinding.inflate(inflater, container, false)
        val root: View = binding.root

        observeGuideRpInfo(viewLifecycleOwner, requireActivity(), viewModel.gotostepRp, viewModel, tv_map)
        viewModel.stepList.observe(viewLifecycleOwner) { items ->
            Log.d("SettingFragment", items.toString())
            stepList = items
            dispStepList()
        }

        return root
    }

    override fun onResume() {
        super.onResume()
        if (activity is MainActivity) {
            activity?.title = "游览模式"
        } else activity?.title = "设置步骤位置"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    @SuppressLint("SetTextI18n")
    private fun dispStepList() {
        if (stepList.size <= 0) return

        val dm = DisplayMetrics()
        this.requireActivity().windowManager.defaultDisplay.getMetrics(dm)
        val width = dm.widthPixels
        val height = dm.heightPixels

        for (i in stepList.indices) {
            val step = stepList[i]
            val textView = TextView(this.context)

            textView.id = step.stepno
            textView.text = "${step.stepno} ${step.stepname}"

            textView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_red_72_45, 0, 0)

            val tvParams = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            )
            if (step.posx > 0 && step.posy > 0) {
                if (step.posx > width - 150) tvParams.leftMargin = width - 150 else tvParams.leftMargin = step.posx
                if (step.posy > height - 250) tvParams.topMargin = height - 250 else tvParams.topMargin = step.posy
            } else {
                tvParams.leftMargin = 100
                tvParams.topMargin = 100
            }

            binding.root.addView(textView, tvParams)
            tv_map[textView.id] = textView
//            btn.setOnTouchListener(mBtnListener)
            textView.setOnClickListener {
                viewModel.gotostep(textView.id)
                textView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_yellow_72_45, 0, 0)
            }
        }
    }
}
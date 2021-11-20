package fgd.showroom.ui.guide

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import fgd.showroom.databinding.FragmentGuideBinding

class GuideFragment : Fragment() {

    private lateinit var guideViewModel: GuideViewModel
    private var _binding: FragmentGuideBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        guideViewModel =
            ViewModelProvider(this).get(GuideViewModel::class.java)

        _binding = FragmentGuideBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textDashboard
        guideViewModel.text.observe(viewLifecycleOwner, {
            textView.text = it
        })
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title = "游览模式"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
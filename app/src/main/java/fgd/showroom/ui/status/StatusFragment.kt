package fgd.showroom.ui.status

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import fgd.showroom.databinding.FragmentStatusBinding

class StatusFragment : Fragment() {

    private lateinit var statusViewModel: StatusViewModel
    private var _binding: FragmentStatusBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        statusViewModel =
            ViewModelProvider(this).get(StatusViewModel::class.java)

        _binding = FragmentStatusBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        statusViewModel.text.observe(viewLifecycleOwner, {
////            main_switch.text = it
//        })

        return root
    }

    override fun onStart() {
        super.onStart()
        activity?.title = "状态监控"


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
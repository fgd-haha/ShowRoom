package fgd.showroom.ui.status

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import fgd.showroom.databinding.FragmentStatusBinding

class StatusFragment : Fragment() {

    private lateinit var homeViewModel: StatusViewModel
    private var _binding: FragmentStatusBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel =
            ViewModelProvider(this).get(StatusViewModel::class.java)

        _binding = FragmentStatusBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner, {
            textView.text = it
        })

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
package fgd.showroom.ui.debug

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import fgd.showroom.R
import fgd.showroom.databinding.FragmentDebugBinding


class DebugFragment : Fragment() {

    private lateinit var viewModel: DebugViewModel
    private var _binding: FragmentDebugBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel =
            ViewModelProvider(this).get(DebugViewModel::class.java)

        _binding = FragmentDebugBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val items = listOf("Option 1", "Option 2", "Option 3", "Option 4")
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, items)
        (binding.menu.editText as? AutoCompleteTextView)?.setAdapter(adapter)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title = "调试模式"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
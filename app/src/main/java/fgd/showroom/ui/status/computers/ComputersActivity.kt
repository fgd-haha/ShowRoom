package fgd.showroom.ui.status.computers


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NavUtils
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import fgd.showroom.R
import fgd.showroom.databinding.ActivityComputersBinding
import fgd.showroom.logic.model.DevStatus
import fgd.showroom.ui.observeCommonRpInfo
import kotlin.math.floor


class ComputersActivity : AppCompatActivity() {
    private lateinit var binding: ActivityComputersBinding
    val viewModel by lazy { ViewModelProvider(this)[ComputersViewModel::class.java] }
    private var computerList = listOf<DevStatus>()

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityComputersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarComputer)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.btnComputerPowerOn.setOnClickListener { viewModel.powerAllRequest() }
        binding.btnComputerPowerOff.setOnClickListener { viewModel.shutAllRequest() }
        observeCommonRpInfo(this, this, viewModel.actionResult)

        val layoutManager = GridLayoutManager(this, 1)
        binding.computerRecyclerView.layoutManager = layoutManager
        val adapter = ComputersAdapter(computerList)
        binding.computerRecyclerView.adapter = adapter

        binding.computerRecyclerView.viewTreeObserver.addOnGlobalLayoutListener(
            object : OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    binding.computerRecyclerView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    val viewWidth: Int = binding.computerRecyclerView.measuredWidth
                    val cardViewWidth: Float = this@ComputersActivity.resources.getDimension(R.dimen.computer_view_width)
                    val newSpanCount = floor((viewWidth / cardViewWidth).toDouble()).toInt()
                    layoutManager.spanCount = newSpanCount
                    layoutManager.requestLayout()
                }
            })

        viewModel.computersStatus.observe(this, { result ->
            computerList = result.getOrNull() ?: listOf()
            adapter.setComputerList(computerList)
            adapter.notifyDataSetChanged()
        })
        viewModel.monitorComputersStatus()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                NavUtils.navigateUpFromSameTask(this)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
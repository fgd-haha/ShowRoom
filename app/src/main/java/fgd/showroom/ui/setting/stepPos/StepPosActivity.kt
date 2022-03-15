package fgd.showroom.ui.setting.stepPos

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import fgd.showroom.databinding.ActivityStepPosBinding


class StepPosActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStepPosBinding
    val viewModel by lazy { ViewModelProvider(this)[StepPosViewModel::class.java] }

    private val TAG = "StepPosActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStepPosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarStepPos)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                this.finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
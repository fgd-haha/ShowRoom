package fgd.showroom.ui.setting.action


import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import fgd.showroom.R
import fgd.showroom.databinding.ActivityActionBinding
import fgd.showroom.logic.model.StepAction
import fgd.showroom.ui.debug.DebugFragment


class ActionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityActionBinding
    val viewModel by lazy { ViewModelProvider(this)[ActionViewModel::class.java] }
    var action: StepAction? = null
    private val TAG = "ActionActivity"

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityActionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarAction)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val id = intent.getIntExtra("id", -1)
        val stepno = intent.getIntExtra("stepno", -1)
        val stepidx = intent.getIntExtra("stepidx", 0)
        val devtype = intent.getIntExtra("devtype", -1)
        val actionno = intent.getIntExtra("action", -1)
        val devno = intent.getIntExtra("devno", -1)
        val filename = intent.getStringExtra("filename") ?: ""
        val intv = intent.getIntExtra("intv", 0)

        Log.i(TAG, "id: $id, stepno: $stepno")

        this.action = StepAction(id, stepno, stepidx, devtype, actionno, devno, filename, intv)
        (supportFragmentManager.findFragmentById(R.id.debugFragment) as DebugFragment?)?.setData(this.action!!)
    }


    override fun onDestroy() {
        super.onDestroy()
        action = null
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
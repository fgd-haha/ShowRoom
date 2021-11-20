package fgd.showroom

import android.os.Bundle
import android.view.Gravity
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import fgd.showroom.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var customTitle: AppCompatTextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        customTitle = createCustomTitleTextView()

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_status,
                R.id.navigation_guide,
                R.id.navigation_debug,
                R.id.navigation_setting
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    private fun createCustomTitleTextView(): AppCompatTextView {
        val mTitleTextView = AppCompatTextView(this)
//        TextViewCompat.setTextAppearance(mTitleTextView, null);

        val layoutParams = ActionBar.LayoutParams(
            ActionBar.LayoutParams.WRAP_CONTENT,
            ActionBar.LayoutParams.WRAP_CONTENT
        )
        layoutParams.gravity = Gravity.CENTER
        supportActionBar?.setCustomView(mTitleTextView, layoutParams)
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM

        return mTitleTextView
    }

    override fun setTitle(title: CharSequence?) {
        customTitle.text = title
    }

    override fun setTitle(titleId: Int) {
        customTitle.text = getString(titleId)
    }
}
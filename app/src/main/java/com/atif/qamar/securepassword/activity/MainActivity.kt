package com.atif.qamar.securepassword.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.atif.qamar.securepassword.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(activity_main_toolbar)
        val navController = Navigation.findNavController(this, R.id.fragment)
        NavigationUI.setupWithNavController(nav_view, navController)
        NavigationUI.setupActionBarWithNavController(this, navController, drawer_layout)
        drawerLayout = drawer_layout
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.passwordManagerAddFragment -> {
                    activity_main_toolbar.setTitle(R.string.add_password_title)
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                }
                R.id.passwordMangerFragment -> {
                    activity_main_toolbar.setTitle(R.string.password_manager_title)
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                }

                R.id.passwordManagerEditFragment -> {
                    activity_main_toolbar.setTitle(R.string.password_manager_edit_title)
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                }

                R.id.passwordChangeFragment -> {
                    activity_main_toolbar.setTitle(R.string.password_update)
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                }

                R.id.passwordGeneratorFragment -> {
                    activity_main_toolbar.setTitle(R.string.passwordGen)
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                }

            }
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(
            Navigation.findNavController(this, R.id.fragment),
            drawer_layout
        )
    }
}

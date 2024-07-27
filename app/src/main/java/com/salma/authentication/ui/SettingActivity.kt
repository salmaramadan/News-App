package com.salma.authentication.ui

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.salma.authentication.R

class SettingsActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        bottomNavigationView = findViewById(R.id.bottom_navigation_setting)
        bottomNavigationView.selectedItemId = R.id.nav_favorites

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, HomeActivity::class.java))
                    true
                }
                R.id.nav_favorites -> {
                    startActivity(Intent(this, FavoritesActivity::class.java))
                    true
                }
                R.id.nav_settings -> {
                    startActivity(Intent(this, SettingsActivity::class.java))
                    true
                }
                else -> false
            }
        }

        // Setup Toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Setup ListView
        val settingsList: ListView = findViewById(R.id.settings_list)
        val options = listOf("Logout", "About Us", "Switch Account", "Add Account")
        val adapter = ArrayAdapter(this, R.layout.item_setting_option, R.id.setting_option, options)
        settingsList.adapter = adapter

        settingsList.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            when (options[position]) {
                "Logout" -> navigateToSignIn()
                "About Us" -> navigateToAboutUs()
                "Switch Account" -> navigateToSwitchAccount()
                "Add Account" -> navigateToAddAccount()
            }
        }
    }

    private fun navigateToSignIn() {
        startActivity(Intent(this, SignInActivity::class.java))
        finish() // Optionally finish SettingsActivity
    }

    private fun navigateToAboutUs() {
        startActivity(Intent(this, AboutUsActivity::class.java))
    }

    private fun navigateToSwitchAccount() {
        startActivity(Intent(this, SignInActivity::class.java))
    }

    private fun navigateToAddAccount() {
        startActivity(Intent(this, SignInActivity::class.java))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                startActivity(Intent(this, HomeActivity::class.java))
                finish() // Optionally finish SettingsActivity
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}

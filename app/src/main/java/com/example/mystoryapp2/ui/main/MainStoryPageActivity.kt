package com.example.mystoryapp2.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mystoryapp2.R
import com.example.mystoryapp2.data.remote.DataStoryRepository
import com.example.mystoryapp2.databinding.ActivityMainBinding
import com.example.mystoryapp2.ui.StoryViewModelFactory
import com.example.mystoryapp2.ui.addStory.AddStoryActivity
import com.example.mystoryapp2.ui.login.LoginActivity
import com.example.mystoryapp2.ui.map.MapsActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {
    private lateinit var mainStoryPageViewModel: MainStoryPageViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var storyViewModel: StoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.main_title)

        val layoutManager = LinearLayoutManager(this)
        binding.rvAccount.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvAccount.addItemDecoration(itemDecoration)

        checkLogin()
        getAllStories()
    }

    override fun onBackPressed() {
        val startMain = Intent(Intent.ACTION_MAIN)
        startMain.addCategory(Intent.CATEGORY_HOME)
        startMain.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(startMain)
    }

    private fun checkLogin() {
        mainStoryPageViewModel = ViewModelProvider(
            this,
            StoryViewModelFactory(DataStoryRepository.getInstance(dataStore))
        )[MainStoryPageViewModel::class.java]

        mainStoryPageViewModel.getStatus().observe(this) { status ->
            if (!status.isLogin) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
    }

    private fun getAllStories() {
        mainStoryPageViewModel.getStatus().observe(this) { status ->
            Log.d("Token", status.token)
            storyViewModel = ViewModelProvider(
                this,
                StoryViewModelFactory(this, "Bearer ${status.token}")
            )[StoryViewModel::class.java]

            val adapter = ListStoriesPageAdapter()
            binding.rvAccount.adapter = adapter
            storyViewModel.story.observe(this) {
                adapter.submitData(lifecycle, it)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu) : Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.item_option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add -> {
                val intentToAdd = Intent(this@MainActivity, AddStoryActivity::class.java)
                startActivity(intentToAdd)
            }
            R.id.map -> {
                val intentToMap = Intent(this, MapsActivity::class.java)
                startActivity(intentToMap)
            }
            R.id.logOut -> {
                mainStoryPageViewModel.logout()
                val intentToLogOut = Intent(this, LoginActivity::class.java)
                startActivity(intentToLogOut)
                finish()
            }
        }
        return true
    }

    companion object {
        const val STATUS_LOGIN = "login"
    }
}
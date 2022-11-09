package com.example.mystoryapp2.ui.map

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.mystoryapp2.R
import com.example.mystoryapp2.data.local.StoryDataClass
import com.example.mystoryapp2.data.remote.DataStoryRepository
import com.example.mystoryapp2.data.remote.response.AllStoriesWithLocation
import com.example.mystoryapp2.data.remote.retrofit.ApiConfigRetrofit
import com.example.mystoryapp2.databinding.ActivityMapsBinding
import com.example.mystoryapp2.ui.StoryViewModelFactory
import com.example.mystoryapp2.ui.main.MainStoryPageViewModel
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var mainStoryPageViewModel: MainStoryPageViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        getStoryLocation()
    }

    private fun getStoryLocation() {
        mainStoryPageViewModel = ViewModelProvider(
            this,
            StoryViewModelFactory(DataStoryRepository.getInstance(dataStore))
        )[MainStoryPageViewModel::class.java]

        mainStoryPageViewModel.getStatus().observe(this) { status ->
            val listStories = ArrayList<StoryDataClass>()
            val client = ApiConfigRetrofit.getApiService().getStoriesWithLocation("Bearer ${status.token}")
            client.enqueue(object : Callback<AllStoriesWithLocation> {
                override fun onResponse(
                    call: Call<AllStoriesWithLocation>,
                    response: Response<AllStoriesWithLocation>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null) {
                            responseBody.listStory.forEach { account ->
                                val storyDataClass = StoryDataClass(
                                    account.name,
                                    account.photoUrl,
                                    account.createdAt,
                                    account.description,
                                    account.lat,
                                    account.lon
                                )
                                listStories.add(storyDataClass)

                                mMap.addMarker(
                                    MarkerOptions()
                                        .position(LatLng(account.lat, account.lon))
                                        .title(account.name)
                                        .snippet(account.description)
                                )
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<AllStoriesWithLocation>, t: Throwable) {
                    Log.e("Error", t.message.toString())
                }
            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.map_options_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.normal_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
                true
            }
            R.id.satellite_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
                true
            }
            R.id.terrain_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
                true
            }
            R.id.hybrid_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_HYBRID
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getMyLocation()
            }
        }

    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
        } else {
            requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }
}
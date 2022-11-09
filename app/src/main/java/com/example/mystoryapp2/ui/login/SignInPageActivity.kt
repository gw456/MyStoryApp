package com.example.mystoryapp2.ui.login

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.mystoryapp2.data.local.StatusDataClass
import com.example.mystoryapp2.data.remote.DataStoryRepository
import com.example.mystoryapp2.data.remote.response.LoginResponse
import com.example.mystoryapp2.data.remote.retrofit.ApiConfigRetrofit
import com.example.mystoryapp2.databinding.ActivityLoginBinding
import com.example.mystoryapp2.ui.StoryViewModelFactory
import com.example.mystoryapp2.ui.main.MainActivity
import com.example.mystoryapp2.ui.signup.SignUpPageActivity
import com.example.mystoryapp2.ui.signup.SignUpPageActivity.Companion.EMAIL
import com.example.mystoryapp2.ui.signup.SignUpPageActivity.Companion.PASSWORD
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class LoginActivity : AppCompatActivity() {
    private lateinit var signInPageViewModel: SignInPageViewModel
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        signInPageViewModel = ViewModelProvider(
            this,
            StoryViewModelFactory(DataStoryRepository.getInstance(dataStore))
        )[SignInPageViewModel::class.java]

        setupActionLogin()
        setupActionRegister()
    }

    private fun setupActionLogin() {

        val emailRegistered = intent.getStringExtra(EMAIL)
        val passwordRegistered = intent.getStringExtra(PASSWORD)

        if (emailRegistered != null) {
            binding.emailEditText.setText(emailRegistered)
        }

        if (passwordRegistered != null) {
            binding.passwordEditText.setText(passwordRegistered)
        }

        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            when {
                email.isEmpty() -> {
                    binding.emailEditTextLayout.error = "Masukkan email"
                }
                password.isEmpty() -> {
                    binding.passwordEditTextLayout.error = "Masukkan password"
                }
                else -> {
                    val client = ApiConfigRetrofit.getApiService().loginStory(email, password)
                    client.enqueue(object : Callback<LoginResponse> {
                        override fun onResponse(
                            call: Call<LoginResponse>,
                            response: Response<LoginResponse>
                        ) {
                            if (response.isSuccessful) {
                                val responseBody = response.body()
                                if (responseBody != null) {
                                    signInPageViewModel.saveStatus(StatusDataClass(responseBody.loginResult.token, true))
                                    val intentToken = Intent(this@LoginActivity, MainActivity::class.java)
                                    startActivity(intentToken)
                                    finish()
                                }
                            } else {
                                response.body()?.let { it1 -> Log.d(TOKEN, it1.message) }
                            }
                        }

                        override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                            Log.d(TOKEN, t.message.toString())
                        }
                    })
                }
            }
        }
    }

    private fun setupActionRegister() {
        binding.registerButton.setOnClickListener {
            startActivity(Intent(this, SignUpPageActivity::class.java))
        }
    }

    companion object {
        const val TOKEN = "token"
    }
}
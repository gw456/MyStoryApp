package com.example.mystoryapp2.ui.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.example.mystoryapp2.data.remote.response.SignUpRetrofitResponse
import com.example.mystoryapp2.data.remote.retrofit.ApiConfigRetrofit
import com.example.mystoryapp2.databinding.ActivitySignupBinding
import com.example.mystoryapp2.ui.login.LoginActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpPageActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAction()
    }

    private fun setupAction() {
        binding.signupButton.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            when {
                name.isEmpty() -> {
                    binding.nameEditTextLayout.error = "Masukkan nama"
                }
                email.isEmpty() -> {
                    binding.emailEditTextLayout.error = "Masukkan email"
                }
                password.isEmpty() -> {
                    binding.passwordEditTextLayout.error = "Masukkan password"
                }
                else -> {
                    val client = ApiConfigRetrofit.getApiService().registerStory(name, email, password)
                    client.enqueue(object : Callback<SignUpRetrofitResponse> {
                        override fun onResponse(
                            call: Call<SignUpRetrofitResponse>,
                            response: Response<SignUpRetrofitResponse>
                        ) {
                            if (response.isSuccessful) {
                                val responseBody = response.body()
                                if (responseBody != null) {
                                    if (!responseBody.error) {
                                        AlertDialog.Builder(this@SignUpPageActivity).apply {
                                            setTitle("Yeah!")
                                            setMessage("Akunnya sudah jadi nih. Yuk, lihat dan bagikan ceritamu.")
                                            setPositiveButton("Lanjut") { _, _ ->
                                                finish()
                                            }
                                            create()
                                            show()
                                        }
                                        val intentRegister = Intent(this@SignUpPageActivity,
                                            LoginActivity::class.java)
                                        intentRegister.putExtra(EMAIL, email)
                                        intentRegister.putExtra(PASSWORD, password)
                                        startActivity(intentRegister)
                                    }
                                }
                            }
                        }

                        override fun onFailure(call: Call<SignUpRetrofitResponse>, t: Throwable) {

                        }

                    })
                }
            }
        }
    }

    companion object {
        const val EMAIL = "email"
        const val PASSWORD = "password"
    }
}
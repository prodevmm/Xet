package com.bllsht.xet3demo

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bllsht.xet3.Xet
import com.bllsht.xet3.dto.Response
import com.bllsht.xet3.exceptions.XetException
import com.bllsht.xet3demo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btn.setOnClickListener { fetchUsingXet() }


    }

    private fun fetchUsingXet() {
        val url = binding.edt.text.toString()

        Xet.fetch(url, object : Xet.Callback() {
            override fun onSuccess(response: Response) {
                val message =
                    "SD URL : ${response.sdUrl}\n\nHD Available : ${response.isHDAvailable()}\nHD URL : ${response.hdUrl}\n\nHLS Available : ${response.isHlsAvailable()}\nHLS URL : ${response.hlsUrl}"

                AlertDialog.Builder(this@MainActivity)
                    .setMessage(message)
                    .setPositiveButton(android.R.string.ok, null)
                    .show()
            }

            override fun onFailure(exception: XetException) {
                AlertDialog.Builder(this@MainActivity)
                    .setMessage(exception.toString())
                    .setPositiveButton(android.R.string.ok, null)
                    .show()
            }
        })
    }
}
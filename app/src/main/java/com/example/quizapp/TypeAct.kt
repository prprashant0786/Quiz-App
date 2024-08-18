package com.example.quizapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import com.example.quizapp.databinding.ActivityFinishBinding
import com.example.quizapp.databinding.ActivityTypeBinding

class TypeAct : AppCompatActivity(), View.OnClickListener {

    private var binding : ActivityTypeBinding? = null
    var mUsername : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTypeBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        mUsername = intent.getStringExtra(Constant.USER_NAME)

        binding?.tvmysql?.setOnClickListener(this)
        binding?.tvwordpress?.setOnClickListener(this)
        binding?.tvdevops?.setOnClickListener(this)
        binding?.tvbash?.setOnClickListener(this)
        binding?.tvhtml?.setOnClickListener(this)
        binding?.tvjavascript?.setOnClickListener(this)
        binding?.tvlinux?.setOnClickListener(this)
        binding?.tvphp?.setOnClickListener(this)

        binding?.tvhard?.setOnClickListener(this)
        binding?.tveasy?.setOnClickListener(this)
        binding?.tvmedium?.setOnClickListener(this)

    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

    override fun onClick(view: View?) {
        val intent = Intent(this,questionact::class.java)
        intent.putExtra(Constant.USER_NAME,mUsername)

        when (view?.id) {

            R.id.tvmysql -> {
                intent.putExtra(Constant.QUESTION_CATEGORY, "MySQL")
                binding?.lltype?.isVisible = false
                binding?.llmed?.isVisible = true
            }
            R.id.tvphp -> {
                intent.putExtra(Constant.QUESTION_CATEGORY, "PHP")
                binding?.lltype?.isVisible = false
                binding?.llmed?.isVisible = true
            }
            R.id.tvhtml -> {
                intent.putExtra(Constant.QUESTION_CATEGORY, "HTML")
                binding?.lltype?.isVisible = false
                binding?.llmed?.isVisible = true
            }
            R.id.tvbash -> {
                intent.putExtra(Constant.QUESTION_CATEGORY, "BASH")
                binding?.lltype?.isVisible = false
                binding?.llmed?.isVisible = true
            }
            R.id.tvdevops -> {
                intent.putExtra(Constant.QUESTION_CATEGORY, "DevOps")
                binding?.lltype?.isVisible = false
                binding?.llmed?.isVisible = true
            }
            R.id.tvjavascript -> {
                intent.putExtra(Constant.QUESTION_CATEGORY, "JavaScript")
                binding?.lltype?.isVisible = false
                binding?.llmed?.isVisible = true
            }
            R.id.tvlinux -> {
                intent.putExtra(Constant.QUESTION_CATEGORY, "Linux")
                binding?.lltype?.isVisible = false
                binding?.llmed?.isVisible = true
            }
            R.id.tvwordpress -> {
                intent.putExtra(Constant.QUESTION_CATEGORY, "WordPress")
                binding?.lltype?.isVisible = false
                binding?.llmed?.isVisible = true
            }

            R.id.tveasy -> {
                intent.putExtra(Constant.QUESTION_MEDIUM, "Easy")
                startActivity(intent)
                finish()
            }
            R.id.tvmedium -> {
                intent.putExtra(Constant.QUESTION_MEDIUM, "Medium")
                startActivity(intent)
                finish()
            }
            R.id.tvhard -> {
                intent.putExtra(Constant.QUESTION_MEDIUM, "Hard")
                startActivity(intent)
                finish()
            }
        }
    }
}
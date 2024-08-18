package com.example.quizapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.quizapp.databinding.ActivityFinishBinding
import com.example.quizapp.databinding.ActivityQuestionactBinding

class FinishAct : AppCompatActivity() {

    private var binding : ActivityFinishBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFinishBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val correct = intent.getStringExtra(Constant.CORRECT_ANSWER)

        binding?.tvName?.text = intent.getStringExtra(Constant.USER_NAME)

        binding?.tvScore?.text = "Your Score is ${correct} out of 10"

        binding?.btnfinish?.setOnClickListener{
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }
}
package com.example.quizapp

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.example.quizapp.Constant
import com.example.quizapp.Model.CorrectAnswers
import com.example.quizapp.Model.ListItem
import com.example.quizapp.Network.Apiinterface
import com.example.quizapp.databinding.ActivityQuestionactBinding
import retrofit.*

class questionact : AppCompatActivity(), View.OnClickListener {

    private var binding: ActivityQuestionactBinding? = null
    private var mProgressDialog: Dialog? = null

    private var currpos = 1;
    private var mquestionlist: ArrayList<ListItem>? = null
    private var selectedopt: Int = 0

    var cat: String? = null
    var diff: String? = null
    val lim: Int = 10;

    var correct : Int = 0;
    var mUsername : String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuestionactBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        cat = intent.getStringExtra(Constant.QUESTION_CATEGORY)
        diff = intent.getStringExtra(Constant.QUESTION_MEDIUM)
        mUsername = intent.getStringExtra(Constant.USER_NAME)

        getquestiondata()

        binding?.tvOptionOne?.setOnClickListener(this)
        binding?.tvOptionTwo?.setOnClickListener(this)
        binding?.tvOptionThree?.setOnClickListener(this)
        binding?.tvOptionFour?.setOnClickListener(this)

        binding?.btnSubmit?.setOnClickListener(this)
    }

    private fun getquestiondata() {
        if (Constant.isNetworkAvailable(this@questionact)) {

            val retrofit: Retrofit = Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val services: Apiinterface = retrofit.create(Apiinterface::class.java)

            val questionlist: Call<ArrayList<ListItem>> = services.getquestion(
                Constant.APP_ID,
                cat, diff, lim
            )


            showCustomProgressDialog()

            questionlist.enqueue(object : Callback<ArrayList<ListItem>> {
                @RequiresApi(Build.VERSION_CODES.N)
                override fun onResponse(
                    response: Response<ArrayList<ListItem>>?,
                    retrofit: Retrofit?
                ) {
                    if (response!!.isSuccess) {
                        hideProgressDialog()
                        mquestionlist = response.body()
                        setquestion()
                    } else {
                        // If the response is not success then we check the response code.
                        val sc = response.code()

                        when (sc) {
                            400 -> {
                                Log.e("Error 400", "Bad Request")
                            }
                            404 -> {
                                Log.e("Error 404", "Not Found")
                            }
                            else -> {
                                Log.e("Error", "Generic Error")
                            }
                        }
                    }
                }

                override fun onFailure(t: Throwable?) {
                    TODO("Not yet implemented")
                }
            })
        } else {
            Toast.makeText(
                this@questionact,
                "No internet connection available.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

    private fun setquestion() {
        val questionno = mquestionlist!![currpos-1]
        maindOptionsView()

        if (currpos == mquestionlist!!.size) {
            binding?.btnSubmit?.text = "FINISH"
        } else {
            binding?.btnSubmit?.text = "SUBMIT"
        }

        binding?.tvOptionThree?.isVisible = true
        binding?.tvOptionFour?.isVisible = true


        binding?.progressBar?.progress = currpos
        binding?.tvProgress?.text = "$currpos" + "/" + binding?.progressBar?.max

        binding?.tvQuestion?.text = questionno.question
        binding?.tvOptionOne?.text = questionno.answers.answer_a
        binding?.tvOptionTwo?.text = questionno.answers.answer_b
        if(questionno.answers.answer_c == null){
            binding?.tvOptionThree?.isVisible = false
        }
        else binding?.tvOptionThree?.text = questionno.answers.answer_c
        if(questionno.answers.answer_d == null){
            binding?.tvOptionFour?.isVisible = false
        }
        else binding?.tvOptionFour?.text = questionno.answers.answer_d
    }

    private fun showCustomProgressDialog() {
        mProgressDialog = Dialog(this)

        mProgressDialog!!.setContentView(R.layout.dialog_custome_progress)

        //Start the dialog and display it on screen.
        mProgressDialog!!.show()
    }

    private fun hideProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog!!.dismiss()
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {

            R.id.tv_option_one -> {
                binding?.tvOptionOne?.let {
                    selectedOptionView(it, 1)
                }
            }

            R.id.tv_option_two -> {
                binding?.tvOptionTwo?.let {
                    selectedOptionView(it, 2)
                }

            }

            R.id.tv_option_three -> {
                binding?.tvOptionThree?.let {
                    selectedOptionView(it, 3)
                }

            }

            R.id.tv_option_four -> {
                binding?.tvOptionFour?.let {
                    selectedOptionView(it, 4)
                }
            }

            R.id.btn_submit -> {
                if (selectedopt == 0) {
                    currpos++

                    when {
                        currpos <= mquestionlist!!.size -> {
                            setquestion()
                        }
                        else -> run {
                            val intent = Intent(this, FinishAct::class.java)
                            intent.putExtra(Constant.USER_NAME,mUsername)
                            intent.putExtra(Constant.CORRECT_ANSWER,correct.toString())
                            startActivity(intent)
                            finish()
                        }

                    }
                } else {
                    var ans  = mquestionlist?.get(currpos-1)?.correct_answers

                    var currans = 0

                    if(ans?.answer_a_correct.equals("true")) currans = 1;
                    else if(ans?.answer_b_correct.equals("true")) currans = 2;
                    else if(ans?.answer_c_correct.equals("true")) currans = 3;
                    else if(ans?.answer_d_correct.equals("true")) currans = 4;




                    // This is to check if the answer is wrong
                    if (currans!=selectedopt) {
                        answerView(selectedopt, R.drawable.wrong_option_border_bg)
                    }
                    else correct++;

                    // This is for correct answer
                    answerView(currans, R.drawable.correct_option_border_bg)

                    if (currpos == mquestionlist!!.size) {
                        binding?.btnSubmit?.text = "FINISH"
                    } else {
                        binding?.btnSubmit?.text = "GO TO NEXT QUESTION"
                    }

                    selectedopt = 0
                }
            }
        }
    }

    private fun answerView(answer: Int, drawableView: Int) {

        when (answer) {

            1 -> {
                binding?.tvOptionOne?.background = ContextCompat.getDrawable(
                    this@questionact,
                    drawableView
                )
            }
            2 -> {
                binding?.tvOptionTwo?.background = ContextCompat.getDrawable(
                    this@questionact,
                    drawableView
                )
            }
            3 -> {
                binding?.tvOptionThree?.background = ContextCompat.getDrawable(
                    this@questionact,
                    drawableView
                )
            }
            4 -> {
                binding?.tvOptionFour?.background = ContextCompat.getDrawable(
                    this@questionact,
                    drawableView
                )
            }
        }
    }

    private fun selectedOptionView(tv: TextView, selectedOptionNum: Int) {

        defaultOptionsView()
        selectedopt = selectedOptionNum

        tv.setTextColor(
            Color.parseColor("#363A43")
        )
        tv.setTypeface(tv.typeface, Typeface.BOLD)
        tv.background = ContextCompat.getDrawable(
            this@questionact,
            R.drawable.selected_option_border_bg
        )
    }
    private fun defaultOptionsView() {

        val options = ArrayList<TextView>()
        binding?.tvOptionOne?.let {

            options.add(0, it)
        }
        binding?.tvOptionTwo?.let {
            options.add(1, it)
        }
        binding?.tvOptionThree?.let {
            options.add(2, it)
        }
        binding?.tvOptionFour?.let {
            options.add(3, it)
        }

        for (option in options) {
            option.setTextColor(Color.parseColor("#7A8089"))
            option.typeface = Typeface.DEFAULT
            option.background = ContextCompat.getDrawable(
                this@questionact,
                R.drawable.default_option_border_bg
            )
        }
    }
    private fun maindOptionsView() {

        val options = ArrayList<TextView>()
        binding?.tvOptionOne?.let {

            options.add(0, it)
        }
        binding?.tvOptionTwo?.let {
            options.add(1, it)
        }
        binding?.tvOptionThree?.let {
            options.add(2, it)
        }
        binding?.tvOptionFour?.let {
            options.add(3, it)
        }

        for (option in options) {
            option.setTextColor(Color.parseColor("#212121"))
            option.setTypeface(option.typeface, Typeface.BOLD)
            option.background = ContextCompat.getDrawable(
                this@questionact,
                R.drawable.default_option_border_bg
            )
        }
    }
}
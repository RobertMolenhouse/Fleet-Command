package com.bobmolenhouse.scorerealms

import android.os.CountDownTimer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds

import java.util.Random

class ScoreBoard : AppCompatActivity() {
    private var player1ScoreView: TextView? = null
    private var player2ScoreView: TextView? = null
    private var player1Score: Int = 0
    private var player2Score: Int = 0

    private var player1ScoreChange: TextView? = null
    private var player2ScoreChange: TextView? = null

    private var player1Plus: Button? = null
    private var player1Minus: Button? = null
    private var player2Plus: Button? = null
    private var player2Minus: Button? = null

    private var timer: CountDownTimer? = null
    private var scoreChange1 = 0
    private var scoreChange2 = 0

    private var mAdView: AdView? = null

    //build click listener.
    private val clickListener = View.OnClickListener { v ->

        //cancel the timer every time a button is clicked, because, when making clicks
        //in rapid succession, we will start the timer again after each click so
        //we always have 1.5 seconds of change score displayed after last click.
        timer!!.cancel()

        //figure out which button was clicked, and update accordingly.
        when (v.id) {
            R.id.player1Plus -> {
                player1Score += 1
                scoreChange1 += 1
                if (scoreChange1 >= 0) {
                    player1ScoreChange!!.text = "+ " + scoreChange1
                } else {
                    player1ScoreChange!!.text = "" + scoreChange1
                }
                timer!!.start()
                player1ScoreView!!.text = "" + player1Score
            }

            R.id.player1Minus -> {
                if (player1Score > 0) {
                    player1Score -= 1
                    scoreChange1 -= 1
                } else {
                }

                if (scoreChange1 >= 0) {
                    player1ScoreChange?.text = "+ " + scoreChange1
                } else if (player1Score == 0) {
                    player1ScoreChange?.text = "You Dead"
                } else {
                    player1ScoreChange?.text = "" + scoreChange1
                }
                timer!!.start()
                player1ScoreView!!.text = "" + player1Score
            }

            R.id.player2Plus -> {
                player2Score += 1
                scoreChange2 += 1
                if (scoreChange2 >= 0) {
                    player2ScoreChange!!.text = "+ " + scoreChange2

                } else {
                    player2ScoreChange!!.text = "" + scoreChange2
                }
                timer!!.start()
                player2ScoreView!!.text = "" + player2Score
            }

            R.id.player2Minus -> {
                if (player2Score > 0) {
                    player2Score -= 1
                    scoreChange2 -= 1
                } else {
                }

                if (scoreChange2 >= 0) {
                    player2ScoreChange!!.text = "+ " + scoreChange2
                } else if (player2Score == 0) {
                    player2ScoreChange!!.text = "You Dead"
                } else {
                    player2ScoreChange!!.text = "" + scoreChange2
                }

                //now we start timer
                timer!!.start()
                player2ScoreView!!.text = "" + player2Score
            }
        }

        //update background colors.
        updateScoreBackground(player1Score, player2Score)
    }

    /**
     * on Create builds the scoreboard view.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_score_board)

        //initialize the banner ads
        MobileAds.initialize(this, "ca-app-pub-6935583766445792~6470879865")

        mAdView = findViewById(R.id.adView) as AdView
        val adRequest = AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build()
        mAdView?.loadAd(adRequest)

        //initialize values and what not
        player1ScoreView = findViewById(R.id.player1Score) as TextView
        player2ScoreView = findViewById(R.id.player2Score) as TextView
        player1ScoreChange = findViewById(R.id.player1_score_change) as TextView
        player2ScoreChange = findViewById(R.id.player2_score_change) as TextView

        //starting score set to 50. TODO make a menu option to set custom value
        player1Score = 50
        player2Score = 50

        //set button listeners
        player1Plus = findViewById(R.id.player1Plus) as Button
        player1Plus!!.setOnClickListener(clickListener)
        player1Minus = findViewById(R.id.player1Minus) as Button
        player1Minus!!.setOnClickListener(clickListener)
        player2Plus = findViewById(R.id.player2Plus) as Button
        player2Plus!!.setOnClickListener(clickListener)
        player2Minus = findViewById(R.id.player2Minus) as Button
        player2Minus!!.setOnClickListener(clickListener)

        //countdown timer used to manage display of score change text view
        timer = object : CountDownTimer(1500, 100) {
            override fun onTick(l: Long) {

            }

            override fun onFinish() {
                player2ScoreChange!!.text = ""
                player1ScoreChange!!.text = ""
                scoreChange1 = 0
                scoreChange2 = 0
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.new_game -> {
                player1Score = 50
                player2Score = 50
                player1ScoreView!!.text = "" + player1Score
                player2ScoreView!!.text = "" + player2Score

                updateScoreBackground(player1Score, player2Score)
                return true
            }

            R.id.who_goes_first -> {
                val r = Random()
                timer!!.start()
                if (r.nextInt(10) > 4) {
                    player1ScoreChange!!.text = "I go first!"
                } else {
                    player2ScoreChange!!.text = "I go first!"
                }
                return true
            }

            //when master control is selected, flip player1 180 deg, virtically
            R.id.master_control -> {
                val l = findViewById(R.id.player1) as LinearLayout
                l.rotation = 0f
                return true
            }

            //when individual control, flip player1 to be upside down.
            R.id.individual_control -> {
                val i = findViewById(R.id.player1) as LinearLayout
                i.rotation = 180f
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun updateScoreBackground(p1 : Int, p2 : Int){

        if(p1 > 25) {
            player1ScoreView!!.setBackgroundResource(R.drawable.g)
            player1ScoreChange!!.setBackgroundResource(R.drawable.gt)
            player1Plus!!.setBackgroundResource(R.drawable.plus_button_press)
            player1Minus!!.setBackgroundResource(R.drawable.minus_button_press)
        }
        else if(p1 in 11..25) {
            player1ScoreView!!.setBackgroundResource(R.drawable.y)
            player1ScoreChange!!.setBackgroundResource(R.drawable.yt)
            player1Plus!!.setBackgroundResource(R.drawable.plus_button_press_y)
            player1Minus!!.setBackgroundResource(R.drawable.minus_button_press_y)
        }
        else {
            player1ScoreView!!.setBackgroundResource(R.drawable.r)
            player1ScoreChange!!.setBackgroundResource(R.drawable.rt)
            player1Plus!!.setBackgroundResource(R.drawable.plus_button_press_r)
            player1Minus!!.setBackgroundResource(R.drawable.minus_button_press_r)
        }

        if(p2 > 25) {
            player2ScoreView!!.setBackgroundResource(R.drawable.g)
            player2ScoreChange!!.setBackgroundResource(R.drawable.gt)
            player2Plus!!.setBackgroundResource(R.drawable.plus_button_press)
            player2Minus!!.setBackgroundResource(R.drawable.minus_button_press)
        }
        else if(p2 in 11..25) {
            player2ScoreView!!.setBackgroundResource(R.drawable.y)
            player2ScoreChange!!.setBackgroundResource(R.drawable.yt)
            player2Plus!!.setBackgroundResource(R.drawable.plus_button_press_y)
            player2Minus!!.setBackgroundResource(R.drawable.minus_button_press_y)
        }
        else {
            player2ScoreView!!.setBackgroundResource(R.drawable.r)
            player2ScoreChange!!.setBackgroundResource(R.drawable.rt)
            player2Plus!!.setBackgroundResource(R.drawable.plus_button_press_r)
            player2Minus!!.setBackgroundResource(R.drawable.minus_button_press_r)
        }
    }


}
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

    private val clickListener = View.OnClickListener { v ->
        timer!!.cancel()
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
                    player1ScoreChange!!.text = "+ " + scoreChange1
                } else if (player1Score == 0) {
                    player1ScoreChange!!.text = "You Dead"
                } else {
                    player1ScoreChange!!.text = "" + scoreChange1
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
                timer!!.start()
                player2ScoreView!!.text = "" + player2Score
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_score_board)

        //initialize the banner ads
        MobileAds.initialize(this, "ca-app-pub-6935583766445792~6470879865")

        mAdView = findViewById(R.id.adView) as AdView
        val adRequest = AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build()
        mAdView!!.loadAd(adRequest)

        //initialize values and what not
        player1ScoreView = findViewById(R.id.player1Score) as TextView
        player2ScoreView = findViewById(R.id.player2Score) as TextView
        player1ScoreChange = findViewById(R.id.player1_score_change) as TextView
        player2ScoreChange = findViewById(R.id.player2_score_change) as TextView
        player1Score = 50
        player2Score = 50
        player1Plus = findViewById(R.id.player1Plus) as Button
        player1Plus!!.setOnClickListener(clickListener)
        player1Minus = findViewById(R.id.player1Minus) as Button
        player1Minus!!.setOnClickListener(clickListener)
        player2Plus = findViewById(R.id.player2Plus) as Button
        player2Plus!!.setOnClickListener(clickListener)
        player2Minus = findViewById(R.id.player2Minus) as Button
        player2Minus!!.setOnClickListener(clickListener)

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

            R.id.master_control -> {
                val l = findViewById(R.id.player1) as LinearLayout
                l.rotation = 0f
                return true
            }

            R.id.individual_control -> {
                val i = findViewById(R.id.player1) as LinearLayout
                i.rotation = 180f
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


}
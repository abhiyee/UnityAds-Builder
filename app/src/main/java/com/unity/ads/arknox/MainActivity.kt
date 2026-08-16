package com.unity.ads.arknox

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.unity3d.ads.IUnityAdsInitializationListener
import com.unity3d.ads.IUnityAdsLoadListener
import com.unity3d.ads.IUnityAdsShowListener
import com.unity3d.ads.UnityAds
import com.unity3d.ads.UnityAds.UnityAdsInitializationError
import com.unity3d.ads.UnityAds.UnityAdsLoadError
import com.unity3d.ads.UnityAds.UnityAdsShowCompletionState
import com.unity3d.ads.UnityAds.UnityAdsShowError

class MainActivity : AppCompatActivity() {

    private val GAME_ID = "6041916"
    private val INTERSTITIAL_AD_UNIT = "Interstitial_Android"
    private val REWARDED_AD_UNIT     = "Rewarded_Android"
    private val TEST_MODE = true
    private var isInterstitialLoaded = false
    private var isRewardedLoaded     = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initUnityAds()

        findViewById<Button>(R.id.btnInterstitial).setOnClickListener {
            showInterstitialAd()
        }

        findViewById<Button>(R.id.btnRewarded).setOnClickListener {
            showRewardedAd()
        }
    }

    // ── Unity Ads ─────────────────────────────────────────────────────────────

    private fun initUnityAds() {
        UnityAds.initialize(applicationContext, GAME_ID, TEST_MODE,
            object : IUnityAdsInitializationListener {
                override fun onInitializationComplete() {
                    loadInterstitialAd()
                    loadRewardedAd()
                }

                override fun onInitializationFailed(
                    error: UnityAdsInitializationError,
                    message: String
                ) {
                    runOnUiThread {
                        Toast.makeText(
                            this@MainActivity,
                            "Unity Ads init failed: $message",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        )
    }

    private fun loadInterstitialAd() {
        UnityAds.load(INTERSTITIAL_AD_UNIT, object : IUnityAdsLoadListener {
            override fun onUnityAdsAdLoaded(placementId: String) {
                isInterstitialLoaded = true
            }

            override fun onUnityAdsFailedToLoad(
                placementId: String,
                error: UnityAdsLoadError,
                message: String
            ) {
                isInterstitialLoaded = false
            }
        })
    }

    private fun loadRewardedAd() {
        UnityAds.load(REWARDED_AD_UNIT, object : IUnityAdsLoadListener {
            override fun onUnityAdsAdLoaded(placementId: String) {
                isRewardedLoaded = true
            }

            override fun onUnityAdsFailedToLoad(
                placementId: String,
                error: UnityAdsLoadError,
                message: String
            ) {
                isRewardedLoaded = false
            }
        })
    }

    private fun showInterstitialAd() {
        if (!isInterstitialLoaded) {
            Toast.makeText(this, "Interstitial ad not ready yet...", Toast.LENGTH_SHORT).show()
            loadInterstitialAd()
            return
        }

        UnityAds.show(this, INTERSTITIAL_AD_UNIT, object : IUnityAdsShowListener {
            override fun onUnityAdsShowFailure(
                placementId: String,
                error: UnityAdsShowError,
                message: String
            ) {
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "Ad failed: $message", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onUnityAdsShowStart(placementId: String) {}

            override fun onUnityAdsShowClick(placementId: String) {}

            override fun onUnityAdsShowComplete(
                placementId: String,
                state: UnityAdsShowCompletionState
            ) {
                isInterstitialLoaded = false
                loadInterstitialAd()
            }
        })
    }

    private fun showRewardedAd() {
        if (!isRewardedLoaded) {
            Toast.makeText(this, "Rewarded ad not ready yet...", Toast.LENGTH_SHORT).show()
            loadRewardedAd()
            return
        }

        UnityAds.show(this, REWARDED_AD_UNIT, object : IUnityAdsShowListener {
            override fun onUnityAdsShowFailure(
                placementId: String,
                error: UnityAdsShowError,
                message: String
            ) {
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "Ad failed: $message", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onUnityAdsShowStart(placementId: String) {}

            override fun onUnityAdsShowClick(placementId: String) {}

            override fun onUnityAdsShowComplete(
                placementId: String,
                state: UnityAdsShowCompletionState
            ) {
                isRewardedLoaded = false
                if (state == UnityAdsShowCompletionState.COMPLETED) {
                    runOnUiThread {
                        Toast.makeText(this@MainActivity, "Reward earned!", Toast.LENGTH_SHORT).show()
                    }
                }
                loadRewardedAd()
            }
        })
    }
}

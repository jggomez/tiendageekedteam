package co.devhack.tiendageekedteam.presentation.views

import android.content.Intent
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import co.devhack.tiendageekedteam.R
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        initAnimation()
    }

    private fun initViewModel() {
        startActivity(Intent(this, SignInActivity::class.java))
    }

    private fun initAnimation() {
        val transition = AnimationUtils.loadAnimation(
            this,
            R.anim.splash_transition
        )
        imgGeekStore.animation = transition
        transition.setAnimationListener(
            object : Animation.AnimationListener {
                override fun onAnimationRepeat(animation: Animation?) {
                }

                override fun onAnimationEnd(animation: Animation?) {
                    initViewModel()
                }

                override fun onAnimationStart(animation: Animation?) {
                }

            }
        )
    }
}

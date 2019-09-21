package co.devhack.tiendageekedteam.presentation.views

import android.content.Intent
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import co.devhack.tiendageekedteam.R
import co.devhack.tiendageekedteam.presentation.viewmodels.SignInViewModel
import co.devhack.tiendageekedteam.util.BaseActivity
import co.devhack.tiendageekedteam.util.Failure
import co.devhack.tiendageekedteam.util.notify
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : BaseActivity() {

    private lateinit var signInViewModel: SignInViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        signInViewModel =
            ViewModelProviders.of(
                this@SplashActivity
            ).get(SignInViewModel::class.java)

        initAnimation()
    }

    private fun initViewModel() {

        signInViewModel.lduser.observe(this, Observer { user ->
            hideProgress()
            if (user != null) {
                startActivity(Intent(this, ProductsActivity::class.java))
            } else {
                startActivity(Intent(this, SignInActivity::class.java))
            }
        })


        signInViewModel.ldfailure.observe(this, Observer { failure ->
            hideProgress()
            when (failure) {
                is Failure.NetworkConnection ->
                    getString(R.string.lbl_network_connection).notify(this)
                is Failure.ServerError -> failure.ex.message?.notify(this)
            }
        })

        signInViewModel.getCurrentUser()

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

package co.devhack.tiendageekedteam.presentation.views

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import co.devhack.tiendageekedteam.R
import co.devhack.tiendageekedteam.presentation.viewmodels.SignInViewModel
import co.devhack.tiendageekedteam.util.BaseActivity
import co.devhack.tiendageekedteam.util.Failure
import co.devhack.tiendageekedteam.util.notify
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignInActivity : BaseActivity() {

    private lateinit var signInViewModel: SignInViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        signInViewModel =
            ViewModelProviders.of(
                this@SignInActivity
            ).get(SignInViewModel::class.java)

        btnSignUp.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        btnSigIn.setOnClickListener {
            signIn()
        }

        signInViewModel.lduser.observe(this, Observer {
            hideProgress()
            startActivity(Intent(this, ProductsActivity::class.java))
            finish()
        })

        signInViewModel.ldfailure.observe(this, Observer { failure ->
            hideProgress()
            when (failure) {
                is Failure.NetworkConnection ->
                    getString(R.string.lbl_network_connection).notify(this)
                is Failure.ServerError -> failure.ex.message?.notify(this)
            }
        })
    }

    private fun signIn() {
        val email = txtEmail.text.toString()
        val password = txtPassword.text.toString()

        signInViewModel.sigInUserWithEmailAndPassword(email, password)
        showProgress()

    }
}

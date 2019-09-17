package co.devhack.tiendageekedteam.presentation.views

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import co.devhack.tiendageekedteam.R
import co.devhack.tiendageekedteam.presentation.viewmodels.SignUpViewModel
import co.devhack.tiendageekedteam.util.BaseActivity
import co.devhack.tiendageekedteam.util.Failure
import co.devhack.tiendageekedteam.util.notify
import kotlinx.android.synthetic.main.activity_main.*

class SignUpActivity : BaseActivity() {

    private lateinit var signUpViewModel: SignUpViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        signUpViewModel =
            ViewModelProviders.of(
                this@SignUpActivity
            ).get(SignUpViewModel::class.java)

        btnSignIn.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
        }

        btnSignUp.setOnClickListener {
<<<<<<< HEAD
<<<<<<< HEAD
            signUp()
        }

        signUpViewModel.lduser.observe(this, Observer {
            hideProgress()
            startActivity(Intent(this, ProductsActivity::class.java))
            finish()
        })

        signUpViewModel.ldfailure.observe(this, Observer { failure ->
            hideProgress()
            when (failure) {
                is Failure.NetworkConnection ->
                    getString(R.string.lbl_network_connection).notify(this)
                is Failure.ServerError -> failure.ex.message?.notify(this)
            }
        })
    }

    private fun signUp() {
        val email = txtEmail.text.toString()
        val password = txtPassword.text.toString()
        val passwordRepeat = txtPasswordRepeat.text.toString()

        // TODO: validar que todos son obligatorios y que el password el mismo

        signUpViewModel.createUserWithEmailAndPassword(email, password)
        showProgress()

=======
            
        }

>>>>>>> master
=======
            
        }

>>>>>>> master
    }
}

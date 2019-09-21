package co.devhack.tiendageekedteam.presentation.views

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import co.devhack.tiendageekedteam.R
import co.devhack.tiendageekedteam.presentation.viewmodels.SignInViewModel
import co.devhack.tiendageekedteam.util.BaseActivity
import co.devhack.tiendageekedteam.util.Failure
import co.devhack.tiendageekedteam.util.notify
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignInActivity : BaseActivity() {

    private lateinit var signInViewModel: SignInViewModel
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var callbackManager: CallbackManager

    private val RC_SIGN_IN = 1010

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        signInViewModel =
            ViewModelProviders.of(
                this@SignInActivity
            ).get(SignInViewModel::class.java)

        buildFacebookSigIn()

        btnSignUp.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        btnSigIn.setOnClickListener {
            signIn()
        }

        btnSignInGoogle.setOnClickListener {
            mGoogleSignInClient = buildGoogleSignClient()
            val signIntent = mGoogleSignInClient.signInIntent
            startActivityForResult(signIntent, RC_SIGN_IN)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == RC_SIGN_IN && resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
                signInViewModel.authWithCredentials(credential)
                showProgress()

            } catch (e: Exception) {
                e.message?.notify(this)
            }
        }

        if (resultCode == Activity.RESULT_OK && requestCode != RC_SIGN_IN) {
            callbackManager.onActivityResult(requestCode, resultCode, data)
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun buildGoogleSignClient(): GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.web_client_id))
            .requestEmail()
            .requestProfile()
            .build()

        return GoogleSignIn.getClient(this, gso)
    }

    private fun signIn() {
        val email = txtEmail.text.toString()
        val password = txtPassword.text.toString()

        signInViewModel.sigInUserWithEmailAndPassword(email, password)
        showProgress()

    }

    private fun buildFacebookSigIn() {
        callbackManager = CallbackManager.Factory.create()

        btnSignInFacebook.registerCallback(callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult?) {
                    val credential =
                        FacebookAuthProvider.getCredential(result?.accessToken?.token ?: "")
                    signInViewModel.authWithCredentials(credential)
                    showProgress()
                }

                override fun onCancel() {
                    getString(R.string.lbl_auth_facebook_cancel).notify(this@SignInActivity)
                }

                override fun onError(error: FacebookException?) {
                    error?.message?.notify(this@SignInActivity)
                    Log.e("FACEBOOG_AUTH", error?.message ?: "")
                }

            })
    }


}

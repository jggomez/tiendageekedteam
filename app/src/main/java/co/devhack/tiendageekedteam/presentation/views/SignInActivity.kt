package co.devhack.tiendageekedteam.presentation.views

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import co.devhack.tiendageekedteam.R
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignInActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        btnSignUp.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }
}

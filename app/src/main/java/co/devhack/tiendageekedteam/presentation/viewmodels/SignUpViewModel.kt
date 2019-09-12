package co.devhack.tiendageekedteam.presentation.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import co.devhack.tiendageekedteam.data.entities.User
import co.devhack.tiendageekedteam.data.repositories.UserRepositoryImp
import co.devhack.tiendageekedteam.util.Failure
import co.devhack.tiendageekedteam.util.NetworkHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignUpViewModel(app: Application) : AndroidViewModel(app) {

    val lduser: MutableLiveData<User> by lazy {
        MutableLiveData<User>()
    }

    val ldfailure: MutableLiveData<Failure> by lazy {
        MutableLiveData<Failure>()
    }

    private val userRepository: UserRepositoryImp by lazy {
        UserRepositoryImp(
            NetworkHandler(app.applicationContext)
        )
    }

    fun createUserWithEmailAndPassword(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val resp = userRepository
                .createUserWithEmailAndPassword(email, password)
            viewModelScope.launch {
                resp.either(::handleFailure, ::handleUser)
            }
        }
    }

    private fun handleFailure(failure: Failure) {
        ldfailure.value = failure
    }

    private fun handleUser(user: User?) {
        lduser.value = user
    }

}
package co.devhack.tiendageekedteam.presentation.repositories

import co.devhack.tiendageekedteam.data.entities.User
import co.devhack.tiendageekedteam.util.Either
import co.devhack.tiendageekedteam.util.Failure
import com.google.firebase.auth.AuthCredential

interface UserRepository {

    suspend fun authUserWithCredentials(credencial: AuthCredential):
            Either<Failure, User?>

    suspend fun createUserWithEmailAndPassword(
        email: String,
        password: String
    ): Either<Failure, User?>

    suspend fun sigInUserWithEmailAndPassword(
        email: String,
        password: String
    ): Either<Failure, User?>

    suspend fun getCurrentUserAuth(): Either<Failure, User?>

    suspend fun signOutUser(): Either<Failure, Boolean>

}
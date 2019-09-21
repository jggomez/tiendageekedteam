package co.devhack.tiendageekedteam.data.repositories

import co.devhack.tiendageekedteam.data.entities.User
import co.devhack.tiendageekedteam.presentation.repositories.UserRepository
import co.devhack.tiendageekedteam.util.Either
import co.devhack.tiendageekedteam.util.Failure
import co.devhack.tiendageekedteam.util.NetworkHandler
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class UserRepositoryImp(
    private val networkHandler: NetworkHandler
) : UserRepository {

    override suspend fun authUserWithCredentials(credential: AuthCredential): Either<Failure, User?> {
        return when (networkHandler.isConnected) {
            true -> {
                suspendCoroutine { continuation ->
                    FirebaseAuth
                        .getInstance()
                        .signInWithCredential(credential)
                        .addOnSuccessListener {
                            continuation.resume(
                                Either.Right(
                                    it.user?.let { firebaseUser ->
                                        mapToUserEntity(firebaseUser)
                                    }
                                )
                            )
                        }
                        .addOnFailureListener { ex ->
                            continuation.resume(
                                Either.Left(
                                    Failure.ServerError(ex)
                                )
                            )
                        }

                }

            }
            false, null -> Either.Left(Failure.NetworkConnection)
        }
    }

    override suspend fun createUserWithEmailAndPassword(
        email: String,
        password: String
    ): Either<Failure, User?> {
        return when (networkHandler.isConnected) {
            true -> {
                suspendCoroutine { continuation ->
                    FirebaseAuth
                        .getInstance()
                        .createUserWithEmailAndPassword(email, password)
                        .addOnSuccessListener { authResult ->
                            authResult.user?.sendEmailVerification()
                            FirebaseAuth.getInstance().signOut()
                            continuation.resume(
                                Either.Right(
                                    authResult.user?.let { firebaseUser ->
                                        mapToUserEntity(firebaseUser)
                                    }
                                )
                            )
                        }
                        .addOnFailureListener { ex ->
                            continuation.resume(
                                Either.Left(
                                    Failure.ServerError(ex)
                                )
                            )
                        }

                }

            }
            false, null -> Either.Left(Failure.NetworkConnection)
        }
    }

    override suspend fun sigInUserWithEmailAndPassword(
        email: String,
        password: String
    ): Either<Failure, User?> {
        return when (networkHandler.isConnected) {
            true -> {
                suspendCoroutine { continuation ->
                    FirebaseAuth
                        .getInstance()
                        .signInWithEmailAndPassword(email, password)
                        .addOnSuccessListener { authResult ->
                            if (authResult.user?.isEmailVerified == true) {
                                continuation.resume(
                                    Either.Right(
                                        authResult.user?.let { firebaseUser ->
                                            mapToUserEntity(firebaseUser)
                                        }
                                    )
                                )
                            } else {
                                continuation.resume(
                                    Either.Left(
                                        Failure.ServerError(
                                            Exception("Email is not verified")
                                        )
                                    )
                                )
                            }

                        }
                        .addOnFailureListener { ex ->
                            continuation.resume(
                                Either.Left(
                                    Failure.ServerError(ex)
                                )
                            )
                        }

                }

            }
            false, null -> Either.Left(Failure.NetworkConnection)
        }
    }

    override suspend fun getCurrentUserAuth(): Either<Failure, User?> {
        return when (networkHandler.isConnected) {
            true -> {
                val user = FirebaseAuth
                    .getInstance()
                    .currentUser
                Either.Right(user?.let {
                    mapToUserEntity(it)
                })
            }
            false, null -> Either.Left(Failure.NetworkConnection)
        }
    }

    override suspend fun signOutUser(): Either<Failure, Boolean> {
        return when (networkHandler.isConnected) {
            true -> {
                FirebaseAuth
                    .getInstance()
                    .signOut()
                Either.Right(true)
            }
            false, null -> Either.Left(Failure.NetworkConnection)
        }
    }

    private fun mapToUserEntity(firebaseUser: FirebaseUser) =
        User(
            firebaseUser.uid,
            firebaseUser.displayName,
            firebaseUser.email,
            firebaseUser.photoUrl
        )


}
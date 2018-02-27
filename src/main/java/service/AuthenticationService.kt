package service

import java.util.concurrent.Future

interface AuthenticationService {
    fun login(dni: String, password: String, remember: Boolean = false): Future<Boolean>
    fun logout(): Future<Unit>
}
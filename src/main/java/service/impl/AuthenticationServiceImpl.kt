package service.impl

import service.AuthenticationService
import java.util.concurrent.Future

class AuthenticationServiceImpl : AuthenticationService {
    override fun login(dni: String, password: String, remember: Boolean): Future<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun logout(): Future<Unit> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
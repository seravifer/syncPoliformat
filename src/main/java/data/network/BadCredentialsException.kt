package data.network

class BadCredentialsException(msg: String = "The credentials were erroneous\n", cause: Throwable? = null) : RuntimeException(msg, cause)
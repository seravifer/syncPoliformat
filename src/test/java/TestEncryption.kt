import org.kodein.di.direct
import org.kodein.di.generic.instance
import syncPoliformat.appComponent
import utils.EncryptedStorage

object TestEncryption {

    @JvmStatic
    fun main(args: Array<String>) {
        val encryptedStorage = EncryptedStorage(appComponent.direct.instance("credentials"))
        encryptedStorage.encrypt("¡Hola mundo!")
        print(encryptedStorage.decrypt())
    }

}

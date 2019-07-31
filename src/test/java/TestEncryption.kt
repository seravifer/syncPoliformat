import utils.Crypt.decrypt
import utils.Crypt.encrypt

object TestEncryption {

    @JvmStatic
    fun main(args: Array<String>) {
        encrypt("¡Hola mundo!")
        print(decrypt())
    }

}

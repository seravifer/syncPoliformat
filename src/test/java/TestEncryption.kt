import utils.decrypt
import utils.encrypt

object TestEncryption {

    @JvmStatic
    fun main(args: Array<String>) {
        encrypt("¡Hola mundo!")
        print(decrypt())
    }

}

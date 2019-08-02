package utils

enum class OS(private val matcher: String) {
    WINDOWS("win"),
    MAC("mac"),
    LINUX("");
    companion object {
        @JvmStatic
        fun host(): OS {
            val osName = System.getProperty("os.name")
            return values().find { osName.contains(it.matcher, true) } ?: LINUX
        }
    }
}

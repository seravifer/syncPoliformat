import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import org.kodein.di.KodeinAwareKt
import syncPoliformat.ApplicationFiles
import syncPoliformat.KodeinKt

import static org.kodein.di.TypesKt.TT

appender("FILE", FileAppender) {
    def app = KodeinAwareKt.getDirect(KodeinKt.getAppComponent()).Instance(TT(File.class), ApplicationFiles.ConfigFolder.INSTANCE)
    file = app.toPath().resolve('syncPoliformat.log').toString()
    encoder(PatternLayoutEncoder) {
        pattern = "%date %level [%thread] %logger{15} - %msg \\(%file:%line\\) %n"
    }
}
appender("STDOUT", ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = "%date %level [%thread] %logger{8} - %msg \\(%file:%line\\) %n"
    }
}
root(DEBUG, ["STDOUT", "FILE"])

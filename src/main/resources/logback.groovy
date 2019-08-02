import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import org.kodein.di.KodeinAwareKt

import static org.kodein.di.TypesKt.TT

appender("FILE", FileAppender) {
    def app = KodeinAwareKt.getDirect(KodeinKt.getAppComponent()).Instance(TT(File.class), "app")
    file = app.toPath().resolve('syncPoliformat.log').toString()
    encoder(PatternLayoutEncoder) {
        pattern = "%date %level [%thread] %logger{10} [%file : %line] %msg%n"
    }
}
appender("STDOUT", ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = "%date %level %logger{8} [%file:%line] %msg%n"
    }
}
root(DEBUG, ["STDOUT", "FILE"])

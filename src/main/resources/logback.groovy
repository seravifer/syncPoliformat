import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import utils.Settings

appender("FILE", FileAppender) {
    file = Settings.appDirectory.resolve('syncPoliformat.log').toString()
    encoder(PatternLayoutEncoder) {
        pattern = "%date %level [%thread] %logger{10} [%file : %line] %msg%n"
    }
}
appender("STDOUT", ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = "%date %level %logger{8} [%file:%line] %msg"
    }
}
root(DEBUG, ["STDOUT", "FILE"])
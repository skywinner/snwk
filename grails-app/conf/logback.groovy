import grails.util.BuildSettings
import grails.util.Environment
import org.springframework.boot.logging.logback.ColorConverter
import org.springframework.boot.logging.logback.WhitespaceThrowableProxyConverter

import java.nio.charset.StandardCharsets

conversionRule 'clr', ColorConverter
conversionRule 'wex', WhitespaceThrowableProxyConverter

// See http://logback.qos.ch/manual/groovy.html for details on configuration
appender('STDOUT', ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        charset = StandardCharsets.UTF_8

        pattern =
                '%clr(%d{yyyy-MM-dd HH:mm:ss.SSS}){faint} ' + // Date
                        '%clr(%5p) ' + // Log level
                        '%clr(---){faint} %clr([%15.15t]){faint} ' + // Thread
                        '%clr(%-40.40logger{39}){cyan} %clr(:){faint} ' + // Logger
                        '%m%n%wex' // Message
        pattern =
                '%clr(%d{ISO8601}){faint} ' + // Date
                        '%clr(%-5p) ' + // Log level
                        '%clr(%c{1}){cyan} %clr(-){faint} ' + // Logger
                        '%m%n%wex' // Message
    }
}

def targetDir = BuildSettings.TARGET_DIR
if (targetDir != null && Environment.current.name != "test") {
    println "TARGET_DIR for stacktrace.log=${targetDir}"
    appender("FULL_STACKTRACE", FileAppender) {
        if (Environment.isDevelopmentMode()) {
            file = "${targetDir}/stacktrace.log"
        } else {
            file = "/var/log/tomcat9/snwk_stacktrace.log"

        }
        append = true
        encoder(PatternLayoutEncoder) {
            charset = StandardCharsets.UTF_8
            pattern = "%level %logger - %msg%n"
        }
    }
    logger("StackTrace", ERROR, ['FULL_STACKTRACE'], false)
}
logger("snwk", INFO)
logger("groovy.sql", INFO)

root(ERROR, ['STDOUT'])

package ru.otus.otuskotlin.markeplace.app.spring

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class Application


// swagger URL: http://localhost:8080/swagger-ui.html

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}

package pl.eked.bartforge.agent.api

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["pl.eked.bartforge"])
class BartforgeApplication

fun main(args: Array<String>) {
    runApplication<BartforgeApplication>(*args)
}
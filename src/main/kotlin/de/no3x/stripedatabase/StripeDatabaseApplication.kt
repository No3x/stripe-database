package de.no3x.stripedatabase

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class StripeDatabaseApplication

fun main(args: Array<String>) {
    runApplication<StripeDatabaseApplication>(*args)
}

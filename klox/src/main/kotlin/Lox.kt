package me.bink.klox

import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.system.exitProcess

private var hadError = false

fun main(args: Array<String>) {
    val argSize = args.size

    when {
        (argSize > 1) -> showUsage()
        (argSize == 1) -> runFile(args[0])
        else -> runPrompt()
    }
}

private fun showUsage() {
    println("Usage: jlox [script]")
    exitProcess(64)
}

private fun runFile(path: String) {
    println("Running file at $path")

    val bytes = Files.readAllBytes(Paths.get(path))
    val pathString = String(bytes, Charset.defaultCharset())

    run(pathString)
    if (hadError) {
        exitProcess(65)
    }
}

private fun runPrompt() {
    println("Running user-provided prompt(s)!")

    val input = InputStreamReader(System.`in`)
    val reader = BufferedReader(input)

    while (true) {
        print("\n> ")
        val line = reader.readLine()
        if (line == null) {
            println("Cannot run null prompt.")
            break
        } else {
            run(line)
            hadError = false
        }
    }
}

private fun run(source: String) {
    println("Running: $source")

    val scanner = Scanner(source)
    val tokens = scanner.scanTokens()

    for (token in tokens) {
        println(token)
    }
}



// ERROR REPORTING

// object keyword helps declare a singleton in this usage
// see: https://kotlinlang.org/docs/object-declarations.html#object-declarations-overview
object Lox {

    fun error(line: Int, message: String) {
        System.err.println("[line $line] Error${""}: $message")
        hadError = true
    }
}
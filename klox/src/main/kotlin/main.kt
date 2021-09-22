package me.bink.klox

import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.system.exitProcess

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
            run(line!!)
        }
    }
}

private fun run(command: String) {
    print("Running: $command")

    // TODO: Create scanner, get tokens, report each token!
}
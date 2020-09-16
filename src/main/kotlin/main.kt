import com.sun.net.httpserver.Headers
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.stream.Collectors


val headerFilenames = mutableMapOf(
    "licenseHeaders/AL2.txt" to "Apache-2.0", "licenseHeaders/MIT.txt" to "MIT", "licenseHeaders/GPL3.txt" to "GPL-3.0",
    "licenseHeaders/BSD3.txt" to "BSD-3-Clause", "licenseHeaders/LGPL3.txt" to "LGPL-3.0"
)

fun checkLicense(header: String, licenseFile: File): Boolean {
    val reader = licenseFile.bufferedReader()
    val iterator = reader.lines().iterator()
    if (iterator.hasNext()) {
        iterator.next()
    }
    while (iterator.hasNext()) {
        val line = iterator.next()
        if (!header.contains(line) && line != "") {
            return false
        }
    }
    return true
}

fun getMainLicenseFromFile(file: File): String {
    var mainLicense: String
    if (file.name == "LICENSE.txt" || file.name == "license.txt" ||
        file.name == "LICENSE" || file.name == "license") {
        mainLicense = file.readText()
        return mainLicense
    }
    return ""
}

fun getLicenseFromFile(file: File): String {
    var licenseHeaderType: String
    val reader = file.bufferedReader()
    val iterator = reader.lines().iterator()
    var i = 0
    var lines = ""
    while (iterator.hasNext()) {
        lines += iterator.next()
        lines += " "
        i++
        if (i == 100) {
            break
        }
    }
    if (file.name == "LICENSE") {
        //println(lines)
    }
    for (filename in headerFilenames.keys) {
        if (checkLicense(lines, File(filename))) {
            licenseHeaderType = headerFilenames[filename]!!
            return licenseHeaderType
        }
    }
    return ""
}

fun main(args: Array<String>) {
    val dir = File (
        if (args.isEmpty()) {
            System.getProperty("user.dir")
        } else {
            args[0]
        }
    )
    var mainLicense: String = ""
    var licenseHeaders: MutableSet<String> = mutableSetOf()
    if (dir.isDirectory) {
        dir.walkTopDown().forEach {
            if (!it.isDirectory) {
                if (mainLicense == "") {
                    mainLicense = getMainLicenseFromFile(it)
                }
                licenseHeaders.add(getLicenseFromFile(it))
            }
        }
    } else {
        if (mainLicense == "") {
            mainLicense = getMainLicenseFromFile(dir)
        }
        licenseHeaders.add(getLicenseFromFile(dir))
    }
    if (mainLicense != "") {
        println("Main license:")
        println(mainLicense)
    } else {
        println("No main license found")
    }
    var flag: Boolean = false
    licenseHeaders.forEach {
        if (it != "") {
            flag = true
        }
    }
    if (flag) {
        println("Other license headers:")
        licenseHeaders.forEach {
            if (it != "") {
                println(it)
            }
        }
    } else {
        println("No license headers found")
    }
}
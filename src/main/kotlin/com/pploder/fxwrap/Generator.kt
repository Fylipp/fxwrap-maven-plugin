package com.pploder.fxwrap

import org.apache.maven.plugin.MojoExecutionException
import java.nio.file.Files
import java.nio.file.Path

class Generator(val baseDir: Path, val template: String) {

    fun generate(className: String, classPackage: List<String>, relativeFXMLPath: String) {
        if (classPackage.isEmpty()) {
            throw MojoExecutionException("Generated class must be in a package")
        }

        val content = template
                .replace("\$className", className)
                .replace("\$package", classPackage.reduce { l, r -> "$l.$r" })
                .replace("\$relativePath", relativeFXMLPath)

        val dir = resolvePath(baseDir, classPackage)
        Files.createDirectories(dir)

        val file = dir.resolve("$className.java")
        file.toFile().writeText(content)
    }

    private fun resolvePath(root: Path, hierarchy: List<String>): Path {
        val child = root.resolve(hierarchy.first())

        return when (hierarchy.size) {
            1 -> child
            else -> resolvePath(child, hierarchy.subList(1, hierarchy.size))
        }
    }

}

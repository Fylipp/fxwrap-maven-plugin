package com.pploder.fxwrap

import org.apache.maven.model.Resource
import org.apache.maven.plugin.AbstractMojo
import org.apache.maven.plugin.MojoExecutionException
import org.apache.maven.plugins.annotations.LifecyclePhase
import org.apache.maven.plugins.annotations.Mojo
import org.apache.maven.plugins.annotations.Parameter
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

/**
 * This MOJO is responsible for scanning the resource directory for FXML files
 * and generating wrapper classes for them.
 */
@Mojo(name = "generate", defaultPhase = LifecyclePhase.GENERATE_SOURCES, requiresOnline = false, requiresProject = true, threadSafe = false)
class GenerationMojo : AbstractMojo() {

    @Parameter(defaultValue = "%sFXML")
    private lateinit var classNameTemplate: String

    @Parameter(defaultValue = "\${project.build.directory}/generated-sources/")
    private lateinit var outputDirectory: File

    @Parameter(defaultValue = "\${project.build.resources}")
    private lateinit var resourceDirectories: Array<Resource>

    @Parameter(defaultValue = "fxml")
    private lateinit var fxmlFileFormat: String

    override fun execute() {
        log.debug("Loading template ...")
        val template = loadTemplate()

        val generator = Generator(Paths.get(outputDirectory.toURI()), template)
        val fxmlSuffix = ".$fxmlFileFormat"

        log.debug("Scanning for FXML files with extension '$fxmlFileFormat' ...")

        resourceDirectories.forEach {
            val path = Paths.get(File(it.directory).toURI())

            log.debug("Scanning resource directory: $path")

            Files.walk(path)
                    .filter { Files.isRegularFile(it) }
                    .map { Pair(it.fileName.toString(), it) }
                    .filter { it.first.endsWith(fxmlSuffix, true) }
                    .forEach {
                        log.info("Generating class for ${it.first} ...")

                        val name = classNameTemplate.replace("%s", it.first.substring(0, it.first.length - fxmlSuffix.length))

                        val fxmlPath = path.relativize(it.second).toString().split("/", "\\")
                        val pckg = fxmlPath.subList(0, fxmlPath.size - 1)

                        try {
                            generator.generate(name, pckg, it.first)
                        } catch (e: Exception) {
                            throw MojoExecutionException("Failed to generate class for ${it.first}: ${e.message}", e)
                        }
                    }
        }
    }

    private fun loadTemplate(): String {
        try {
            with(GenerationMojo::class.java.getResourceAsStream("OutputTemplate.java")) {
                return@loadTemplate Scanner(this).useDelimiter("\\Z").next()
            }
        } catch (e: Exception) {
            throw MojoExecutionException("Failed to load output template: $e")
        }
    }

}

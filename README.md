# FXWrap

![Travis CI](https://travis-ci.org/Fylipp/fxwrap-maven-plugin.svg?branch=master)

A Maven plugin to generate boilerplate loading code for FXML resources.

## Example

Assuming a FXML resource called `ExamplePane.fxml` is available at compile- and run-time
the following code can then be used:

```java
FXMLLoader loader = ExamplePaneFXML.getInstance().load();
Pane root = loader.getRoot();
ExampleController controller = loader.getController();
```

## Generated Class

FXWrap generates a class for every FXML file it finds at compile-time. These are singletons
with a method `public String getRelativePath()` which returns the path to the FXML file and
`public FXMLLoader load() throws IOException` which loads the FXML file and returns the loader (a new `FXMLLoader` is used every time).

## Usage

To add the plugin you must add it to your POM along with a helper plugin and the [JitPack](https://jitpack.io/) repository.

```xml
<plugin>
     <groupId>com.github.Fylipp</groupId>
     <artifactId>fxwrap</artifactId>
     <version>v1.0.0</version>
 </plugin>
 
 <plugin>
     <groupId>org.codehaus.mojo</groupId>
     <artifactId>build-helper-maven-plugin</artifactId>
     <version>3.0.0</version>
     <executions>
         <execution>
             <id>test</id>
             <phase>generate-sources</phase>
             <goals>
                 <goal>add-source</goal>
             </goals>
             <configuration>
                 <sources>
                     <source>${project.build.directory}/generated-sources</source>
                 </sources>
             </configuration>
         </execution>
     </executions>
 </plugin>
```

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

## Configuration Options

| Option              | Type       | Default                                       | Description                            |
| ------------------- | ---------- | --------------------------------------------- | -------------------------------------- |
| classNameTemplate   | String     | %sFXML                                        | The template for generated class names |
| outputDirectory     | File       | ${project.build.directory}/generated-sources/ | The output directory                   |
| resourceDirectories | Resource[] | ${project.build.resources}                    | The directories to scan                |
| fxmlFileFormat      | String     | fxml                                          | The file extension used by FXML files  |

## License
MIT.

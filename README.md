# mpb
> A Minecraft plugin bootstrap library

[![Release](https://jitpack.io/v/tguichaoua/mpb.svg)](https://jitpack.io/#tguichaoua/mbp)


[javadoc](https://jitpack.io/com/github/tguichaoua/mpb/latest/javadoc/)

## Maven depencency

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

 <dependencies>
    <dependency>
        <groupId>com.github.tguichaoua</groupId>
        <artifactId>mpb</artifactId>
        <version>0.3.0</version>
    </dependency>
</dependencies>
```

**Note**: if you are using `maven-shade-plugin` you must also use relocation to avoid conflict with other plugins that may use this library.

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-shade-plugin</artifactId>
            <version>3.2.1</version>
            <configuration>
                <relocations>
                    <relocation>
                        <pattern>azerty.tguichaoua.mpb</pattern>
                        <shadedPattern>${project.groupId}.mpb</shadedPattern>
                    </relocation>
                </relocations>
            </configuration>
        </plugin>
    </plugins>
</build>
```

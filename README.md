# Minecraft Plugin Bootstrap

<span><img alt="unmaintained" src="https://img.shields.io/badge/status-unmaintained-lightgrey?style=for-the-badge" height="20"></span>
[<img alt="jitpack" src="https://img.shields.io/jitpack/version/com.github.tguichaoua/mpb?style=for-the-badge" height="20" />](https://jitpack.io/#tguichaoua/mpb)

## Features

- Shortcut/util methods for plugin
- Commands
  - Automatic argument parsing
  - Automatic completation
  - Extensible
- Inventory based menu

## Docs

- [javadoc](https://jitpack.io/com/github/tguichaoua/mpb/latest/javadoc/)

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
        <version>1.1.0</version>
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

## Author

👤 **Tristan Guichaoua**

-   Github: [@tguichaoua](https://github.com/tguichaoua)

## 🤝 Contributing

Contributions, issues and feature requests are welcome!<br />Feel free to check [issues page](https://github.com/tguichaoua/mpb/issues).

## Show your support

Give a ⭐️ if this project helped you!

## 📝 License

Copyright © 2021 [Tristan Guichaoua](https://github.com/tguichaoua).<br />
This project is [MIT](https://github.com/tguichaoua/mpb/blob/main/LICENSE) licensed.

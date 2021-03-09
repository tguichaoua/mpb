package azerty.tguichaoua.mpb.command;

import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

@FunctionalInterface
public interface CommandExceptionCatcher extends Consumer<@NotNull CommandException> {
}

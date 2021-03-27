package azerty.tguichaoua.mpb.command.argument.cascade;

import azerty.tguichaoua.mpb.command.argument.CommandArgument;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface CascadeFunction<From, To> {
	@NotNull CommandArgument<To> apply(From o);
}

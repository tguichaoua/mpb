package azerty.tguichaoua.mpb.command;

import azerty.tguichaoua.mpb.command.function.*;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;

@RequiredArgsConstructor
public final class SingleCommand extends MyCommand {

	private final @Nullable String permission;
	private final @NotNull CommandArgument<?>[] arguments;
	private final @NotNull CommandFunction function;

	@Override
	protected void execute(@NotNull final CommandExecution execution) throws CommandException {
		if (permission != null)
			execution.checkPermission(permission);
		function.execute(execution);
	}

	@Override
	protected @NotNull Collection<String> complete(@NotNull final CommandExecution execution) {
		Collection<String> completion = Collections.emptyList();
		try {
			for (final CommandArgument<?> arg : arguments) {
				for (int i = arg.multiplicity(); i > 0; i--) {
					completion = arg.complete(execution);
					if (execution.remains() == 0) break;
				}
				if (execution.remains() == 0) break;
			}
		} catch (final CommandException ignored) {
			return Collections.emptyList();
		}
		return execution.remains() == 0 ? completion : Collections.emptyList();
	}

	// --- 0 Arguments ------------------------------------------------
	public static SingleCommand of(
			@NotNull final CommandFunction fc
	) {
		return new SingleCommand(null, new CommandArgument[0], fc);
	}

	public static SingleCommand of(
			@NotNull final String permission,
			@NotNull final CommandFunction fc
	) {
		return new SingleCommand(permission, new CommandArgument[0], fc);
	}

	// --- 1 Arguments ------------------------------------------------
	public static <A> SingleCommand of(
			@NotNull final CommandArgument<A> a,
			@NotNull final CommandFunction1<A> fc
	) {
		return new SingleCommand(null, new CommandArgument[]{a}, fc.asCommandFunction(a));
	}

	public static <A> SingleCommand of(
			@NotNull final String permission,
			@NotNull final CommandArgument<A> a,
			@NotNull final CommandFunction1<A> fc
	) {
		return new SingleCommand(permission, new CommandArgument[]{a}, fc.asCommandFunction(a));
	}

	// --- 2 Arguments ------------------------------------------------
	public static <A, B> SingleCommand of(
			@NotNull final CommandArgument<A> a,
			@NotNull final CommandArgument<B> b,
			@NotNull final CommandFunction2<A, B> fc
	) {
		return new SingleCommand(null, new CommandArgument[]{a, b}, fc.asCommandFunction(a, b));
	}

	public static <A, B> SingleCommand of(
			@NotNull final String permission,
			@NotNull final CommandArgument<A> a,
			@NotNull final CommandArgument<B> b,
			@NotNull final CommandFunction2<A, B> fc
	) {
		return new SingleCommand(permission, new CommandArgument[]{a, b}, fc.asCommandFunction(a, b));
	}

	// --- 3 Arguments ------------------------------------------------
	public static <A, B, C> SingleCommand of(
			@NotNull final CommandArgument<A> a,
			@NotNull final CommandArgument<B> b,
			@NotNull final CommandArgument<C> c,
			@NotNull final CommandFunction3<A, B, C> fc
	) {
		return new SingleCommand(null, new CommandArgument[]{a, b, c}, fc.asCommandFunction(a, b, c));
	}

	public static <A, B, C> SingleCommand of(
			@NotNull final String permission,
			@NotNull final CommandArgument<A> a,
			@NotNull final CommandArgument<B> b,
			@NotNull final CommandArgument<C> c,
			@NotNull final CommandFunction3<A, B, C> fc
	) {
		return new SingleCommand(permission, new CommandArgument[]{a, b, c}, fc.asCommandFunction(a, b, c));
	}

	// --- 4 Arguments ------------------------------------------------
	public static <A, B, C, D> SingleCommand of(
			@NotNull final CommandArgument<A> a,
			@NotNull final CommandArgument<B> b,
			@NotNull final CommandArgument<C> c,
			@NotNull final CommandArgument<D> d,
			@NotNull final CommandFunction4<A, B, C, D> fc
	) {
		return new SingleCommand(null, new CommandArgument[]{a, b, c, d}, fc.asCommandFunction(a, b, c, d));
	}

	public static <A, B, C, D> SingleCommand of(
			@NotNull final String permission,
			@NotNull final CommandArgument<A> a,
			@NotNull final CommandArgument<B> b,
			@NotNull final CommandArgument<C> c,
			@NotNull final CommandArgument<D> d,
			@NotNull final CommandFunction4<A, B, C, D> fc
	) {
		return new SingleCommand(permission, new CommandArgument[]{a, b, c, d}, fc.asCommandFunction(a, b, c, d));
	}

	// --- 5 Arguments ------------------------------------------------
	public static <A, B, C, D, E> SingleCommand of(
			@NotNull final CommandArgument<A> a,
			@NotNull final CommandArgument<B> b,
			@NotNull final CommandArgument<C> c,
			@NotNull final CommandArgument<D> d,
			@NotNull final CommandArgument<E> e,
			@NotNull final CommandFunction5<A, B, C, D, E> fc
	) {
		return new SingleCommand(null, new CommandArgument[]{a, b, c, d, e}, fc.asCommandFunction(a, b, c, d, e));
	}

	public static <A, B, C, D, E> SingleCommand of(
			@NotNull final String permission,
			@NotNull final CommandArgument<A> a,
			@NotNull final CommandArgument<B> b,
			@NotNull final CommandArgument<C> c,
			@NotNull final CommandArgument<D> d,
			@NotNull final CommandArgument<E> e,
			@NotNull final CommandFunction5<A, B, C, D, E> fc
	) {
		return new SingleCommand(permission, new CommandArgument[]{a, b, c, d, e}, fc.asCommandFunction(a, b, c, d, e));
	}
}

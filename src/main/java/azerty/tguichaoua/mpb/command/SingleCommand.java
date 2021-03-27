package azerty.tguichaoua.mpb.command;

import azerty.tguichaoua.mpb.command.argument.CommandArgument;
import azerty.tguichaoua.mpb.command.executor.*;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;

@RequiredArgsConstructor
public final class SingleCommand extends MyCommand {

	private final @Nullable String permission;
	private final @NotNull CommandArgument<?>[] arguments;
	private final @NotNull MyCommandExecutor executor;

	@Override
	protected void execute(@NotNull final CommandExecution execution) throws CommandException {
		if (permission != null)
			execution.checkPermission(permission);
		executor.execute(execution);
	}

	@Override
	protected @NotNull Collection<String> complete(@NotNull final CommandExecution execution) {
		Collection<String> completion = Collections.emptyList();
		try {
			for (final CommandArgument<?> arg : arguments) {
				completion = arg.complete(execution);
				if (execution.remains() == 0) break;
				execution.endArgument();
				execution.dropAllCheckpoints();
			}
		} catch (final CommandException ignored) {
			return Collections.emptyList();
		}
		return execution.remains() == 0 ? completion : Collections.emptyList();
	}

	// --- 0 Arguments ------------------------------------------------
	public static SingleCommand of(
			@NotNull final MyCommandExecutor fc
	) {
		return new SingleCommand(null, new CommandArgument[0], fc);
	}

	public static SingleCommand of(
			@NotNull final String permission,
			@NotNull final MyCommandExecutor fc
	) {
		return new SingleCommand(permission, new CommandArgument[0], fc);
	}

	// --- 1 Arguments ------------------------------------------------
	public static <A> SingleCommand of(
			@NotNull final CommandArgument<A> a,
			@NotNull final MyCommandExecutor1<A> fc
	) {
		return new SingleCommand(null, new CommandArgument[]{a}, fc.asMyCommandExecutor(a));
	}

	public static <A> SingleCommand of(
			@NotNull final String permission,
			@NotNull final CommandArgument<A> a,
			@NotNull final MyCommandExecutor1<A> fc
	) {
		return new SingleCommand(permission, new CommandArgument[]{a}, fc.asMyCommandExecutor(a));
	}

	// --- 2 Arguments ------------------------------------------------
	public static <A, B> SingleCommand of(
			@NotNull final CommandArgument<A> a,
			@NotNull final CommandArgument<B> b,
			@NotNull final MyCommandExecutor2<A, B> fc
	) {
		return new SingleCommand(null, new CommandArgument[]{a, b}, fc.asMyCommandExecutor(a, b));
	}

	public static <A, B> SingleCommand of(
			@NotNull final String permission,
			@NotNull final CommandArgument<A> a,
			@NotNull final CommandArgument<B> b,
			@NotNull final MyCommandExecutor2<A, B> fc
	) {
		return new SingleCommand(permission, new CommandArgument[]{a, b}, fc.asMyCommandExecutor(a, b));
	}

	// --- 3 Arguments ------------------------------------------------
	public static <A, B, C> SingleCommand of(
			@NotNull final CommandArgument<A> a,
			@NotNull final CommandArgument<B> b,
			@NotNull final CommandArgument<C> c,
			@NotNull final MyCommandExecutor3<A, B, C> fc
	) {
		return new SingleCommand(null, new CommandArgument[]{a, b, c}, fc.asMyCommandExecutor(a, b, c));
	}

	public static <A, B, C> SingleCommand of(
			@NotNull final String permission,
			@NotNull final CommandArgument<A> a,
			@NotNull final CommandArgument<B> b,
			@NotNull final CommandArgument<C> c,
			@NotNull final MyCommandExecutor3<A, B, C> fc
	) {
		return new SingleCommand(permission, new CommandArgument[]{a, b, c}, fc.asMyCommandExecutor(a, b, c));
	}

	// --- 4 Arguments ------------------------------------------------
	public static <A, B, C, D> SingleCommand of(
			@NotNull final CommandArgument<A> a,
			@NotNull final CommandArgument<B> b,
			@NotNull final CommandArgument<C> c,
			@NotNull final CommandArgument<D> d,
			@NotNull final MyCommandExecutor4<A, B, C, D> fc
	) {
		return new SingleCommand(null, new CommandArgument[]{a, b, c, d}, fc.asMyCommandExecutor(a, b, c, d));
	}

	public static <A, B, C, D> SingleCommand of(
			@NotNull final String permission,
			@NotNull final CommandArgument<A> a,
			@NotNull final CommandArgument<B> b,
			@NotNull final CommandArgument<C> c,
			@NotNull final CommandArgument<D> d,
			@NotNull final MyCommandExecutor4<A, B, C, D> fc
	) {
		return new SingleCommand(permission, new CommandArgument[]{a, b, c, d}, fc.asMyCommandExecutor(a, b, c, d));
	}

	// --- 5 Arguments ------------------------------------------------
	public static <A, B, C, D, E> SingleCommand of(
			@NotNull final CommandArgument<A> a,
			@NotNull final CommandArgument<B> b,
			@NotNull final CommandArgument<C> c,
			@NotNull final CommandArgument<D> d,
			@NotNull final CommandArgument<E> e,
			@NotNull final MyCommandExecutor5<A, B, C, D, E> fc
	) {
		return new SingleCommand(null, new CommandArgument[]{a, b, c, d, e}, fc.asMyCommandExecutor(a, b, c, d, e));
	}

	public static <A, B, C, D, E> SingleCommand of(
			@NotNull final String permission,
			@NotNull final CommandArgument<A> a,
			@NotNull final CommandArgument<B> b,
			@NotNull final CommandArgument<C> c,
			@NotNull final CommandArgument<D> d,
			@NotNull final CommandArgument<E> e,
			@NotNull final MyCommandExecutor5<A, B, C, D, E> fc
	) {
		return new SingleCommand(permission, new CommandArgument[]{a, b, c, d, e}, fc.asMyCommandExecutor(a, b, c, d, e));
	}
}

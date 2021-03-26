package azerty.tguichaoua.mpb.command.argument;

import azerty.tguichaoua.mpb.command.CommandException;
import azerty.tguichaoua.mpb.command.CommandExecution;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.function.Function;

@RequiredArgsConstructor
public class CascadeCommandArgument<A, B, C, D, E, F> implements CommandArgument<F> {

	private final @NotNull CommandArgument<A> source;
	private final @NotNull Function<A, @NotNull CommandArgument<B>> transform1;
	private final @NotNull Function<B, @NotNull CommandArgument<C>> transform2;
	private final @NotNull Function<C, @NotNull CommandArgument<D>> transform3;
	private final @NotNull Function<D, @NotNull CommandArgument<E>> transform4;
	private final @NotNull Function<E, @NotNull CommandArgument<F>> transform5;

	@Override
	public F parse(@NotNull final CommandExecution execution) throws CommandException {
		final A a = execution.get(source);
		final B b = execution.get(transform1.apply(a));
		final C c = execution.get(transform2.apply(b));
		final D d = execution.get(transform3.apply(c));
		final E e = execution.get(transform4.apply(d));
		return execution.get(transform5.apply(e));
	}

	@Override
	public @NotNull Collection<String> complete(@NotNull final CommandExecution execution) throws CommandException {
		Collection<String> complete = Collections.emptyList();
		if (execution.remains() == 0) return complete;

		execution.checkpoint();
		complete = source.complete(execution);
		if (execution.remains() == 0) return complete;
		execution.restore();
		final A a = execution.get(source);

		final CommandArgument<B> B = transform1.apply(a);
		execution.checkpoint();
		complete = B.complete(execution);
		if (execution.remains() == 0) return complete;
		execution.restore();
		final B b = execution.get(B);

		final CommandArgument<C> C = transform2.apply(b);
		execution.checkpoint();
		complete = C.complete(execution);
		if (execution.remains() == 0) return complete;
		execution.restore();
		final C c = execution.get(C);

		final CommandArgument<D> D = transform3.apply(c);
		execution.checkpoint();
		complete = D.complete(execution);
		if (execution.remains() == 0) return complete;
		execution.restore();
		final D d = execution.get(D);

		final CommandArgument<E> E = transform4.apply(d);
		execution.checkpoint();
		complete = E.complete(execution);
		if (execution.remains() == 0) return complete;
		execution.restore();
		final E e = execution.get(E);

		final CommandArgument<F> F = transform5.apply(e);
		complete = F.complete(execution);

		return complete;
	}

	public static <A, B, C, D, E, F> CascadeCommandArgument<A, B, C, D, E, F> of(
			@NotNull final CommandArgument<A> source,
			@NotNull final Function<A, @NotNull CommandArgument<B>> transform1,
			@NotNull final Function<B, @NotNull CommandArgument<C>> transform2,
			@NotNull final Function<C, @NotNull CommandArgument<D>> transform3,
			@NotNull final Function<D, @NotNull CommandArgument<E>> transform4,
			@NotNull final Function<E, @NotNull CommandArgument<F>> transform5
	) {
		return new CascadeCommandArgument<>(source, transform1, transform2, transform3, transform4, transform5);
	}

	public static <A, B, C, D, E> CascadeCommandArgument<A, B, C, D, E, E> of(
			@NotNull final CommandArgument<A> source,
			@NotNull final Function<A, @NotNull CommandArgument<B>> transform1,
			@NotNull final Function<B, @NotNull CommandArgument<C>> transform2,
			@NotNull final Function<C, @NotNull CommandArgument<D>> transform3,
			@NotNull final Function<D, @NotNull CommandArgument<E>> transform4
	) {
		return new CascadeCommandArgument<>(source, transform1, transform2, transform3, transform4, CommandArgument::constant);
	}

	public static <A, B, C, D> CascadeCommandArgument<A, B, C, D, D, D> of(
			@NotNull final CommandArgument<A> source,
			@NotNull final Function<A, @NotNull CommandArgument<B>> transform1,
			@NotNull final Function<B, @NotNull CommandArgument<C>> transform2,
			@NotNull final Function<C, @NotNull CommandArgument<D>> transform3
	) {
		return new CascadeCommandArgument<>(source, transform1, transform2, transform3, CommandArgument::constant, CommandArgument::constant);
	}

	public static <A, B, C> CascadeCommandArgument<A, B, C, C, C, C> of(
			@NotNull final CommandArgument<A> source,
			@NotNull final Function<A, @NotNull CommandArgument<B>> transform1,
			@NotNull final Function<B, @NotNull CommandArgument<C>> transform2
	) {
		return new CascadeCommandArgument<>(source, transform1, transform2, CommandArgument::constant, CommandArgument::constant, CommandArgument::constant);
	}

	public static <A, B> CascadeCommandArgument<A, B, B, B, B, B> of(
			@NotNull final CommandArgument<A> source,
			@NotNull final Function<A, @NotNull CommandArgument<B>> transform
	) {
		return new CascadeCommandArgument<>(source, transform, CommandArgument::constant, CommandArgument::constant, CommandArgument::constant, CommandArgument::constant);
	}
}

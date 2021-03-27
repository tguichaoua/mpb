package azerty.tguichaoua.mpb.command.argument.cascade;

import azerty.tguichaoua.mpb.command.CommandException;
import azerty.tguichaoua.mpb.command.CommandExecution;
import azerty.tguichaoua.mpb.command.argument.CommandArgument;
import lombok.RequiredArgsConstructor;
import org.javatuples.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;

public abstract class CascadeArgument2<A, B> implements CommandArgument<Pair<A, B>> {
	public final <C> CascadeArgument3<A, B, C> cascade(
			@NotNull final CascadeFunction<B, C> c
	) {
		return new Cascade3_2<>(this, c);
	}

	public final <C, D> CascadeArgument4<A, B, C, D> cascade(
			@NotNull final CascadeFunction<B, C> c,
			@NotNull final CascadeFunction<C, D> d
	) {
		return new Cascade4_2<>(this, c, d);
	}

	public final <C, D, E> CascadeArgument5<A, B, C, D, E> cascade(
			@NotNull final CascadeFunction<B, C> c,
			@NotNull final CascadeFunction<C, D> d,
			@NotNull final CascadeFunction<D, E> e
	) {
		return new Cascade5_2<>(this, c, d, e);
	}

	public final <C, D, E, F> CascadeArgument6<A, B, C, D, E, F> cascade(
			@NotNull final CascadeFunction<B, C> c,
			@NotNull final CascadeFunction<C, D> d,
			@NotNull final CascadeFunction<D, E> e,
			@NotNull final CascadeFunction<E, F> f
	) {
		return new Cascade6_2<>(this, c, d, e, f);
	}

	public final <C, D, E, F, G> CascadeArgument7<A, B, C, D, E, F, G> cascade(
			@NotNull final CascadeFunction<B, C> c,
			@NotNull final CascadeFunction<C, D> d,
			@NotNull final CascadeFunction<D, E> e,
			@NotNull final CascadeFunction<E, F> f,
			@NotNull final CascadeFunction<F, G> g
	) {
		return new Cascade7_2<>(this, c, d, e, f, g);
	}

	public final <C, D, E, F, G, H> CascadeArgument8<A, B, C, D, E, F, G, H> cascade(
			@NotNull final CascadeFunction<B, C> c,
			@NotNull final CascadeFunction<C, D> d,
			@NotNull final CascadeFunction<D, E> e,
			@NotNull final CascadeFunction<E, F> f,
			@NotNull final CascadeFunction<F, G> g,
			@NotNull final CascadeFunction<G, H> h
	) {
		return new Cascade8_2<>(this, c, d, e, f, g, h);
	}

	public final <C, D, E, F, G, H, I> CascadeArgument9<A, B, C, D, E, F, G, H, I> cascade(
			@NotNull final CascadeFunction<B, C> c,
			@NotNull final CascadeFunction<C, D> d,
			@NotNull final CascadeFunction<D, E> e,
			@NotNull final CascadeFunction<E, F> f,
			@NotNull final CascadeFunction<F, G> g,
			@NotNull final CascadeFunction<G, H> h,
			@NotNull final CascadeFunction<H, I> i
	) {
		return new Cascade9_2<>(this, c, d, e, f, g, h, i);
	}

	public final <C, D, E, F, G, H, I, J> CascadeArgument10<A, B, C, D, E, F, G, H, I, J> cascade(
			@NotNull final CascadeFunction<B, C> c,
			@NotNull final CascadeFunction<C, D> d,
			@NotNull final CascadeFunction<D, E> e,
			@NotNull final CascadeFunction<E, F> f,
			@NotNull final CascadeFunction<F, G> g,
			@NotNull final CascadeFunction<G, H> h,
			@NotNull final CascadeFunction<H, I> i,
			@NotNull final CascadeFunction<I, J> j
	) {
		return new Cascade10_2<>(this, c, d, e, f, g, h, i, j);
	}

	public final CommandArgument<B> lastValue() {
		return this.then(Pair::getValue1);
	}
}

@RequiredArgsConstructor final class Cascade2_1<A, B> extends CascadeArgument2<A, B> {
	private final @NotNull CommandArgument<A> A;
	private final @NotNull CascadeFunction<A, B> toB;

	@Override
	public Pair<A, B> parse(@NotNull final CommandExecution execution) throws CommandException {
		final A a = execution.get(A);
		final B b = execution.get(toB.apply(a));
		return Pair.with(a, b);
	}

	@Override
	public @NotNull Collection<String> complete(@NotNull final CommandExecution execution) throws CommandException {
		Collection<String> complete = Collections.emptyList();
		if (execution.remains() == 0) return complete;

		execution.checkpoint();
		complete = A.complete(execution);
		if (execution.remains() == 0) return complete;
		execution.restore();

		final CommandArgument<B> B = toB.apply(execution.get(A));
		return B.complete(execution);
	}
}

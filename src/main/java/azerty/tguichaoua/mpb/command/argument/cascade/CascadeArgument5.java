package azerty.tguichaoua.mpb.command.argument.cascade;

import azerty.tguichaoua.mpb.command.CommandException;
import azerty.tguichaoua.mpb.command.CommandExecution;
import azerty.tguichaoua.mpb.command.argument.CommandArgument;
import lombok.RequiredArgsConstructor;
import org.javatuples.Quintet;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;

@RequiredArgsConstructor
public final class CascadeArgument5<A, B, C, D, E> implements CommandArgument<Quintet<A, B, C, D, E>> {
	private final @NotNull CommandArgument<A> A;
	private final @NotNull CascadeFunction<A, B> toB;
	private final @NotNull CascadeFunction<B, C> toC;
	private final @NotNull CascadeFunction<C, D> toD;
	private final @NotNull CascadeFunction<D, E> toE;

	@Override
	public Quintet<A, B, C, D, E> parse(@NotNull final CommandExecution execution) throws CommandException {
		final A a = execution.get(A);
		final B b = execution.get(toB.apply(a));
		final C c = execution.get(toC.apply(b));
		final D d = execution.get(toD.apply(c));
		final E e = execution.get(toE.apply(d));
		return Quintet.with(a, b, c, d, e);
	}

	@Override
	public @NotNull Collection<String> complete(@NotNull final CommandExecution execution) throws CommandException {
		Collection<String> complete = Collections.emptyList();
		if (execution.remains() == 0) return complete;

		execution.checkpoint();
		complete = execution.complete(A);
		if (execution.remains() == 0) return complete;
		execution.restore();

		final CommandArgument<B> B = toB.apply(execution.get(A));
		execution.checkpoint();
		complete = execution.complete(B);
		if (execution.remains() == 0) return complete;
		execution.restore();

		final CommandArgument<C> C = toC.apply(execution.get(B));
		execution.checkpoint();
		complete = execution.complete(C);
		if (execution.remains() == 0) return complete;
		execution.restore();

		final CommandArgument<D> D = toD.apply(execution.get(C));
		execution.checkpoint();
		complete = execution.complete(D);
		if (execution.remains() == 0) return complete;
		execution.restore();

		final CommandArgument<E> E = toE.apply(execution.get(D));
		return execution.complete(E);
	}

	// --- Cascade -------------------------------------------------------
	public final <F> CascadeArgument6<A, B, C, D, E, F> cascade(
			@NotNull final CascadeFunction<E, F> f
	) {
		return new CascadeArgument6<>(A, toB, toC, toD, toE, f);
	}

	public final <F, G> CascadeArgument7<A, B, C, D, E, F, G> cascade(
			@NotNull final CascadeFunction<E, F> f,
			@NotNull final CascadeFunction<F, G> g
	) {
		return new CascadeArgument7<>(A, toB, toC, toD, toE, f, g);
	}

	public final <F, G, H> CascadeArgument8<A, B, C, D, E, F, G, H> cascade(
			@NotNull final CascadeFunction<E, F> f,
			@NotNull final CascadeFunction<F, G> g,
			@NotNull final CascadeFunction<G, H> h
	) {
		return new CascadeArgument8<>(A, toB, toC, toD, toE, f, g, h);
	}

	public final <F, G, H, I> CascadeArgument9<A, B, C, D, E, F, G, H, I> cascade(
			@NotNull final CascadeFunction<E, F> f,
			@NotNull final CascadeFunction<F, G> g,
			@NotNull final CascadeFunction<G, H> h,
			@NotNull final CascadeFunction<H, I> i
	) {
		return new CascadeArgument9<>(A, toB, toC, toD, toE, f, g, h, i);
	}

	public final <F, G, H, I, J> CascadeArgument10<A, B, C, D, E, F, G, H, I, J> cascade(
			@NotNull final CascadeFunction<E, F> f,
			@NotNull final CascadeFunction<F, G> g,
			@NotNull final CascadeFunction<G, H> h,
			@NotNull final CascadeFunction<H, I> i,
			@NotNull final CascadeFunction<I, J> j
	) {
		return new CascadeArgument10<>(A, toB, toC, toD, toE, f, g, h, i, j);
	}

	// --- Utils ------------------------------------------------------
	public final CommandArgument<E> lastValue() {
		return this.then(Quintet::getValue4);
	}
}

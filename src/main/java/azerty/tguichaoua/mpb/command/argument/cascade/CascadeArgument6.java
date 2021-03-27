package azerty.tguichaoua.mpb.command.argument.cascade;

import azerty.tguichaoua.mpb.command.CommandException;
import azerty.tguichaoua.mpb.command.CommandExecution;
import azerty.tguichaoua.mpb.command.argument.CommandArgument;
import lombok.RequiredArgsConstructor;
import org.javatuples.Sextet;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;

@RequiredArgsConstructor
public final class CascadeArgument6<A, B, C, D, E, F> implements CommandArgument<Sextet<A, B, C, D, E, F>> {
	private final @NotNull CommandArgument<A> A;
	private final @NotNull CascadeFunction<A, B> toB;
	private final @NotNull CascadeFunction<B, C> toC;
	private final @NotNull CascadeFunction<C, D> toD;
	private final @NotNull CascadeFunction<D, E> toE;
	private final @NotNull CascadeFunction<E, F> toF;

	@Override
	public Sextet<A, B, C, D, E, F> parse(@NotNull final CommandExecution execution) throws CommandException {
		final A a = execution.get(A);
		final B b = execution.get(toB.apply(a));
		final C c = execution.get(toC.apply(b));
		final D d = execution.get(toD.apply(c));
		final E e = execution.get(toE.apply(d));
		final F f = execution.get(toF.apply(e));
		return Sextet.with(a, b, c, d, e, f);
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
		execution.checkpoint();
		complete = B.complete(execution);
		if (execution.remains() == 0) return complete;
		execution.restore();

		final CommandArgument<C> C = toC.apply(execution.get(B));
		execution.checkpoint();
		complete = C.complete(execution);
		if (execution.remains() == 0) return complete;
		execution.restore();

		final CommandArgument<D> D = toD.apply(execution.get(C));
		execution.checkpoint();
		complete = D.complete(execution);
		if (execution.remains() == 0) return complete;
		execution.restore();

		final CommandArgument<E> E = toE.apply(execution.get(D));
		execution.checkpoint();
		complete = E.complete(execution);
		if (execution.remains() == 0) return complete;
		execution.restore();

		final CommandArgument<F> F = toF.apply(execution.get(E));
		return F.complete(execution);
	}

	// --- Cascade -------------------------------------------------------
	public final <G> CascadeArgument7<A, B, C, D, E, F, G> cascade(
			@NotNull final CascadeFunction<F, G> g
	) {
		return new CascadeArgument7<>(A, toB, toC, toD, toE, toF, g);
	}

	public final <G, H> CascadeArgument8<A, B, C, D, E, F, G, H> cascade(
			@NotNull final CascadeFunction<F, G> g,
			@NotNull final CascadeFunction<G, H> h
	) {
		return new CascadeArgument8<>(A, toB, toC, toD, toE, toF, g, h);
	}

	public final <G, H, I> CascadeArgument9<A, B, C, D, E, F, G, H, I> cascade(
			@NotNull final CascadeFunction<F, G> g,
			@NotNull final CascadeFunction<G, H> h,
			@NotNull final CascadeFunction<H, I> i
	) {
		return new CascadeArgument9<>(A, toB, toC, toD, toE, toF, g, h, i);
	}

	public final <G, H, I, J> CascadeArgument10<A, B, C, D, E, F, G, H, I, J> cascade(
			@NotNull final CascadeFunction<F, G> g,
			@NotNull final CascadeFunction<G, H> h,
			@NotNull final CascadeFunction<H, I> i,
			@NotNull final CascadeFunction<I, J> j
	) {
		return new CascadeArgument10<>(A, toB, toC, toD, toE, toF, g, h, i, j);
	}

	// --- Utils ------------------------------------------------------
	public final CommandArgument<F> lastValue() {
		return this.then(Sextet::getValue5);
	}
}

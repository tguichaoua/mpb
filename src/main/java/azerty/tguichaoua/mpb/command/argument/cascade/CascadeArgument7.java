package azerty.tguichaoua.mpb.command.argument.cascade;

import azerty.tguichaoua.mpb.command.CommandException;
import azerty.tguichaoua.mpb.command.CommandExecution;
import azerty.tguichaoua.mpb.command.argument.CommandArgument;
import lombok.RequiredArgsConstructor;
import org.javatuples.Septet;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;

@RequiredArgsConstructor
public final class CascadeArgument7<A, B, C, D, E, F, G> implements CommandArgument<Septet<A, B, C, D, E, F, G>> {
	private final @NotNull CommandArgument<A> A;
	private final @NotNull CascadeFunction<A, B> toB;
	private final @NotNull CascadeFunction<B, C> toC;
	private final @NotNull CascadeFunction<C, D> toD;
	private final @NotNull CascadeFunction<D, E> toE;
	private final @NotNull CascadeFunction<E, F> toF;
	private final @NotNull CascadeFunction<F, G> toG;

	@Override
	public Septet<A, B, C, D, E, F, G> parse(@NotNull final CommandExecution execution) throws CommandException {
		final A a = execution.get(A);
		final B b = execution.get(toB.apply(a));
		final C c = execution.get(toC.apply(b));
		final D d = execution.get(toD.apply(c));
		final E e = execution.get(toE.apply(d));
		final F f = execution.get(toF.apply(e));
		final G g = execution.get(toG.apply(f));
		return Septet.with(a, b, c, d, e, f, g);
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
		execution.checkpoint();
		complete = F.complete(execution);
		if (execution.remains() == 0) return complete;
		execution.restore();

		final CommandArgument<G> G = toG.apply(execution.get(F));
		return G.complete(execution);
	}

	// --- Cascade -------------------------------------------------------
	public final <H> CascadeArgument8<A, B, C, D, E, F, G, H> cascade(
			@NotNull final CascadeFunction<G, H> h
	) {
		return new CascadeArgument8<>(A, toB, toC, toD, toE, toF, toG, h);
	}

	public final <H, I> CascadeArgument9<A, B, C, D, E, F, G, H, I> cascade(
			@NotNull final CascadeFunction<G, H> h,
			@NotNull final CascadeFunction<H, I> i
	) {
		return new CascadeArgument9<>(A, toB, toC, toD, toE, toF, toG, h, i);
	}

	public final <H, I, J> CascadeArgument10<A, B, C, D, E, F, G, H, I, J> cascade(
			@NotNull final CascadeFunction<G, H> h,
			@NotNull final CascadeFunction<H, I> i,
			@NotNull final CascadeFunction<I, J> j
	) {
		return new CascadeArgument10<>(A, toB, toC, toD, toE, toF, toG, h, i, j);
	}

	// --- Utils ------------------------------------------------------
	public final CommandArgument<G> lastValue() {
		return this.then(Septet::getValue6);
	}
}

package azerty.tguichaoua.mpb.command.argument.cascade;

import azerty.tguichaoua.mpb.command.CommandException;
import azerty.tguichaoua.mpb.command.CommandExecution;
import azerty.tguichaoua.mpb.command.argument.CommandArgument;
import lombok.RequiredArgsConstructor;
import org.javatuples.Octet;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;

@RequiredArgsConstructor
public final class CascadeArgument8<A, B, C, D, E, F, G, H> implements CommandArgument<Octet<A, B, C, D, E, F, G, H>> {
	private final @NotNull CommandArgument<A> A;
	private final @NotNull CascadeFunction<A, B> toB;
	private final @NotNull CascadeFunction<B, C> toC;
	private final @NotNull CascadeFunction<C, D> toD;
	private final @NotNull CascadeFunction<D, E> toE;
	private final @NotNull CascadeFunction<E, F> toF;
	private final @NotNull CascadeFunction<F, G> toG;
	private final @NotNull CascadeFunction<G, H> toH;

	@Override
	public Octet<A, B, C, D, E, F, G, H> parse(@NotNull final CommandExecution execution) throws CommandException {
		final A a = execution.get(A);
		final B b = execution.get(toB.apply(a));
		final C c = execution.get(toC.apply(b));
		final D d = execution.get(toD.apply(c));
		final E e = execution.get(toE.apply(d));
		final F f = execution.get(toF.apply(e));
		final G g = execution.get(toG.apply(f));
		final H h = execution.get(toH.apply(g));
		return Octet.with(a, b, c, d, e, f, g, h);
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
		execution.checkpoint();
		complete = G.complete(execution);
		if (execution.remains() == 0) return complete;
		execution.restore();

		final CommandArgument<H> H = toH.apply(execution.get(G));
		return H.complete(execution);
	}

	// --- Cascade -------------------------------------------------------
	public final <I> CascadeArgument9<A, B, C, D, E, F, G, H, I> cascade(
			@NotNull final CascadeFunction<H, I> i
	) {
		return new CascadeArgument9<>(A, toB, toC, toD, toE, toF, toG, toH, i);
	}

	public final <I, J> CascadeArgument10<A, B, C, D, E, F, G, H, I, J> cascade(
			@NotNull final CascadeFunction<H, I> i,
			@NotNull final CascadeFunction<I, J> j
	) {
		return new CascadeArgument10<>(A, toB, toC, toD, toE, toF, toG, toH, i, j);
	}

	// --- Utils ------------------------------------------------------
	public final CommandArgument<H> lastValue() {
		return this.then(Octet::getValue7);
	}
}

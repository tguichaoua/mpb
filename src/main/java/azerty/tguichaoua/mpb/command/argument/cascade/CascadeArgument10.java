package azerty.tguichaoua.mpb.command.argument.cascade;

import azerty.tguichaoua.mpb.command.CommandException;
import azerty.tguichaoua.mpb.command.CommandExecution;
import azerty.tguichaoua.mpb.command.argument.CommandArgument;
import lombok.RequiredArgsConstructor;
import org.javatuples.Decade;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;

@RequiredArgsConstructor
public final class CascadeArgument10<A, B, C, D, E, F, G, H, I, J> implements CommandArgument<Decade<A, B, C, D, E, F, G, H, I, J>> {
	private final @NotNull CommandArgument<A> A;
	private final @NotNull CascadeFunction<A, B> toB;
	private final @NotNull CascadeFunction<B, C> toC;
	private final @NotNull CascadeFunction<C, D> toD;
	private final @NotNull CascadeFunction<D, E> toE;
	private final @NotNull CascadeFunction<E, F> toF;
	private final @NotNull CascadeFunction<F, G> toG;
	private final @NotNull CascadeFunction<G, H> toH;
	private final @NotNull CascadeFunction<H, I> toI;
	private final @NotNull CascadeFunction<I, J> toJ;

	@Override
	public Decade<A, B, C, D, E, F, G, H, I, J> parse(@NotNull final CommandExecution execution) throws CommandException {
		final A a = execution.get(A);
		final B b = execution.get(toB.apply(a));
		final C c = execution.get(toC.apply(b));
		final D d = execution.get(toD.apply(c));
		final E e = execution.get(toE.apply(d));
		final F f = execution.get(toF.apply(e));
		final G g = execution.get(toG.apply(f));
		final H h = execution.get(toH.apply(g));
		final I i = execution.get(toI.apply(h));
		final J j = execution.get(toJ.apply(i));
		return Decade.with(a, b, c, d, e, f, g, h, i, j);
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
		execution.checkpoint();
		complete = execution.complete(E);
		if (execution.remains() == 0) return complete;
		execution.restore();

		final CommandArgument<F> F = toF.apply(execution.get(E));
		execution.checkpoint();
		complete = execution.complete(F);
		if (execution.remains() == 0) return complete;
		execution.restore();

		final CommandArgument<G> G = toG.apply(execution.get(F));
		execution.checkpoint();
		complete = execution.complete(F);
		if (execution.remains() == 0) return complete;
		execution.restore();

		final CommandArgument<H> H = toH.apply(execution.get(G));
		execution.checkpoint();
		complete = execution.complete(H);
		if (execution.remains() == 0) return complete;
		execution.restore();

		final CommandArgument<I> I = toI.apply(execution.get(H));
		execution.checkpoint();
		complete = execution.complete(I);
		if (execution.remains() == 0) return complete;
		execution.restore();

		final CommandArgument<J> J = toJ.apply(execution.get(I));
		return execution.complete(J);
	}

	// --- Utils ------------------------------------------------------
	public final CommandArgument<J> lastValue() {
		return this.then(Decade::getValue9);
	}
}

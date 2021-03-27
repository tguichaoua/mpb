package azerty.tguichaoua.mpb.command.argument.cascade;

import azerty.tguichaoua.mpb.command.CommandException;
import azerty.tguichaoua.mpb.command.CommandExecution;
import azerty.tguichaoua.mpb.command.argument.CommandArgument;
import lombok.RequiredArgsConstructor;
import org.javatuples.*;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;

public abstract class CascadeArgument7<A, B, C, D, E, F, G> implements CommandArgument<Septet<A, B, C, D, E, F, G>> {
	public final <H> CascadeArgument8<A, B, C, D, E, F, G, H> cascade(
			@NotNull final CascadeFunction<G, H> h
	) {
		return new Cascade8_7<>(this, h);
	}

	public final <H, I> CascadeArgument9<A, B, C, D, E, F, G, H, I> cascade(
			@NotNull final CascadeFunction<G, H> h,
			@NotNull final CascadeFunction<H, I> i
	) {
		return new Cascade9_7<>(this, h, i);
	}

	public final <H, I, J> CascadeArgument10<A, B, C, D, E, F, G, H, I, J> cascade(
			@NotNull final CascadeFunction<G, H> h,
			@NotNull final CascadeFunction<H, I> i,
			@NotNull final CascadeFunction<I, J> j
	) {
		return new Cascade10_7<>(this, h, i, j);
	}

	public final CommandArgument<G> lastValue() {
		return this.then(Septet::getValue6);
	}
}

@RequiredArgsConstructor final class Cascade7_1<A, B, C, D, E, F, G> extends CascadeArgument7<A, B, C, D, E, F, G> {
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
}

@RequiredArgsConstructor final class Cascade7_2<A, B, C, D, E, F, G> extends CascadeArgument7<A, B, C, D, E, F, G> {
	private final @NotNull CascadeArgument2<A, B> AB;
	private final @NotNull CascadeFunction<B, C> toC;
	private final @NotNull CascadeFunction<C, D> toD;
	private final @NotNull CascadeFunction<D, E> toE;
	private final @NotNull CascadeFunction<E, F> toF;
	private final @NotNull CascadeFunction<F, G> toG;

	@Override
	public Septet<A, B, C, D, E, F, G> parse(@NotNull final CommandExecution execution) throws CommandException {
		final Pair<A, B> ab = execution.get(AB);
		final C c = execution.get(toC.apply(ab.getValue1()));
		final D d = execution.get(toD.apply(c));
		final E e = execution.get(toE.apply(d));
		final F f = execution.get(toF.apply(e));
		final G g = execution.get(toG.apply(f));
		return ab.add(c, d, e, f, g);
	}

	@Override
	public @NotNull Collection<String> complete(@NotNull final CommandExecution execution) throws CommandException {
		Collection<String> complete = Collections.emptyList();
		if (execution.remains() == 0) return complete;

		execution.checkpoint();
		complete = AB.complete(execution);
		if (execution.remains() == 0) return complete;
		execution.restore();

		final CommandArgument<C> C = toC.apply(execution.get(AB).getValue1());
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
}

@RequiredArgsConstructor final class Cascade7_3<A, B, C, D, E, F, G> extends CascadeArgument7<A, B, C, D, E, F, G> {
	private final @NotNull CascadeArgument3<A, B, C> ABC;
	private final @NotNull CascadeFunction<C, D> toD;
	private final @NotNull CascadeFunction<D, E> toE;
	private final @NotNull CascadeFunction<E, F> toF;
	private final @NotNull CascadeFunction<F, G> toG;

	@Override
	public Septet<A, B, C, D, E, F, G> parse(@NotNull final CommandExecution execution) throws CommandException {
		final Triplet<A, B, C> abc = execution.get(ABC);
		final D d = execution.get(toD.apply(abc.getValue2()));
		final E e = execution.get(toE.apply(d));
		final F f = execution.get(toF.apply(e));
		final G g = execution.get(toG.apply(f));
		return abc.add(d, e, f, g);
	}

	@Override
	public @NotNull Collection<String> complete(@NotNull final CommandExecution execution) throws CommandException {
		Collection<String> complete = Collections.emptyList();
		if (execution.remains() == 0) return complete;

		execution.checkpoint();
		complete = ABC.complete(execution);
		if (execution.remains() == 0) return complete;
		execution.restore();

		final CommandArgument<D> D = toD.apply(execution.get(ABC).getValue2());
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
}

@RequiredArgsConstructor final class Cascade7_4<A, B, C, D, E, F, G> extends CascadeArgument7<A, B, C, D, E, F, G> {
	private final @NotNull CascadeArgument4<A, B, C, D> ABCD;
	private final @NotNull CascadeFunction<D, E> toE;
	private final @NotNull CascadeFunction<E, F> toF;
	private final @NotNull CascadeFunction<F, G> toG;

	@Override
	public Septet<A, B, C, D, E, F, G> parse(@NotNull final CommandExecution execution) throws CommandException {
		final Quartet<A, B, C, D> abcd = execution.get(ABCD);
		final E e = execution.get(toE.apply(abcd.getValue3()));
		final F f = execution.get(toF.apply(e));
		final G g = execution.get(toG.apply(f));
		return abcd.add(e, f, g);
	}

	@Override
	public @NotNull Collection<String> complete(@NotNull final CommandExecution execution) throws CommandException {
		Collection<String> complete = Collections.emptyList();
		if (execution.remains() == 0) return complete;

		execution.checkpoint();
		complete = ABCD.complete(execution);
		if (execution.remains() == 0) return complete;
		execution.restore();

		final CommandArgument<E> E = toE.apply(execution.get(ABCD).getValue3());
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
}

@RequiredArgsConstructor final class Cascade7_5<A, B, C, D, E, F, G> extends CascadeArgument7<A, B, C, D, E, F, G> {
	private final @NotNull CascadeArgument5<A, B, C, D, E> ABCDE;
	private final @NotNull CascadeFunction<E, F> toF;
	private final @NotNull CascadeFunction<F, G> toG;

	@Override
	public Septet<A, B, C, D, E, F, G> parse(@NotNull final CommandExecution execution) throws CommandException {
		final Quintet<A, B, C, D, E> abcde = execution.get(ABCDE);
		final F f = execution.get(toF.apply(abcde.getValue4()));
		final G g = execution.get(toG.apply(f));
		return abcde.add(f, g);
	}

	@Override
	public @NotNull Collection<String> complete(@NotNull final CommandExecution execution) throws CommandException {
		Collection<String> complete = Collections.emptyList();
		if (execution.remains() == 0) return complete;

		execution.checkpoint();
		complete = ABCDE.complete(execution);
		if (execution.remains() == 0) return complete;
		execution.restore();

		final CommandArgument<F> F = toF.apply(execution.get(ABCDE).getValue4());
		execution.checkpoint();
		complete = F.complete(execution);
		if (execution.remains() == 0) return complete;
		execution.restore();

		final CommandArgument<G> G = toG.apply(execution.get(F));
		return G.complete(execution);
	}
}

@RequiredArgsConstructor final class Cascade7_6<A, B, C, D, E, F, G> extends CascadeArgument7<A, B, C, D, E, F, G> {
	private final @NotNull CascadeArgument6<A, B, C, D, E, F> ABCDEF;
	private final @NotNull CascadeFunction<F, G> toG;

	@Override
	public Septet<A, B, C, D, E, F, G> parse(@NotNull final CommandExecution execution) throws CommandException {
		final Sextet<A, B, C, D, E, F> abcdef = execution.get(ABCDEF);
		final G g = execution.get(toG.apply(abcdef.getValue5()));
		return abcdef.add(g);
	}

	@Override
	public @NotNull Collection<String> complete(@NotNull final CommandExecution execution) throws CommandException {
		Collection<String> complete = Collections.emptyList();
		if (execution.remains() == 0) return complete;

		execution.checkpoint();
		complete = ABCDEF.complete(execution);
		if (execution.remains() == 0) return complete;
		execution.restore();

		final CommandArgument<G> G = toG.apply(execution.get(ABCDEF).getValue5());
		return G.complete(execution);
	}
}

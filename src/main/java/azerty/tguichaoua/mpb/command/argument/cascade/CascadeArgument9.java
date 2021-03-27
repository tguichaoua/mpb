package azerty.tguichaoua.mpb.command.argument.cascade;

import azerty.tguichaoua.mpb.command.CommandException;
import azerty.tguichaoua.mpb.command.CommandExecution;
import azerty.tguichaoua.mpb.command.argument.CommandArgument;
import lombok.RequiredArgsConstructor;
import org.javatuples.*;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;

public abstract class CascadeArgument9<A, B, C, D, E, F, G, H, I> implements CommandArgument<Ennead<A, B, C, D, E, F, G, H, I>> {
	public final <J> CascadeArgument10<A, B, C, D, E, F, G, H, I, J> cascade(
			@NotNull final CascadeFunction<I, J> j
	) {
		return new Cascade10_9<>(this, j);
	}

	public final CommandArgument<I> lastValue() {
		return this.then(Ennead::getValue8);
	}
}

@RequiredArgsConstructor final class Cascade9_1<A, B, C, D, E, F, G, H, I> extends CascadeArgument9<A, B, C, D, E, F, G, H, I> {
	private final @NotNull CommandArgument<A> A;
	private final @NotNull CascadeFunction<A, B> toB;
	private final @NotNull CascadeFunction<B, C> toC;
	private final @NotNull CascadeFunction<C, D> toD;
	private final @NotNull CascadeFunction<D, E> toE;
	private final @NotNull CascadeFunction<E, F> toF;
	private final @NotNull CascadeFunction<F, G> toG;
	private final @NotNull CascadeFunction<G, H> toH;
	private final @NotNull CascadeFunction<H, I> toI;

	@Override
	public Ennead<A, B, C, D, E, F, G, H, I> parse(@NotNull final CommandExecution execution) throws CommandException {
		final A a = execution.get(A);
		final B b = execution.get(toB.apply(a));
		final C c = execution.get(toC.apply(b));
		final D d = execution.get(toD.apply(c));
		final E e = execution.get(toE.apply(d));
		final F f = execution.get(toF.apply(e));
		final G g = execution.get(toG.apply(f));
		final H h = execution.get(toH.apply(g));
		final I i = execution.get(toI.apply(h));
		return Ennead.with(a, b, c, d, e, f, g, h, i);
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
		execution.checkpoint();
		complete = H.complete(execution);
		if (execution.remains() == 0) return complete;
		execution.restore();

		final CommandArgument<I> I = toI.apply(execution.get(H));
		return I.complete(execution);
	}
}

@RequiredArgsConstructor final class Cascade9_2<A, B, C, D, E, F, G, H, I> extends CascadeArgument9<A, B, C, D, E, F, G, H, I> {
	private final @NotNull CascadeArgument2<A, B> AB;
	private final @NotNull CascadeFunction<B, C> toC;
	private final @NotNull CascadeFunction<C, D> toD;
	private final @NotNull CascadeFunction<D, E> toE;
	private final @NotNull CascadeFunction<E, F> toF;
	private final @NotNull CascadeFunction<F, G> toG;
	private final @NotNull CascadeFunction<G, H> toH;
	private final @NotNull CascadeFunction<H, I> toI;

	@Override
	public Ennead<A, B, C, D, E, F, G, H, I> parse(@NotNull final CommandExecution execution) throws CommandException {
		final Pair<A, B> ab = execution.get(AB);
		final C c = execution.get(toC.apply(ab.getValue1()));
		final D d = execution.get(toD.apply(c));
		final E e = execution.get(toE.apply(d));
		final F f = execution.get(toF.apply(e));
		final G g = execution.get(toG.apply(f));
		final H h = execution.get(toH.apply(g));
		final I i = execution.get(toI.apply(h));
		return ab.add(c, d, e, f, g, h, i);
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
		execution.checkpoint();
		complete = G.complete(execution);
		if (execution.remains() == 0) return complete;
		execution.restore();

		final CommandArgument<H> H = toH.apply(execution.get(G));
		execution.checkpoint();
		complete = H.complete(execution);
		if (execution.remains() == 0) return complete;
		execution.restore();

		final CommandArgument<I> I = toI.apply(execution.get(H));
		return I.complete(execution);
	}
}

@RequiredArgsConstructor final class Cascade9_3<A, B, C, D, E, F, G, H, I> extends CascadeArgument9<A, B, C, D, E, F, G, H, I> {
	private final @NotNull CascadeArgument3<A, B, C> ABC;
	private final @NotNull CascadeFunction<C, D> toD;
	private final @NotNull CascadeFunction<D, E> toE;
	private final @NotNull CascadeFunction<E, F> toF;
	private final @NotNull CascadeFunction<F, G> toG;
	private final @NotNull CascadeFunction<G, H> toH;
	private final @NotNull CascadeFunction<H, I> toI;

	@Override
	public Ennead<A, B, C, D, E, F, G, H, I> parse(@NotNull final CommandExecution execution) throws CommandException {
		final Triplet<A, B, C> abc = execution.get(ABC);
		final D d = execution.get(toD.apply(abc.getValue2()));
		final E e = execution.get(toE.apply(d));
		final F f = execution.get(toF.apply(e));
		final G g = execution.get(toG.apply(f));
		final H h = execution.get(toH.apply(g));
		final I i = execution.get(toI.apply(h));
		return abc.add(d, e, f, g, h, i);
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
		execution.checkpoint();
		complete = G.complete(execution);
		if (execution.remains() == 0) return complete;
		execution.restore();

		final CommandArgument<H> H = toH.apply(execution.get(G));
		execution.checkpoint();
		complete = H.complete(execution);
		if (execution.remains() == 0) return complete;
		execution.restore();

		final CommandArgument<I> I = toI.apply(execution.get(H));
		return I.complete(execution);
	}
}

@RequiredArgsConstructor final class Cascade9_4<A, B, C, D, E, F, G, H, I> extends CascadeArgument9<A, B, C, D, E, F, G, H, I> {
	private final @NotNull CascadeArgument4<A, B, C, D> ABCD;
	private final @NotNull CascadeFunction<D, E> toE;
	private final @NotNull CascadeFunction<E, F> toF;
	private final @NotNull CascadeFunction<F, G> toG;
	private final @NotNull CascadeFunction<G, H> toH;
	private final @NotNull CascadeFunction<H, I> toI;

	@Override
	public Ennead<A, B, C, D, E, F, G, H, I> parse(@NotNull final CommandExecution execution) throws CommandException {
		final Quartet<A, B, C, D> abcd = execution.get(ABCD);
		final E e = execution.get(toE.apply(abcd.getValue3()));
		final F f = execution.get(toF.apply(e));
		final G g = execution.get(toG.apply(f));
		final H h = execution.get(toH.apply(g));
		final I i = execution.get(toI.apply(h));
		return abcd.add(e, f, g, h, i);
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
		execution.checkpoint();
		complete = G.complete(execution);
		if (execution.remains() == 0) return complete;
		execution.restore();

		final CommandArgument<H> H = toH.apply(execution.get(G));
		execution.checkpoint();
		complete = H.complete(execution);
		if (execution.remains() == 0) return complete;
		execution.restore();

		final CommandArgument<I> I = toI.apply(execution.get(H));
		return I.complete(execution);
	}
}

@RequiredArgsConstructor final class Cascade9_5<A, B, C, D, E, F, G, H, I> extends CascadeArgument9<A, B, C, D, E, F, G, H, I> {
	private final @NotNull CascadeArgument5<A, B, C, D, E> ABCDE;
	private final @NotNull CascadeFunction<E, F> toF;
	private final @NotNull CascadeFunction<F, G> toG;
	private final @NotNull CascadeFunction<G, H> toH;
	private final @NotNull CascadeFunction<H, I> toI;

	@Override
	public Ennead<A, B, C, D, E, F, G, H, I> parse(@NotNull final CommandExecution execution) throws CommandException {
		final Quintet<A, B, C, D, E> abcde = execution.get(ABCDE);
		final F f = execution.get(toF.apply(abcde.getValue4()));
		final G g = execution.get(toG.apply(f));
		final H h = execution.get(toH.apply(g));
		final I i = execution.get(toI.apply(h));
		return abcde.add(f, g, h, i);
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
		execution.checkpoint();
		complete = G.complete(execution);
		if (execution.remains() == 0) return complete;
		execution.restore();

		final CommandArgument<H> H = toH.apply(execution.get(G));
		execution.checkpoint();
		complete = H.complete(execution);
		if (execution.remains() == 0) return complete;
		execution.restore();

		final CommandArgument<I> I = toI.apply(execution.get(H));
		return I.complete(execution);
	}
}

@RequiredArgsConstructor final class Cascade9_6<A, B, C, D, E, F, G, H, I> extends CascadeArgument9<A, B, C, D, E, F, G, H, I> {
	private final @NotNull CascadeArgument6<A, B, C, D, E, F> ABCDEF;
	private final @NotNull CascadeFunction<F, G> toG;
	private final @NotNull CascadeFunction<G, H> toH;
	private final @NotNull CascadeFunction<H, I> toI;

	@Override
	public Ennead<A, B, C, D, E, F, G, H, I> parse(@NotNull final CommandExecution execution) throws CommandException {
		final Sextet<A, B, C, D, E, F> abcdef = execution.get(ABCDEF);
		final G g = execution.get(toG.apply(abcdef.getValue5()));
		final H h = execution.get(toH.apply(g));
		final I i = execution.get(toI.apply(h));
		return abcdef.add(g, h, i);
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
		execution.checkpoint();
		complete = G.complete(execution);
		if (execution.remains() == 0) return complete;
		execution.restore();

		final CommandArgument<H> H = toH.apply(execution.get(G));
		execution.checkpoint();
		complete = H.complete(execution);
		if (execution.remains() == 0) return complete;
		execution.restore();

		final CommandArgument<I> I = toI.apply(execution.get(H));
		return I.complete(execution);
	}
}

@RequiredArgsConstructor final class Cascade9_7<A, B, C, D, E, F, G, H, I> extends CascadeArgument9<A, B, C, D, E, F, G, H, I> {
	private final @NotNull CascadeArgument7<A, B, C, D, E, F, G> ABCDEFG;
	private final @NotNull CascadeFunction<G, H> toH;
	private final @NotNull CascadeFunction<H, I> toI;

	@Override
	public Ennead<A, B, C, D, E, F, G, H, I> parse(@NotNull final CommandExecution execution) throws CommandException {
		final Septet<A, B, C, D, E, F, G> abcdefg = execution.get(ABCDEFG);
		final H h = execution.get(toH.apply(abcdefg.getValue6()));
		final I i = execution.get(toI.apply(h));
		return abcdefg.add(h, i);
	}

	@Override
	public @NotNull Collection<String> complete(@NotNull final CommandExecution execution) throws CommandException {
		Collection<String> complete = Collections.emptyList();
		if (execution.remains() == 0) return complete;

		execution.checkpoint();
		complete = ABCDEFG.complete(execution);
		if (execution.remains() == 0) return complete;
		execution.restore();

		final CommandArgument<H> H = toH.apply(execution.get(ABCDEFG).getValue6());
		execution.checkpoint();
		complete = H.complete(execution);
		if (execution.remains() == 0) return complete;
		execution.restore();

		final CommandArgument<I> I = toI.apply(execution.get(H));
		return I.complete(execution);
	}
}

@RequiredArgsConstructor final class Cascade9_8<A, B, C, D, E, F, G, H, I> extends CascadeArgument9<A, B, C, D, E, F, G, H, I> {
	private final @NotNull CascadeArgument8<A, B, C, D, E, F, G, H> ABCDEFGH;
	private final @NotNull CascadeFunction<H, I> toI;

	@Override
	public Ennead<A, B, C, D, E, F, G, H, I> parse(@NotNull final CommandExecution execution) throws CommandException {
		final Octet<A, B, C, D, E, F, G, H> abcdefgh = execution.get(ABCDEFGH);
		final I i = execution.get(toI.apply(abcdefgh.getValue7()));
		return abcdefgh.add(i);
	}

	@Override
	public @NotNull Collection<String> complete(@NotNull final CommandExecution execution) throws CommandException {
		Collection<String> complete = Collections.emptyList();
		if (execution.remains() == 0) return complete;

		execution.checkpoint();
		complete = ABCDEFGH.complete(execution);
		if (execution.remains() == 0) return complete;
		execution.restore();

		final CommandArgument<I> I = toI.apply(execution.get(ABCDEFGH).getValue7());
		return I.complete(execution);
	}
}

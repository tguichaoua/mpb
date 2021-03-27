package azerty.tguichaoua.mpb.command.argument.cascade;

import azerty.tguichaoua.mpb.command.CommandException;
import azerty.tguichaoua.mpb.command.CommandExecution;
import azerty.tguichaoua.mpb.command.argument.CommandArgument;
import lombok.RequiredArgsConstructor;
import org.javatuples.Pair;
import org.javatuples.Quartet;
import org.javatuples.Quintet;
import org.javatuples.Triplet;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;

public abstract class CascadeArgument5<A, B, C, D, E> implements CommandArgument<Quintet<A, B, C, D, E>> {
	public final <F> CascadeArgument6<A, B, C, D, E, F> cascade(
			@NotNull final CascadeFunction<E, F> f
	) {
		return new Cascade6_5<>(this, f);
	}

	public final <F, G> CascadeArgument7<A, B, C, D, E, F, G> cascade(
			@NotNull final CascadeFunction<E, F> f,
			@NotNull final CascadeFunction<F, G> g
	) {
		return new Cascade7_5<>(this, f, g);
	}

	public final <F, G, H> CascadeArgument8<A, B, C, D, E, F, G, H> cascade(
			@NotNull final CascadeFunction<E, F> f,
			@NotNull final CascadeFunction<F, G> g,
			@NotNull final CascadeFunction<G, H> h
	) {
		return new Cascade8_5<>(this, f, g, h);
	}

	public final <F, G, H, I> CascadeArgument9<A, B, C, D, E, F, G, H, I> cascade(
			@NotNull final CascadeFunction<E, F> f,
			@NotNull final CascadeFunction<F, G> g,
			@NotNull final CascadeFunction<G, H> h,
			@NotNull final CascadeFunction<H, I> i
	) {
		return new Cascade9_5<>(this, f, g, h, i);
	}

	public final <F, G, H, I, J> CascadeArgument10<A, B, C, D, E, F, G, H, I, J> cascade(
			@NotNull final CascadeFunction<E, F> f,
			@NotNull final CascadeFunction<F, G> g,
			@NotNull final CascadeFunction<G, H> h,
			@NotNull final CascadeFunction<H, I> i,
			@NotNull final CascadeFunction<I, J> j
	) {
		return new Cascade10_5<>(this, f, g, h, i, j);
	}

	public final CommandArgument<E> lastValue() {
		return this.then(Quintet::getValue4);
	}
}

@RequiredArgsConstructor final class Cascade5_1<A, B, C, D, E> extends CascadeArgument5<A, B, C, D, E> {
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
		return E.complete(execution);
	}
}

@RequiredArgsConstructor final class Cascade5_2<A, B, C, D, E> extends CascadeArgument5<A, B, C, D, E> {
	private final @NotNull CascadeArgument2<A, B> AB;
	private final @NotNull CascadeFunction<B, C> toC;
	private final @NotNull CascadeFunction<C, D> toD;
	private final @NotNull CascadeFunction<D, E> toE;

	@Override
	public Quintet<A, B, C, D, E> parse(@NotNull final CommandExecution execution) throws CommandException {
		final Pair<A, B> ab = execution.get(AB);
		final C c = execution.get(toC.apply(ab.getValue1()));
		final D d = execution.get(toD.apply(c));
		final E e = execution.get(toE.apply(d));
		return ab.add(c, d, e);
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
		return E.complete(execution);
	}
}

@RequiredArgsConstructor final class Cascade5_3<A, B, C, D, E> extends CascadeArgument5<A, B, C, D, E> {
	private final @NotNull CascadeArgument3<A, B, C> ABC;
	private final @NotNull CascadeFunction<C, D> toD;
	private final @NotNull CascadeFunction<D, E> toE;

	@Override
	public Quintet<A, B, C, D, E> parse(@NotNull final CommandExecution execution) throws CommandException {
		final Triplet<A, B, C> abc = execution.get(ABC);
		final D d = execution.get(toD.apply(abc.getValue2()));
		final E e = execution.get(toE.apply(d));
		return abc.add(d, e);
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
		return E.complete(execution);
	}
}

@RequiredArgsConstructor final class Cascade5_4<A, B, C, D, E> extends CascadeArgument5<A, B, C, D, E> {
	private final @NotNull CascadeArgument4<A, B, C, D> ABCD;
	private final @NotNull CascadeFunction<D, E> toE;

	@Override
	public Quintet<A, B, C, D, E> parse(@NotNull final CommandExecution execution) throws CommandException {
		final Quartet<A, B, C, D> abcd = execution.get(ABCD);
		final E e = execution.get(toE.apply(abcd.getValue3()));
		return abcd.add(e);
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
		return E.complete(execution);
	}
}
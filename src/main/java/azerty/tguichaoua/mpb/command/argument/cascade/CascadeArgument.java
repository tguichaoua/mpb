package azerty.tguichaoua.mpb.command.argument.cascade;

import azerty.tguichaoua.mpb.command.argument.CommandArgument;
import org.jetbrains.annotations.NotNull;

public class CascadeArgument {
	public static <A, B> CascadeArgument2<A, B> of(
			@NotNull final CommandArgument<A> a,
			@NotNull final CascadeFunction<A, B> b
	) {
		return new CascadeArgument2<>(a, b);
	}

	public static <A, B, C> CascadeArgument3<A, B, C> of(
			@NotNull final CommandArgument<A> a,
			@NotNull final CascadeFunction<A, B> b,
			@NotNull final CascadeFunction<B, C> c
	) {
		return new CascadeArgument3<>(a, b, c);
	}

	public static <A, B, C, D> CascadeArgument4<A, B, C, D> of(
			@NotNull final CommandArgument<A> a,
			@NotNull final CascadeFunction<A, B> b,
			@NotNull final CascadeFunction<B, C> c,
			@NotNull final CascadeFunction<C, D> d
	) {
		return new CascadeArgument4<>(a, b, c, d);
	}

	public static <A, B, C, D, E> CascadeArgument5<A, B, C, D, E> of(
			@NotNull final CommandArgument<A> a,
			@NotNull final CascadeFunction<A, B> b,
			@NotNull final CascadeFunction<B, C> c,
			@NotNull final CascadeFunction<C, D> d,
			@NotNull final CascadeFunction<D, E> e
	) {
		return new CascadeArgument5<>(a, b, c, d, e);
	}

	public static <A, B, C, D, E, F> CascadeArgument6<A, B, C, D, E, F> of(
			@NotNull final CommandArgument<A> a,
			@NotNull final CascadeFunction<A, B> b,
			@NotNull final CascadeFunction<B, C> c,
			@NotNull final CascadeFunction<C, D> d,
			@NotNull final CascadeFunction<D, E> e,
			@NotNull final CascadeFunction<E, F> f
	) {
		return new CascadeArgument6<>(a, b, c, d, e, f);
	}

	public static <A, B, C, D, E, F, G> CascadeArgument7<A, B, C, D, E, F, G> of(
			@NotNull final CommandArgument<A> a,
			@NotNull final CascadeFunction<A, B> b,
			@NotNull final CascadeFunction<B, C> c,
			@NotNull final CascadeFunction<C, D> d,
			@NotNull final CascadeFunction<D, E> e,
			@NotNull final CascadeFunction<E, F> f,
			@NotNull final CascadeFunction<F, G> g
	) {
		return new CascadeArgument7<>(a, b, c, d, e, f, g);
	}

	public static <A, B, C, D, E, F, G, H> CascadeArgument8<A, B, C, D, E, F, G, H> of(
			@NotNull final CommandArgument<A> a,
			@NotNull final CascadeFunction<A, B> b,
			@NotNull final CascadeFunction<B, C> c,
			@NotNull final CascadeFunction<C, D> d,
			@NotNull final CascadeFunction<D, E> e,
			@NotNull final CascadeFunction<E, F> f,
			@NotNull final CascadeFunction<F, G> g,
			@NotNull final CascadeFunction<G, H> h
	) {
		return new CascadeArgument8<>(a, b, c, d, e, f, g, h);
	}

	public static <A, B, C, D, E, F, G, H, I> CascadeArgument9<A, B, C, D, E, F, G, H, I> of(
			@NotNull final CommandArgument<A> a,
			@NotNull final CascadeFunction<A, B> b,
			@NotNull final CascadeFunction<B, C> c,
			@NotNull final CascadeFunction<C, D> d,
			@NotNull final CascadeFunction<D, E> e,
			@NotNull final CascadeFunction<E, F> f,
			@NotNull final CascadeFunction<F, G> g,
			@NotNull final CascadeFunction<G, H> h,
			@NotNull final CascadeFunction<H, I> i
	) {
		return new CascadeArgument9<>(a, b, c, d, e, f, g, h, i);
	}

	public static <A, B, C, D, E, F, G, H, I, J> CascadeArgument10<A, B, C, D, E, F, G, H, I, J> of(
			@NotNull final CommandArgument<A> a,
			@NotNull final CascadeFunction<A, B> b,
			@NotNull final CascadeFunction<B, C> c,
			@NotNull final CascadeFunction<C, D> d,
			@NotNull final CascadeFunction<D, E> e,
			@NotNull final CascadeFunction<E, F> f,
			@NotNull final CascadeFunction<F, G> g,
			@NotNull final CascadeFunction<G, H> h,
			@NotNull final CascadeFunction<H, I> i,
			@NotNull final CascadeFunction<I, J> j
	) {
		return new CascadeArgument10<>(a, b, c, d, e, f, g, h, i, j);
	}
}

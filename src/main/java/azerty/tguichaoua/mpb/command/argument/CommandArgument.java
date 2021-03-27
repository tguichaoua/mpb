package azerty.tguichaoua.mpb.command.argument;

import azerty.tguichaoua.mpb.command.CommandException;
import azerty.tguichaoua.mpb.command.CommandExecution;
import azerty.tguichaoua.mpb.command.argument.cascade.*;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Biome;
import org.bukkit.block.BlockFace;
import org.bukkit.block.banner.PatternType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public interface CommandArgument<T> {
	/**
	 * Parses the next value.
	 * <p>
	 * Should *NOT* be called directly, use {@link CommandExecution#get(CommandArgument)} instead.
	 *
	 * @param execution the {@link CommandExecution}
	 * @return the parsed value
	 * @throws CommandException if the parsing fail
	 */
	T parse(@NotNull CommandExecution execution) throws CommandException;

	/**
	 * Returns the completion list for this argument.
	 * <p>
	 * Should *NOT* be called directly, use {@link CommandExecution#complete(CommandArgument)} instead.
	 *
	 * @param execution the {@link CommandExecution}
	 * @return the completion list
	 * @throws CommandException if the parsing fail
	 */
	default @NotNull Collection<String> complete(@NotNull final CommandExecution execution) throws CommandException {
		execution.next();
		return Collections.emptyList();
	}

	default <R> CommandArgument<R> then(@NotNull final Function<T, R> function) {
		return new TransformCommandArgument<>(this, (execution, argument) -> function.apply(execution.get(argument)));
	}

	default CommandArgument<T> check(@NotNull final Predicate<T> predicate) {
		return new TransformCommandArgument<>(this, (execution, argument) -> {
			final T value = execution.get(argument);
			if (predicate.test(value)) return value;
			else throw execution.invalidArgument();
		});
	}

	default CommandArgument<T> check(@NotNull final Predicate<T> predicate, final String reasonKey, final String... formatArgs) {
		return new TransformCommandArgument<>(this, (execution, argument) -> {
			final T value = execution.get(argument);
			if (predicate.test(value)) return value;
			else throw execution.invalidArgument(reasonKey, formatArgs);
		});
	}

	default CommandArgument<T> defaultValue(final T defaultValue) {
		return new TransformCommandArgument<>(
				this,
				(execution, argument) -> execution.remains() == 0 ? defaultValue : execution.get(argument)
		);
	}

	// --- To Cascade --------------------------------------------
	default <B, C, D, E, F, G, H, I, J> CascadeArgument10<T, B, C, D, E, F, G, H, I, J> toCascade(
			@NotNull final CascadeFunction<T, B> b,
			@NotNull final CascadeFunction<B, C> c,
			@NotNull final CascadeFunction<C, D> d,
			@NotNull final CascadeFunction<D, E> e,
			@NotNull final CascadeFunction<E, F> f,
			@NotNull final CascadeFunction<F, G> g,
			@NotNull final CascadeFunction<G, H> h,
			@NotNull final CascadeFunction<H, I> i,
			@NotNull final CascadeFunction<I, J> j
	) {
		return CascadeArgument.of(this, b, c, d, e, f, g, h, i, j);
	}

	default <B, C, D, E, F, G, H, I> CascadeArgument9<T, B, C, D, E, F, G, H, I> toCascade(
			@NotNull final CascadeFunction<T, B> b,
			@NotNull final CascadeFunction<B, C> c,
			@NotNull final CascadeFunction<C, D> d,
			@NotNull final CascadeFunction<D, E> e,
			@NotNull final CascadeFunction<E, F> f,
			@NotNull final CascadeFunction<F, G> g,
			@NotNull final CascadeFunction<G, H> h,
			@NotNull final CascadeFunction<H, I> i
	) {
		return CascadeArgument.of(this, b, c, d, e, f, g, h, i);
	}

	default <B, C, D, E, F, G, H> CascadeArgument8<T, B, C, D, E, F, G, H> toCascade(
			@NotNull final CascadeFunction<T, B> b,
			@NotNull final CascadeFunction<B, C> c,
			@NotNull final CascadeFunction<C, D> d,
			@NotNull final CascadeFunction<D, E> e,
			@NotNull final CascadeFunction<E, F> f,
			@NotNull final CascadeFunction<F, G> g,
			@NotNull final CascadeFunction<G, H> h
	) {
		return CascadeArgument.of(this, b, c, d, e, f, g, h);
	}

	default <B, C, D, E, F, G> CascadeArgument7<T, B, C, D, E, F, G> toCascade(
			@NotNull final CascadeFunction<T, B> b,
			@NotNull final CascadeFunction<B, C> c,
			@NotNull final CascadeFunction<C, D> d,
			@NotNull final CascadeFunction<D, E> e,
			@NotNull final CascadeFunction<E, F> f,
			@NotNull final CascadeFunction<F, G> g
	) {
		return CascadeArgument.of(this, b, c, d, e, f, g);
	}

	default <B, C, D, E, F> CascadeArgument6<T, B, C, D, E, F> toCascade(
			@NotNull final CascadeFunction<T, B> b,
			@NotNull final CascadeFunction<B, C> c,
			@NotNull final CascadeFunction<C, D> d,
			@NotNull final CascadeFunction<D, E> e,
			@NotNull final CascadeFunction<E, F> f
	) {
		return CascadeArgument.of(this, b, c, d, e, f);
	}

	default <B, C, D, E> CascadeArgument5<T, B, C, D, E> toCascade(
			@NotNull final CascadeFunction<T, B> b,
			@NotNull final CascadeFunction<B, C> c,
			@NotNull final CascadeFunction<C, D> d,
			@NotNull final CascadeFunction<D, E> e
	) {
		return CascadeArgument.of(this, b, c, d, e);
	}

	default <B, C, D> CascadeArgument4<T, B, C, D> toCascade(
			@NotNull final CascadeFunction<T, B> b,
			@NotNull final CascadeFunction<B, C> c,
			@NotNull final CascadeFunction<C, D> d
	) {
		return CascadeArgument.of(this, b, c, d);
	}

	default <B, C> CascadeArgument3<T, B, C> toCascade(
			@NotNull final CascadeFunction<T, B> b,
			@NotNull final CascadeFunction<B, C> c
	) {
		return CascadeArgument.of(this, b, c);
	}

	default <B> CascadeArgument2<T, B> toCascade(
			@NotNull final CascadeFunction<T, B> b
	) {
		return CascadeArgument.of(this, b);
	}

	// --- Collections -------------------------------------------
	static ListedCommandArgument<String> of(@NotNull final Collection<String> values) {
		return new ListedCommandArgument<>(values::stream, s -> s);
	}

	static ListedCommandArgument<String> of(final String... values) {
		return of(Arrays.asList(values));
	}

	static <T> ListedCommandArgument<T> of(@NotNull final Map<String, T> map) {
		return new ListedCommandArgument<>(() -> map.keySet().stream(), map::get);
	}

	// --- Enum ----------------------------------------------------------
	static <E extends Enum<E>> ListedCommandArgument<E> of(
			@NotNull final Class<E> clazz,
			@NotNull final Predicate<E> predicate,
			final boolean ignoreCase
	) {
		final List<String> values = Arrays.stream(clazz.getEnumConstants())
				.filter(predicate)
				.map(ignoreCase ? e -> e.name().toLowerCase() : Enum::name)
				.collect(Collectors.toList());

		final Function<String, E> parser =
				ignoreCase ?
						s -> Arrays.stream(clazz.getEnumConstants())
								.filter(e -> e.name().equalsIgnoreCase(s)).findAny().orElse(null) :
						s -> Enum.valueOf(clazz, s);

		return new ListedCommandArgument<>(
				values::stream,
				parser
		);
	}

	static <E extends Enum<E>> ListedCommandArgument<E> of(
			@NotNull final Class<E> clazz,
			@NotNull final Predicate<E> predicate
	) {
		return of(clazz, predicate, false);
	}

	static <E extends Enum<E>> ListedCommandArgument<E> of(
			@NotNull final Class<E> clazz,
			final boolean ignoreCase
	) {
		return of(clazz, e -> true, ignoreCase);
	}

	static <E extends Enum<E>> ListedCommandArgument<E> of(@NotNull final Class<E> clazz) {
		return of(clazz, e -> true, false);
	}

	// --- Repeating arguments ------------------------------------------------
	static <T> CommandArgument<@NotNull List<T>> repeat(
			@NotNull final CommandArgument<T> argument,
			final int amount
	) {
		return new CommandArgument<@NotNull List<T>>() {
			@Override
			public @NotNull List<T> parse(@NotNull final CommandExecution execution) throws CommandException {
				final List<T> values = new ArrayList<>(amount);
				for (int i = amount; i > 0; i--) {
					values.add(execution.get(argument));
				}
				return values;
			}

			@Override
			public @NotNull Collection<String> complete(@NotNull final CommandExecution execution) throws CommandException {
				Collection<String> complete = Collections.emptyList();
				for (int i = amount; i > 0 && execution.remains() != 0; i--) {
					complete = execution.complete(argument);
				}
				return complete;
			}
		};
	}

	static <T> CommandArgument<@NotNull List<T>> rest(@NotNull final CommandArgument<T> argument) {
		return new CommandArgument<@NotNull List<T>>() {
			@Override
			public @NotNull List<T> parse(@NotNull final CommandExecution execution) throws CommandException {
				final List<T> values = new ArrayList<>();
				while (execution.remains() != 0) {
					values.add(execution.get(argument));
				}
				return values;
			}

			@Override
			public @NotNull Collection<String> complete(@NotNull final CommandExecution execution) throws CommandException {
				Collection<String> complete = Collections.emptyList();
				while (execution.remains() != 0) {
					complete = execution.complete(argument);
				}
				return complete;
			}
		};
	}

	// -- Single value
	CommandArgument<String> STRING = CommandExecution::nextArgument;
	IntegerCommandArgument INTEGER = IntegerCommandArgument.SINGLETON;
	DoubleCommandArgument DOUBLE = DoubleCommandArgument.SINGLETON;

	// -- Bukkit Enums
	ListedCommandArgument<Art> ART = of(Art.class, true);
	ListedCommandArgument<Attribute> ATTRIBUTE = of(Attribute.class, true);
	ListedCommandArgument<Biome> BIOME = of(Biome.class, true);
	ListedCommandArgument<BlockFace> BLOCK_FACE = of(BlockFace.class, true);
	ListedCommandArgument<Difficulty> DIFFICULTY = of(Difficulty.class, true);
	ListedCommandArgument<DyeColor> DYE_COLOR = of(DyeColor.class, true);
	ListedCommandArgument<Effect> EFFECT = of(Effect.class, true);
	ListedCommandArgument<EntityType> ENTITY_TYPE = of(EntityType.class, true);
	ListedCommandArgument<EquipmentSlot> EQUIPMENT_SLOT = of(EquipmentSlot.class, true);
	ListedCommandArgument<GameMode> GAME_MODE = of(GameMode.class, true);
	ListedCommandArgument<Material> MATERIAL = of(Material.class, true);
	ListedCommandArgument<Particle> PARTICLE = of(Particle.class, true);
	ListedCommandArgument<PatternType> PATTERN_TYPE = of(PatternType.class, true);
	ListedCommandArgument<PotionType> POTION_TYPE = of(PotionType.class, true);
	ListedCommandArgument<Sound> SOUND = of(Sound.class, true);
	ListedCommandArgument<SoundCategory> SOUND_CATEGORY = of(SoundCategory.class, true);
	ListedCommandArgument<Statistic> STATISTIC = of(Statistic.class, true);

	// -- Misc
	ListedCommandArgument<@Nullable Player> ONLINE_PLAYER =
			new ListedCommandArgument<>(
					() -> Bukkit.getServer().getOnlinePlayers().stream().map(HumanEntity::getName),
					s -> Bukkit.getServer().getPlayerExact(s)
			);

	LocationCommandArgument LOCATION = LocationCommandArgument.SINGLETON;

	TargetSelectorCommandArgument TARGET_SELECTOR = TargetSelectorCommandArgument.SINGLETON;

	CommandArgument<List<Entity>> ENTITIES =
			new TransformCommandArgument<>(
					TARGET_SELECTOR,
					(execution, argument) -> execution.get(argument).get(execution.getSender())
			);
}

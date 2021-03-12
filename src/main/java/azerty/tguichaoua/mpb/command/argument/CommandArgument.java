package azerty.tguichaoua.mpb.command.argument;

import azerty.tguichaoua.mpb.command.CommandException;
import azerty.tguichaoua.mpb.command.CommandExecution;
import org.apache.commons.lang.StringUtils;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Biome;
import org.bukkit.block.BlockFace;
import org.bukkit.block.banner.PatternType;
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
	T parse(@NotNull CommandExecution execution) throws CommandException;

	default @NotNull Collection<String> complete(@NotNull final CommandExecution execution) throws CommandException {
		execution.next();
		return Collections.emptyList();
	}

	default int multiplicity() {
		return 1;
	}

	default <R> CommandArgument<R> then(@NotNull final Function<T, R> function) {
		return new TransformCommandArgument<>(this, (execution, value) -> function.apply(value));
	}

	default CommandArgument<T> check(@NotNull final Predicate<T> predicate) {
		return new TransformCommandArgument<>(this, (execution, value) -> {
			if (predicate.test(value)) return value;
			else throw execution.invalidArgument();
		});
	}

	default CommandArgument<T> defaultValue(final T defaultValue) {
		return new ProxyCommandArgument<T, T>(this) {
			@Override public T parse(@NotNull final CommandExecution execution) throws CommandException {
				if (execution.remains() == 0)
					return defaultValue;
				else
					return source.parse(execution);
			}
		};
	}

	static ListedCommandArgument<String> of(@NotNull final Collection<String> values) {
		return new ListedCommandArgument<>(values::stream, s -> s);
	}

	static ListedCommandArgument<String> of(final String... values) {
		return of(Arrays.asList(values));
	}

	static <T> ListedCommandArgument<T> of(@NotNull final Map<String, T> map) {
		return new ListedCommandArgument<>(() -> map.keySet().stream(), map::get);
	}

	static <E extends Enum<E>> ListedCommandArgument<E> of(@NotNull final Class<E> clazz, @NotNull final Predicate<E> predicate) {
		final List<String> values = Arrays.stream(clazz.getEnumConstants())
				.filter(predicate)
				.map(Enum::name)
				.collect(Collectors.toList());
		return new ListedCommandArgument<>(
				values::stream,
				s -> Enum.valueOf(clazz, s)
		);
	}

	static <E extends Enum<E>> ListedCommandArgument<E> of(@NotNull final Class<E> clazz) {
		return of(clazz, e -> true);
	}

	static <T> CommandArgument<@NotNull List<T>> repeat(
			@NotNull final CommandArgument<T> argument,
			final int amount
	) {
		return new CommandArgument<@NotNull List<T>>() {
			@Override
			public @NotNull List<T> parse(@NotNull final CommandExecution execution) throws CommandException {
				final List<T> values = new ArrayList<>(amount);
				for (int i = 0; i < amount; i++) {
					values.add(execution.get(argument));
				}
				return values;
			}

			@Override
			public @NotNull Collection<String> complete(@NotNull final CommandExecution execution) throws CommandException {
				return argument.complete(execution);
			}

			@Override public int multiplicity() {
				return amount;
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
				return argument.complete(execution);
			}

			@Override public int multiplicity() {
				return Integer.MAX_VALUE;
			}
		};
	}

	// -- Single value
	CommandArgument<String> STRING = CommandExecution::nextString;
	CommandArgument<Integer> INTEGER = CommandExecution::nextInteger;
	CommandArgument<Double> DOUBLE = CommandExecution::nextDouble;

	// -- Bukkit Enums
	ListedCommandArgument<Art> ART = of(Art.class);
	ListedCommandArgument<Attribute> ATTRIBUTE = of(Attribute.class);
	ListedCommandArgument<Biome> BIOME = of(Biome.class);
	ListedCommandArgument<BlockFace> BLOCK_FACE = of(BlockFace.class);
	ListedCommandArgument<Difficulty> DIFFICULTY = of(Difficulty.class);
	ListedCommandArgument<DyeColor> DYE_COLOR = of(DyeColor.class);
	ListedCommandArgument<Effect> EFFECT = of(Effect.class);
	ListedCommandArgument<EntityType> ENTITY_TYPE = of(EntityType.class);
	ListedCommandArgument<EquipmentSlot> EQUIPMENT_SLOT = of(EquipmentSlot.class);
	ListedCommandArgument<GameMode> GAME_MODE = of(GameMode.class);
	ListedCommandArgument<Material> MATERIAL = of(Material.class);
	ListedCommandArgument<Particle> PARTICLE = of(Particle.class);
	ListedCommandArgument<PatternType> PATTERN_TYPE = of(PatternType.class);
	ListedCommandArgument<PotionType> POTION_TYPE = of(PotionType.class);
	ListedCommandArgument<Sound> SOUND = of(Sound.class);
	ListedCommandArgument<SoundCategory> SOUND_CATEGORY = of(SoundCategory.class);
	ListedCommandArgument<Statistic> STATISTIC = of(Statistic.class);

	// -- Misc
	ListedCommandArgument<@Nullable Player> ONLINE_PLAYER =
			new ListedCommandArgument<>(
					() -> Bukkit.getServer().getOnlinePlayers().stream().map(HumanEntity::getName),
					s -> Bukkit.getServer().getPlayerExact(s)
			);

	CommandArgument<Location> LOCATION =
			new CommandArgument<Location>() {
				@Override
				public Location parse(final @NotNull CommandExecution execution) throws CommandException {
					final Location location = execution.getSenderLocation();
					return new Location(
							location.getWorld(),
							getCoord(execution, location.getX()),
							getCoord(execution, location.getY()),
							getCoord(execution, location.getZ())
					);
				}

				private double getCoord(final CommandExecution execution, final double relative) throws CommandException {
					double coord = 0;
					String arg = execution.nextString();
					if (arg.startsWith("~")) {
						if (arg.length() == 1) {
							return relative;
						}
						arg = arg.substring(1);
						coord += relative;
					}

					try {
						return coord + Double.parseDouble(arg);
					} catch (final NumberFormatException exception) {
						throw execution.invalidArgument();
					}
				}

				@Override
				public @NotNull Collection<String> complete(@NotNull final CommandExecution execution) throws CommandException {
					switch (execution.remains()) {
						case 1:
							return Collections.singleton(complete(execution.nextString()) + " ~ ~");
						case 2:
							execution.next();
							return Collections.singleton(complete(execution.nextString()) + " ~");
						default:
							execution.next(2);
							return Collections.singleton(complete(execution.nextString()));
					}
				}

				private String complete(final String s) {
					return StringUtils.isEmpty(s) ? "~" : s;
				}
			};
}

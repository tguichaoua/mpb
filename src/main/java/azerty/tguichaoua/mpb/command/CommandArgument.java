package azerty.tguichaoua.mpb.command;

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

	default <U> CommandArgument<U> then(@NotNull final Function<T, U> function) {
		return new CommandArgument<U>() {
			@Override
			public U parse(final @NotNull CommandExecution execution) throws CommandException {
				return function.apply(CommandArgument.this.parse(execution));
			}

			@Override
			public @NotNull Collection<String> complete(@NotNull final CommandExecution execution) throws CommandException {
				return CommandArgument.this.complete(execution);
			}

			@Override public int multiplicity() {
				return CommandArgument.this.multiplicity();
			}
		};
	}

	static CommandArgument<String> of(@NotNull final Collection<String> values) {
		return new CommandArgument<String>() {
			@Override
			public String parse(final @NotNull CommandExecution execution) throws CommandException {
				final String arg = execution.nextString();
				if (values.contains(arg))
					return arg;
				else
					throw execution.invalidArgument();
			}

			@Override
			public @NotNull Collection<String> complete(@NotNull final CommandExecution execution) throws CommandException {
				execution.next();
				return values;
			}
		};
	}

	static CommandArgument<String> of(final String... values) {
		return of(Arrays.asList(values));
	}

	static <T> CommandArgument<T> of(@NotNull final Map<String, T> map) {
		return new CommandArgument<T>() {
			@Override
			public T parse(final @NotNull CommandExecution execution) throws CommandException {
				final String key = execution.nextString();
				if (map.containsKey(key)) {
					return map.get(key);
				} else {
					throw execution.invalidArgument();
				}
			}

			@Override
			public @NotNull Collection<String> complete(@NotNull final CommandExecution execution) throws CommandException {
				execution.next();
				return map.keySet();
			}
		};
	}

	static <E extends Enum<E>> CommandArgument<E> of(@NotNull final Class<E> clazz, @NotNull final Predicate<E> predicate) {
		final Collection<String> values = Arrays.stream(clazz.getEnumConstants())
				.filter(predicate)
				.map(Enum::name)
				.collect(Collectors.toList());

		return new CommandArgument<E>() {
			@Override
			public E parse(final @NotNull CommandExecution execution) throws CommandException {
				try {
					final E e = Enum.valueOf(clazz, execution.nextString());
					if (!predicate.test(e)) throw execution.invalidArgument();
					return e;
				} catch (final IllegalArgumentException e) {
					throw execution.invalidArgument();
				}
			}

			@Override
			public @NotNull Collection<String> complete(@NotNull final CommandExecution execution) throws CommandException {
				execution.next();
				return values;
			}
		};
	}

	static <E extends Enum<E>> CommandArgument<E> of(@NotNull final Class<E> clazz) {
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
	CommandArgument<Art> ART = of(Art.class);
	CommandArgument<Attribute> ATTRIBUTE = of(Attribute.class);
	CommandArgument<Biome> BIOME = of(Biome.class);
	CommandArgument<BlockFace> BLOCK_FACE = of(BlockFace.class);
	CommandArgument<Difficulty> DIFFICULTY = of(Difficulty.class);
	CommandArgument<DyeColor> DYE_COLOR = of(DyeColor.class);
	CommandArgument<Effect> EFFECT = of(Effect.class);
	CommandArgument<EntityType> ENTITY_TYPE = of(EntityType.class);
	CommandArgument<EquipmentSlot> EQUIPMENT_SLOT = of(EquipmentSlot.class);
	CommandArgument<GameMode> GAME_MODE = of(GameMode.class);
	CommandArgument<Material> MATERIAL = of(Material.class);
	CommandArgument<Particle> PARTICLE = of(Particle.class);
	CommandArgument<PatternType> PATTERN_TYPE = of(PatternType.class);
	CommandArgument<PotionType> POTION_TYPE = of(PotionType.class);
	CommandArgument<Sound> SOUND = of(Sound.class);
	CommandArgument<SoundCategory> SOUND_CATEGORY = of(SoundCategory.class);
	CommandArgument<Statistic> STATISTIC = of(Statistic.class);

	// -- Misc
	CommandArgument<@Nullable Player> ONLINE_PLAYER =
			new CommandArgument<@Nullable Player>() {
				@Override public @Nullable Player parse(final @NotNull CommandExecution execution) throws CommandException {
					return Bukkit.getServer().getPlayerExact(execution.nextString());
				}

				@Override public @NotNull Collection<String> complete(@NotNull final CommandExecution execution) throws CommandException {
					execution.next();
					return Bukkit.getServer().getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList());
				}
			};

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

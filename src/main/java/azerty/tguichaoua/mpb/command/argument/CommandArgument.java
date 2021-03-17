package azerty.tguichaoua.mpb.command.argument;

import azerty.tguichaoua.mpb.command.CommandException;
import azerty.tguichaoua.mpb.command.CommandExecution;
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
	T parse(@NotNull CommandExecution execution) throws CommandException;

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
				for (int i = amount; i > 0; i--) {
					values.add(execution.get(argument));
				}
				return values;
			}

			@Override
			public @NotNull Collection<String> complete(@NotNull final CommandExecution execution) throws CommandException {
				Collection<String> complete = Collections.emptyList();
				for (int i = amount; i > 0 && execution.remains() != 0; i--) {
					complete = argument.complete(execution);
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
					complete = argument.complete(execution);
				}
				return complete;
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

	LocationCommandArgument LOCATION = LocationCommandArgument.SINGLETON;

	TargetSelectorCommandArgument TARGET_SELECTOR = TargetSelectorCommandArgument.SINGLETON;

	CommandArgument<List<Entity>> ENTITIES =
			new TransformCommandArgument<>(
					TARGET_SELECTOR,
					(execution, argument) -> execution.get(argument).get(execution.getSender())
			);
}

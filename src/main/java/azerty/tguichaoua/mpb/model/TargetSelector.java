package azerty.tguichaoua.mpb.model;

import azerty.tguichaoua.mpb.util.CommandUtils;
import lombok.*;
import org.apache.commons.lang.StringUtils;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Builder(access = AccessLevel.PRIVATE, builderClassName = "Builder")
public class TargetSelector {

	@lombok.Builder.Default private final @NotNull Selector selector = Selector.PLAYER;

	private final @Nullable Double x;
	private final @Nullable Double y;
	private final @Nullable Double z;

	private final @Nullable Double dx;
	private final @Nullable Double dy;
	private final @Nullable Double dz;

	private final @Nullable Integer limit;
	private final @Nullable Range distance;

	private final @Nullable XRotationPredicate xRotation;
	private final @Nullable YRotationPredicate yRotation;

	private final @Nullable TypePredicate type;
	private final @Nullable LevelPredicate level;
	private final @Nullable GameModePredicate gameMode;
	private final @Nullable NamePredicate name;
	@Singular private final @Nullable Collection<TagPredicate> tags;

	private final @Nullable Map<String, Range> scores; // TODO
	private final @Nullable Object team; // TODO
	private final @Nullable Object advancements; // TODO
	private final @Nullable Object nbt; // TODO

	private @Nullable Vector dxyz() {
		if (dx == null && dy == null && dz == null) return null;
		return new Vector(
				dx == null ? 0 : dx,
				dy == null ? 0 : dy,
				dz == null ? 0 : dz
		);
	}

	public @NotNull List<Entity> get(final CommandSender sender) {
		final Location senderLocation = CommandUtils.getCommandSenderLocation(sender);
		final Location origin = new Location(
				senderLocation.getWorld(),
				x == null ? senderLocation.getX() : x,
				y == null ? senderLocation.getX() : y,
				z == null ? senderLocation.getX() : z
		);

		if (selector.equals(Selector.SELF)) {
			final Entity target = (Entity) sender;
			if (
					(dxyz() == null || BoundingBox.of(origin.toVector(), dxyz()).contains(senderLocation.toVector()))
							&& (distance == null || distance.contains(origin.distance(senderLocation)))
							&& (level == null || level.test(target))
							&& (gameMode == null || gameMode.test(target))
							&& (name == null || name.test(target))
							&& (xRotation == null || xRotation.test(target))
							&& (yRotation == null || yRotation.test(target))
							&& (type == null || type.test(target))
							&& (tags == null || tags.stream().allMatch(t -> t.test(target)))
				// && (scores == null || true) // TODO
				// && (team == null || true) // TODO
				// && (advancements == null || true) // TODO
				// && (nbt == null || true) // TODO
			) {
				return Collections.singletonList((Entity) sender);
			} else {
				return Collections.emptyList();
			}
		}

		Stream<Entity> entities = origin.getWorld().getEntities().stream();

		if (!selector.equals(Selector.ENTITIES)) {
			entities = entities.filter(e -> e instanceof Player);
		}

		if (dxyz() != null) {
			final BoundingBox bb = BoundingBox.of(origin.toVector(), dxyz());
			entities = entities.filter(e -> bb.contains(e.getBoundingBox()));
		}

		if (distance != null) {
			entities = entities.filter(e -> distance.contains(origin.distance(e.getLocation())));
		}

		// TODO : scores

		// TODO : team

		if (level != null) {
			entities = entities.filter(level);
		}

		if (gameMode != null) {
			entities = entities.filter(gameMode);
		}

		if (name != null) {
			entities = entities.filter(name);
		}

		if (xRotation != null) {
			entities = entities.filter(xRotation);
		}

		if (yRotation != null) {
			entities = entities.filter(yRotation);
		}

		if (type != null) {
			entities = entities.filter(type);
		}

		if (tags != null) {
			for (val tag : tags) {
				entities = entities.filter(tag);
			}
		}

		// TODO : advencements

		// TODO : nbt

		if (selector.equals(Selector.RANDOM)) {
			final List<Entity> e = entities.collect(Collectors.toList());
			Collections.shuffle(e);
			return new ArrayList<>(e.subList(0, Math.min(limit == null ? 1 : limit, e.size())));
		} else {
			Comparator<Entity> comp = Comparator.comparingDouble(a -> origin.distance(a.getLocation()));
			if (limit != null && limit < 0) {
				comp = comp.reversed();
			}
			entities = entities.sorted(comp);

			final Integer limit;
			if (selector.equals(Selector.PLAYER)) {
				limit = this.limit == null ? 1 : this.limit;
			} else {
				limit = this.limit;
			}

			if (limit != null) {
				entities = entities.limit(Math.abs(limit));
			}
			return entities.collect(Collectors.toList());
		}
	}

	private static final Pattern PARSE_REGEX = Pattern.compile("^@(?<selector>[paers])(?:\\[(?<args>.*)])?$");

	public static TargetSelector parse(@NotNull final String s) throws TargetSelectorParseException {
		val matcher = PARSE_REGEX.matcher(s);
		if (!matcher.matches()) {
			throw new InvalidFormatTargetSelectorParseException();
		}

		val selectorString = matcher.group("selector");
		val argsString = matcher.group("args");
		val builder = TargetSelector.builder();

		builder.selector(Selector.parse(selectorString));

		if (argsString != null) {
			final String[] args = argsString.split(",");
			for (final String a : args) {
				final String[] kv = a.split("=");
				if (kv.length == 2) {
					try {
						final Property property = Property.valueOf(kv[0]);
						try {
							property.setter.accept(builder, kv[1]);
						} catch (final IllegalArgumentException e) {
							throw new InvalidArgumentValueTargetSelectorParseException(kv[1], e);
						}
					} catch (final IllegalArgumentException e) {
						throw new InvalidArgumentTargetSelectorParseException(kv[0], e);
					}
				} else {
					throw new IllegalArgumentException(String.format("Invalid argument \"%s\"", a));
				}
			}
		}

		return builder.build();
	}


	public enum Selector {
		PLAYER, // @p
		RANDOM, // @r
		ALL_PLAYERS, // @a
		ENTITIES, // @e
		SELF; // @s

		public static Selector parse(@NotNull final String s) {
			switch (s.startsWith("@") ? s.substring(1) : s) {
				case "p":
					return Selector.PLAYER;
				case "r":
					return Selector.RANDOM;
				case "a":
					return Selector.ALL_PLAYERS;
				case "e":
					return Selector.ENTITIES;
				case "s":
					return Selector.SELF;
				default:
					throw new InvalidSelectorTargetSelectorParseException(s);
			}
		}
	}

	@RequiredArgsConstructor
	public enum Property {
		x((b, s) -> b.x(Double.parseDouble(s))),
		y((b, s) -> b.y(Double.parseDouble(s))),
		z((b, s) -> b.z(Double.parseDouble(s))),
		dx((b, s) -> b.dx(Double.parseDouble(s))),
		dy((b, s) -> b.dy(Double.parseDouble(s))),
		dz((b, s) -> b.dz(Double.parseDouble(s))),
		distance((b, s) -> b.distance(Range.parse(s))),
		limit((b, s) -> b.limit(Integer.parseInt(s))),
		x_rotation((b, s) -> b.xRotation(XRotationPredicate.parse(s))),
		y_rotation((b, s) -> b.yRotation(YRotationPredicate.parse(s))),
		type((b, s) -> b.type(TypePredicate.parse(s))),
		level((b, s) -> b.level(LevelPredicate.parse(s))),
		gamemode((b, s) -> b.gameMode(GameModePredicate.parse(s))),
		name((b, s) -> b.name(NamePredicate.parse(s))),
		tag((b, s) -> b.tag(TagPredicate.parse(s))),
		scores((b, s) -> {
			throw new RuntimeException("Not Implemented");
		}), // TODO
		team((b, s) -> {
			throw new RuntimeException("Not Implemented");
		}), // TODO
		advancements((b, s) -> {
			throw new RuntimeException("Not Implemented");
		}), // TODO
		nbt((b, s) -> {
			throw new RuntimeException("Not Implemented");
		}) // TODO
		;

		private final @NotNull BiConsumer<TargetSelector.Builder, String> setter;
	}

	@RequiredArgsConstructor
	private static abstract class PropertyPredicate<T> implements Predicate<Entity> {
		@Getter private final T value;
		@Getter private final boolean reversed;

		@Override public final boolean test(final Entity entity) {
			return reversed != test(entity, value);
		}

		protected abstract boolean test(Entity entity, T value);

		protected static <P, T extends PropertyPredicate<P>> T parse(
				@NotNull String s,
				@NotNull final Function<String, P> parser,
				@NotNull final BiFunction<P, Boolean, T> ctor
		) {
			boolean reversed = false;
			if (s.startsWith("!")) {
				reversed = true;
				s = s.substring(1);
			}
			return ctor.apply(parser.apply(s), reversed);
		}
	}

	public static final class TagPredicate extends PropertyPredicate<String> {
		public TagPredicate(final String tag, final boolean reversed) {
			super(tag, reversed);
		}

		@Override protected boolean test(final Entity entity, final String tag) {
			return entity.getScoreboardTags().contains(tag);
		}

		public static TagPredicate parse(@NotNull final String s) {
			return parse(s, o -> o, TagPredicate::new);
		}
	}

	public static final class GameModePredicate extends PropertyPredicate<GameMode> {
		public GameModePredicate(final GameMode gameMode, final boolean reversed) {
			super(gameMode, reversed);
		}

		@Override protected boolean test(final Entity entity, final GameMode gameMode) {
			return entity instanceof Player && ((Player) entity).getGameMode().equals(gameMode);
		}

		public static GameModePredicate parse(@NotNull final String s) {
			return parse(s, GameMode::valueOf, GameModePredicate::new);
		}
	}

	public static final class NamePredicate extends PropertyPredicate<String> {
		public NamePredicate(final String name, final boolean reversed) {
			super(name, reversed);
		}

		@Override protected boolean test(final Entity entity, final String name) {
			return entity.getName().equals(name);
		}

		public static NamePredicate parse(@NotNull final String s) {
			return parse(s, o -> o, NamePredicate::new);
		}
	}

	public static final class TypePredicate extends PropertyPredicate<EntityType> {
		public TypePredicate(final EntityType type, final boolean reversed) {
			super(type, reversed);
		}

		@Override protected boolean test(final Entity entity, final EntityType type) {
			return entity.getType().equals(type);
		}

		public static TypePredicate parse(@NotNull final String s) {
			return parse(s, EntityType::valueOf, TypePredicate::new);
		}
	}

	public static final class LevelPredicate extends PropertyPredicate<Range> {
		public LevelPredicate(final Range range, final boolean reversed) {
			super(range, reversed);
		}

		@Override protected boolean test(final Entity entity, final Range range) {
			return entity instanceof Player && range.contains(((Player) entity).getLevel());
		}

		public static LevelPredicate parse(@NotNull final String s) {
			return parse(s, Range::parse, LevelPredicate::new);
		}
	}

	public static final class XRotationPredicate extends PropertyPredicate<Range> {
		public XRotationPredicate(final Range range, final boolean reversed) {
			super(range, reversed);
		}

		@Override protected boolean test(final Entity entity, final Range range) {
			return range.contains(entity.getLocation().getPitch());
		}

		public static XRotationPredicate parse(@NotNull final String s) {
			return parse(s, Range::parse, XRotationPredicate::new);
		}
	}

	public static final class YRotationPredicate extends PropertyPredicate<Range> {
		public YRotationPredicate(final Range range, final boolean reversed) {
			super(range, reversed);
		}

		@Override protected boolean test(final Entity entity, final Range range) {
			return range.contains(entity.getLocation().getYaw());
		}

		public static YRotationPredicate parse(@NotNull final String s) {
			return parse(s, Range::parse, YRotationPredicate::new);
		}
	}

	@RequiredArgsConstructor
	public static final class Range {
		public final double min;
		public final double max;

		public boolean contains(final double value) {
			return value >= min && value <= max;
		}

		public static Range of(final double value) {
			return new Range(value, value);
		}

		public static Range parse(@NotNull final String s) throws IllegalArgumentException {
			final int i = s.indexOf("..");
			if (i == -1) {
				return of(Double.parseDouble(s));
			} else {
				final String[] minMax = s.split("..");
				if (minMax.length == 2) {
					return new Range(
							StringUtils.isEmpty(minMax[0]) ? Double.NEGATIVE_INFINITY : Double.parseDouble(minMax[0]),
							StringUtils.isEmpty(minMax[1]) ? Double.POSITIVE_INFINITY : Double.parseDouble(minMax[1])
					);
				} else {
					throw new IllegalArgumentException();
				}
			}
		}
	}

	public static class TargetSelectorParseException extends IllegalArgumentException {
		private static final long serialVersionUID = 1393649650890334972L;

		public TargetSelectorParseException(final String message) {
			super(message);
		}

		public TargetSelectorParseException(final String message, final Throwable cause) {
			super(message, cause);
		}
	}

	public static class InvalidFormatTargetSelectorParseException extends TargetSelectorParseException {
		private static final long serialVersionUID = -8100864707102534769L;

		public InvalidFormatTargetSelectorParseException() {
			super("The value's format is not valid.");
		}
	}

	public static class InvalidSelectorTargetSelectorParseException extends TargetSelectorParseException {
		private static final long serialVersionUID = 672177824473989302L;

		@Getter private final String selector;

		public InvalidSelectorTargetSelectorParseException(final String selector) {
			super(String.format("Invalid selector \"%s\", expecting \"p\", \"r\", \"a\", \"e\" or \"s\".", selector));
			this.selector = selector;
		}
	}

	public static class InvalidArgumentTargetSelectorParseException extends TargetSelectorParseException {
		private static final long serialVersionUID = -5093117766931978581L;

		@Getter private final String argument;

		public InvalidArgumentTargetSelectorParseException(final String argument, final Throwable cause) {
			super(String.format("Invalid property \"%s\"", argument), cause);
			this.argument = argument;
		}
	}

	public static class InvalidArgumentValueTargetSelectorParseException extends TargetSelectorParseException {
		private static final long serialVersionUID = 14225124028860583L;

		@Getter private final String value;

		public InvalidArgumentValueTargetSelectorParseException(final String value, final Throwable cause) {
			super(String.format("Invalid property value \"%s\"", value), cause);
			this.value = value;
		}
	}
}


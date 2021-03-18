package azerty.tguichaoua.mpb.command;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class MultipleCommand extends MyCommand {

	private final @Nullable SingleCommand command;
	private final @NotNull Map<String, MyCommand> children;

	@Override
	protected void execute(final @NotNull CommandExecution execution) throws CommandException {
		if (execution.remains() > 0) {
			final MyCommand subCommand = children.get(execution.current());
			if (subCommand != null) {
				execution.pushLabel(execution.nextArgument()); // consume the sub command name
				subCommand.execute(execution);
				return;
			} else if (command == null) {
				throw new CommandException(CommandException.Type.UNKNOWN_COMMAND, execution);
			}
		}
		if (command != null) {
			command.execute(execution);
		}
	}

	@Override
	protected @NotNull Collection<String> complete(@NotNull final CommandExecution execution) {
		final List<String> complete = new ArrayList<>();

		if (execution.remains() < 2) {
			complete.addAll(children.keySet());
			if (command != null) {
				complete.addAll(command.complete(execution));
			}
		} else {
			try {
				final MyCommand subCommand = children.get(execution.current());
				if (subCommand != null) {
					execution.pushLabel(execution.nextArgument()); // consume the sub command name
					complete.addAll(subCommand.complete(execution));
				} else if (command != null) {
					complete.addAll(command.complete(execution));
				}
			} catch (final CommandException ignored) {
				// shouldn't happen
			}
		}

		return complete;
	}

	// --- BUILDER -----------------------------
	public static Builder builder(final SingleCommand command) {
		return new Builder(command);
	}

	public static Builder builder() {
		return new Builder(null);
	}

	@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
	public static final class Builder {
		private final @Nullable SingleCommand command;
		private final Map<String, MyCommand> children = new HashMap<>();

		public MultipleCommand build() {
			return new MultipleCommand(command, new HashMap<>(children));
		}

		public Builder add(@NotNull final String name, @NotNull final MyCommand command) {
			children.put(name, command);
			return this;
		}
	}
}

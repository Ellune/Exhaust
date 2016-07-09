package ninja.miserable.exhaust.command;

import com.sk89q.intake.parametric.Provider;

import static com.google.common.base.Preconditions.checkNotNull;

public class ParameterContainer<A, B, C, W extends Provider<A>, X extends Provider<B>, Y extends Provider<B>, Z extends C> {

    private final Class<A> commandSenderClass;
    private final Class<B> playerClass;
    private final Class<C> serverClass;
    private final W commandSenderProvider;
    private final X playerSenderProvider;
    private final Y playerProvider;
    private final Z serverInstance;

    public ParameterContainer(Class<A> commandSenderClass,
        Class<B> playerClass,
        Class<C> serverClass,
        W commandSenderProvider,
        X playerSenderProvider,
        Y playerProvider,
        Z serverInstance
    ) {
        this.commandSenderClass = checkNotNull(commandSenderClass);
        this.playerClass = checkNotNull(playerClass);
        this.serverClass = checkNotNull(serverClass);
        this.commandSenderProvider = checkNotNull(commandSenderProvider);
        this.playerSenderProvider = checkNotNull(playerSenderProvider);
        this.playerProvider = checkNotNull(playerProvider);
        this.serverInstance = checkNotNull(serverInstance);
    }

    public Class<A> getCommandSenderClass() {
        return commandSenderClass;
    }

    public Class<B> getPlayerClass() {
        return playerClass;
    }

    public Class<C> getServerClass() {
        return serverClass;
    }

    public W getCommandSenderProvider() {
        return commandSenderProvider;
    }

    public X getPlayerSenderProvider() {
        return playerSenderProvider;
    }

    public Y getPlayerProvider() {
        return playerProvider;
    }

    public Z getServerInstance() {
        return serverInstance;
    }
}

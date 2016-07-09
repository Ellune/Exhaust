package ninja.miserable.exhaust.command;

import com.sk89q.intake.parametric.AbstractModule;
import com.sk89q.intake.parametric.Provider;
import ninja.miserable.exhaust.command.annotation.Sender;

public class ExhaustModule extends AbstractModule {

    private final ParameterContainer<Object, Object, Object, Provider<Object>, Provider<Object>, Provider<Object>, Object> parameterContainer;

    public ExhaustModule(ParameterContainer<Object, Object, Object, Provider<Object>, Provider<Object>, Provider<Object>, Object> parameterContainer) {
        this.parameterContainer = parameterContainer;
    }

    @Override
    protected void configure() {
        this.bind(parameterContainer.getCommandSenderClass()).toProvider(parameterContainer.getCommandSenderProvider());
        this.bind(parameterContainer.getPlayerClass()).annotatedWith(Sender.class).toProvider(parameterContainer.getPlayerSenderProvider());
        this.bind(parameterContainer.getPlayerClass()).toProvider(parameterContainer.getPlayerProvider());
        this.bind(parameterContainer.getServerClass()).toInstance(parameterContainer.getServerInstance());
    }

}

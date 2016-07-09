package ninja.miserable.exhaust.command;

public interface CommandExecutor<T> {

    boolean onCommand(T sender, String name, String[] args);

}

package ninja.miserable.exhaust.command.annotation;

import com.sk89q.intake.parametric.annotation.Classifier;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Applied to a parameter of a command handler method to specify that the parameter should be fetched
 * from the sender of the command rather than from the arguments the sender used.
 */
@Classifier
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Sender {

}

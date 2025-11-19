package controller;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CommandDef {
    // The command keyword(s) (e.g., "go", "move")
    String[] value();

    // The help description
    String description();

    // Does this command take up a game turn?
    boolean consumesTurn() default true;
    
}

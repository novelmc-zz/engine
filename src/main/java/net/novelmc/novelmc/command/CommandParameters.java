package net.novelmc.novelmc.command;

import net.novelmc.novelmc.rank.Rank;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CommandParameters
{

    String description();

    String usage() default "/<command>";

    String aliases() default ""; // alias1, alias2, alias3 etc.

    SourceType source();

    Rank rank();
}

package net.pkhapps.dart.modules.resources.integration.rabbitmq.request;

import net.pkhapps.dart.modules.resources.integration.xsd.Request;

import javax.inject.Qualifier;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * TODO Document me!
 */
@Qualifier
@Retention(RUNTIME)
@Target({METHOD, FIELD, PARAMETER, TYPE})
public @interface HandlerFor {
    Class<? extends Request> value();
}

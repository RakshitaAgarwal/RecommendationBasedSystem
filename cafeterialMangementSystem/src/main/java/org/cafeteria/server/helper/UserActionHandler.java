package org.cafeteria.server.helper;

import org.cafeteria.common.model.UserAction;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface UserActionHandler {
    UserAction value();
}
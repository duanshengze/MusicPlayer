package com.victoryze.musicplayer.injector.scope;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by dsz on 17/6/6.
 */
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface PerApplication {
}

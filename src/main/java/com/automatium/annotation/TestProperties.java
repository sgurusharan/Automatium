package com.automatium.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Gurusharan on 20-11-2016.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface TestProperties {
    String product();
    String module();
    String name();
    String id();
    String description();
    String urlFormat();
    String url();
    String priority();
    String group();
}

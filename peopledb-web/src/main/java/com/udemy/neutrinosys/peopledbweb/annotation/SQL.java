package com.udemy.neutrinosys.peopledbweb.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

//we will create interface,this annotation to be visible using @Retention/policy or calls
@Retention(RetentionPolicy.RUNTIME)
public @interface SQL {
    //declaring method called value
    String value()
}

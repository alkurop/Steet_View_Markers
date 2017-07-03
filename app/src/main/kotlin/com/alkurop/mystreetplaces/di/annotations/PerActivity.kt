package com.alkurop.mystreetplaces.di.annotations

 import javax.inject.Scope


/**
 * A scoping annotation to permit objects with activity lifetime.
 */
@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class PerActivity

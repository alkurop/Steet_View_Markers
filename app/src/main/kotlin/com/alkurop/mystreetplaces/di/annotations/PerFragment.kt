package com.alkurop.mystreetplaces.di.annotations

 import javax.inject.Scope


/**
 * A scoping annotation to permit objects with fragment lifetime.
 */
@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class PerFragment

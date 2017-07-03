package com.alkurop.mystreetplaces.ui

import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject


fun <T> createViewSubject(): Subject<T> {
  return BehaviorSubject.create<T>().toSerialized()
}

fun <T> createNavigationSubject(): Subject<T> {
  return PublishSubject.create<T>().toSerialized()
}

package com.alkurop.mystreetplaces.db

import io.realm.Realm
import io.realm.RealmConfiguration


class RealmProviderImpl(val configuration: RealmConfiguration) : RealmProvider {

  override fun provideRealm(): Realm {
    return Realm.getInstance(configuration)
  }
}

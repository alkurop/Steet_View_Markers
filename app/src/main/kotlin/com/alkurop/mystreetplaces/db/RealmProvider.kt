package com.alkurop.mystreetplaces.db

import io.realm.Realm

interface RealmProvider {

  fun provideRealm(): Realm

}

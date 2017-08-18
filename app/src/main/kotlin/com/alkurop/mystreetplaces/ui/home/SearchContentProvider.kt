package com.alkurop.mystreetplaces.ui.home

import android.app.SearchManager
import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import com.alkurop.mystreetplaces.MyStreetPlacesApp
import com.alkurop.mystreetplaces.db.RealmProvider
import com.alkurop.mystreetplaces.di.modules.ProviderModule
import com.alkurop.mystreetplaces.domain.pin.PinDto
import javax.inject.Inject

class SearchContentProvider : ContentProvider() {

    @Inject lateinit var realmProvider: RealmProvider

    var isInited = false

    override fun insert(uri: Uri?, values: ContentValues?): Uri {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun query(uri: Uri, projection: Array<out String>?, selection: String?, selectionArgs: Array<out String>?, sortOrder: String?): Cursor {
        if (isInited.not()) {
            (context.applicationContext as MyStreetPlacesApp).component.providerComponent(ProviderModule(this)).inject(this)
            isInited = true
        }

        val cursor = MatrixCursor(arrayOf("_id",
                SearchManager.SUGGEST_COLUMN_TEXT_1,
                SearchManager.SUGGEST_COLUMN_TEXT_2,
                SearchManager. SUGGEST_COLUMN_INTENT_DATA))

        val realm = realmProvider.provideRealm()
        val findAll = realm.where(PinDto::class.java).contains("title", uri.lastPathSegment).findAll()
        val copyFromRealm = realm.copyFromRealm(findAll)
        copyFromRealm.forEach {
            val elements = it.id?.hashCode() ?: 0
            cursor.addRow(arrayOf(elements, it.title, it.description, it.id))
        }
        return cursor
    }

    override fun onCreate(): Boolean {
        return true
    }

    override fun update(uri: Uri?, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delete(uri: Uri?, selection: String?, selectionArgs: Array<out String>?): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getType(uri: Uri?): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
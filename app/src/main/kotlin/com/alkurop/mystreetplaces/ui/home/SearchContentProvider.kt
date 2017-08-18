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
    companion object {
        val AUTH = "SearchContentProvider"
    }

    @Inject lateinit var realmProvider: RealmProvider

    override fun insert(uri: Uri?, values: ContentValues?): Uri {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun query(uri: Uri, projection: Array<out String>?, selection: String?, selectionArgs: Array<out String>, sortOrder: String?): Cursor {
        val cursor = MatrixCursor(arrayOf(SearchManager.SUGGEST_COLUMN_TEXT_1
                , SearchManager.SUGGEST_COLUMN_TEXT_2, SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID))

        val realm = realmProvider.provideRealm()
        val findAll = realm.where(PinDto::class.java).contains("title", uri.lastPathSegment).findAll()
        val copyFromRealm = realm.copyFromRealm(findAll)
        copyFromRealm.forEach {
            cursor.addRow(arrayOf(it.title, it.description, it.id))
        }
        return cursor
    }

    override fun onCreate(): Boolean {
        (context.applicationContext as MyStreetPlacesApp).component.providerComponent(ProviderModule(this)).inject(this)
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
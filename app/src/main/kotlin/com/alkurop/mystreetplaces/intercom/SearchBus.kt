package com.alkurop.mystreetplaces.intercom

import com.alkurop.mystreetplaces.domain.pin.PinDto
import io.reactivex.subjects.PublishSubject

class SearchBus {
    val pinSearch = PublishSubject.create<SearchModel>()

    data class SearchModel(val pinDto: PinDto, val query: String)
}
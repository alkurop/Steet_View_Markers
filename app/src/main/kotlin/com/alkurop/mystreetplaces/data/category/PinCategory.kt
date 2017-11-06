package com.alkurop.mystreetplaces.data.category

import android.support.annotation.DrawableRes
import com.alkurop.mystreetplaces.R

enum class PinCategory(@DrawableRes val icon: Int) {
    PARKING(R.drawable.ic_parking),
    SHOPPING(R.drawable.ic_shopping),
    SPORTS(R.drawable.ic_sports),
    BAR(R.drawable.ic_bar),
    AIRPORT(R.drawable.ic_airport),
    ANIMALS(R.drawable.ic_animals),
    COFFEE(R.drawable.ic_coffee),
    CINEMA(R.drawable.ic_cinema),
    PUB(R.drawable.ic_pub),
    BEACH(R.drawable.ic_beach),
    DOCTOR(R.drawable.ic_doctor),
    CAR_REPAIR(R.drawable.ic_car_repair),
    GAS_STATION(R.drawable.ic_gas_station),
    FAST_FOOD(R.drawable.ic_fast_food),
    FITNESS(R.drawable.ic_fitness),
    GROCERY(R.drawable.ic_grocery),
    HOTEL(R.drawable.ic_hotel),
    KARAOKE(R.drawable.ic_karaoke),
    LIBRARY(R.drawable.ic_library),
    MUSIC(R.drawable.ic_music),
    PARK(R.drawable.ic_park),
    PETS(R.drawable.ic_pets),
    ;
}

fun String?.mapCategory(): PinCategory? {
    PinCategory.values().forEach {
        if (it.name == this) {
            return it
        }
    }
    return null
}
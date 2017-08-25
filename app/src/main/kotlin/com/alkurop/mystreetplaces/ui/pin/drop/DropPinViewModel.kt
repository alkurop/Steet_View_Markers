package com.alkurop.mystreetplaces.ui.pin.drop

import android.location.Address
import android.support.annotation.StringRes
import com.alkurop.mystreetplaces.domain.pin.PinDto

data class DropPinViewModel(val pinDto: PinDto? = null,
                            val addressList: List<Address>? = null,
                            @StringRes val errorRes: Int? = null,
                            val isLoading: Boolean? = null)
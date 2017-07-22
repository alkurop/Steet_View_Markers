package com.alkurop.mystreetplaces.ui.pin.drop

import android.support.annotation.StringRes
import com.alkurop.mystreetplaces.domain.pin.PinDto


data class DropPinViewModel(val pinDto: PinDto? = null,
                            @StringRes val errorRes: Int? = null)
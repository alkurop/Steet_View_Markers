package com.alkurop.mystreetplaces.ui.navigation

import android.os.Bundle

interface NavigationAction

fun Bundle?.equalBundles(two: Bundle?): Boolean {
  if (this == null && two == null) return true
  if (this == null || two == null) return false
  if (this == two) return true
  if (this.size() != two.size())
    return false

  val setOne = this.keySet()
  val setTwo = two.keySet()
  var valueOne: Any?
  var valueTwo: Any?

  if (setOne == null && setTwo == null) return true
  if(setOne == null || setTwo == null) return false
  for (key in setOne) {
    valueOne = this.get(key)
    valueTwo = two.get(key)
    if (valueOne is Bundle && valueTwo is Bundle &&
        !valueOne.equalBundles(valueTwo as Bundle?)) {
      return false
    } else if (valueOne == null) {
      if (valueTwo != null || !two.containsKey(key))
        return false
    } else if (valueOne != valueTwo)
      return false
  }

  return true
}

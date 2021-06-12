package navigation

import com.arkivanov.decompose.statekeeper.Parcelable

sealed class ScreenConfiguration : Parcelable {
    object Welcome : ScreenConfiguration()
    data class Chat(val userName: String) : ScreenConfiguration()
}

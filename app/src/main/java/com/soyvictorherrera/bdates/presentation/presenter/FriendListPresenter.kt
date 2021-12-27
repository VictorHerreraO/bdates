package com.soyvictorherrera.bdates.presentation.presenter

import com.soyvictorherrera.bdates.data.repository.FriendRepository
import com.soyvictorherrera.bdates.domain.model.FriendModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

class FriendListPresenter(
    private val friendRepository: FriendRepository
) : BasePresenter<FriendListPresenter.FriendListView>() {

    private var friendList: List<FriendModel> by Delegates.observable(emptyList()) { _, _, newList ->
        onFriendListUpdated(newList)
    }

    override fun onViewSet(view: FriendListView) {
        GlobalScope.launch {
            friendList = friendRepository.getFriendList().sortedBy {
                it.daysUntilBirthDate
            }
        }
    }

    private fun onFriendListUpdated(list: List<FriendModel>) {
        view?.updateFriendList(list)
    }

    interface FriendListView : View {
        fun updateFriendList(list: List<FriendModel>)
    }

}
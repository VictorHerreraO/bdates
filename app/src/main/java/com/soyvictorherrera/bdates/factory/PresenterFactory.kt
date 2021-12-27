package com.soyvictorherrera.bdates.factory

import com.soyvictorherrera.bdates.presentation.presenter.FriendListPresenter
import com.soyvictorherrera.bdates.presentation.presenter.TemplatesPresenter

object PresenterFactory {

    fun getFriendListPresenter(): FriendListPresenter {
        return FriendListPresenter(
            RepositoryFactory.getFriendRepository()
        )
    }

    fun getTemplatesPresenter(): TemplatesPresenter {
        return TemplatesPresenter(
            RepositoryFactory.getTemplatesRepository()
        )
    }

}

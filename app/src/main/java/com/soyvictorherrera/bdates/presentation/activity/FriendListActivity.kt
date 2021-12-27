package com.soyvictorherrera.bdates.presentation.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.soyvictorherrera.bdates.databinding.ActivityFriendListBinding
import com.soyvictorherrera.bdates.domain.model.FriendModel
import com.soyvictorherrera.bdates.factory.PresenterFactory
import com.soyvictorherrera.bdates.presentation.adapter.FriendListAdapter
import com.soyvictorherrera.bdates.presentation.presenter.FriendListPresenter

class FriendListActivity : AppCompatActivity(), FriendListPresenter.FriendListView {

    private val presenter: FriendListPresenter = PresenterFactory.getFriendListPresenter()

    private var _binding: ActivityFriendListBinding? = null
    private val binding: ActivityFriendListBinding get() = _binding!!

    private lateinit var adapter: FriendListAdapter

    //region AppCompatActivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupContent()
        setupFriendList()

        presenter.view = this
    }
    //endregion

    //region FriendListPresenter.FriendListView
    override fun updateFriendList(list: List<FriendModel>) {
        adapter.submitList(list)
    }
    //endregion

    private fun setupContent() {
        _binding = ActivityFriendListBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun setupFriendList(): Unit = with(binding) {
        listFriends.apply {
            adapter = FriendListAdapter()
                .apply {
                    onItemClickListener = this@FriendListActivity::onFriendClick
                }
                .also {
                    this@FriendListActivity.adapter = it
                }
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
    }

    private fun onFriendClick(friend: FriendModel) {
        startActivity(Intent(this, TemplatesActivity::class.java)
            .apply {
                putExtra(TemplatesActivity.EXTRA_FRIEND, friend)
            })
    }

}

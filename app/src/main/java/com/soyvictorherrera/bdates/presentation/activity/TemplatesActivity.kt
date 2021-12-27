package com.soyvictorherrera.bdates.presentation.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.soyvictorherrera.bdates.databinding.ActivityTemplatesBinding
import com.soyvictorherrera.bdates.domain.model.FriendModel
import com.soyvictorherrera.bdates.factory.PresenterFactory
import com.soyvictorherrera.bdates.presentation.adapter.TemplateListAdapter
import com.soyvictorherrera.bdates.presentation.presenter.TemplatesPresenter
import com.soyvictorherrera.bdates.utils.extensions.shareText

class TemplatesActivity : AppCompatActivity(), TemplatesPresenter.TemplatesView {

    companion object {
        const val EXTRA_FRIEND = "extra_friend"
    }

    private val presenter = PresenterFactory.getTemplatesPresenter()

    private lateinit var binding: ActivityTemplatesBinding
    private lateinit var adapter: TemplateListAdapter
    private var friend: FriendModel? = null

    //region AppCompatActivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        friend = intent?.getSerializableExtra(EXTRA_FRIEND) as FriendModel?

        setupContent()
        setupTemplatesList()

        presenter.view = this
    }
    //endregion

    //region TemplatesPresenter.TemplatesView
    override fun updateTemplateList(templates: List<String>) {
        adapter.submitList(templates)
    }
    //endregion

    private fun setupContent() {
        binding = ActivityTemplatesBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun setupTemplatesList(): Unit = with(binding) {
        listTemplates.apply {
            adapter = TemplateListAdapter()
                .apply {
                    onItemClickListener = this@TemplatesActivity::onTemplateClick
                }
                .also {
                    this@TemplatesActivity.adapter = it
                }
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL))
        }
    }

    private fun onTemplateClick(template: String) {
        shareText(template)
    }

}

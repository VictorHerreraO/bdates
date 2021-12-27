package com.soyvictorherrera.bdates.presentation.presenter

import com.soyvictorherrera.bdates.data.repository.TemplatesRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

class TemplatesPresenter(
    private val templatesRepository: TemplatesRepository
) : BasePresenter<TemplatesPresenter.TemplatesView>() {
    private var templates by Delegates.observable(emptyList<String>()) { _, _, newList ->
        onTemplateListUpdated(newList)
    }

    override fun onViewSet(view: TemplatesView) {
        GlobalScope.launch {
            templates = templatesRepository.getTemplateList()
        }
    }

    private fun onTemplateListUpdated(list: List<String>) {
        view?.updateTemplateList(list)
    }

    interface TemplatesView : View {
        fun updateTemplateList(templates: List<String>)
    }

}

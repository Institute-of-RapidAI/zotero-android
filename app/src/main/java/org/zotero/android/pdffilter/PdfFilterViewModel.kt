package org.zotero.android.pdffilter

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.zotero.android.architecture.BaseViewModel2
import org.zotero.android.architecture.ScreenArguments
import org.zotero.android.architecture.ViewEffect
import org.zotero.android.architecture.ViewState
import org.zotero.android.database.objects.RCustomLibraryType
import org.zotero.android.pdf.data.AnnotationsFilter
import org.zotero.android.screens.tagpicker.data.TagPickerArgs
import org.zotero.android.screens.tagpicker.data.TagPickerResult
import org.zotero.android.sync.LibraryIdentifier
import org.zotero.android.sync.Tag
import javax.inject.Inject

@HiltViewModel
internal class PdfFilterViewModel @Inject constructor(
) : BaseViewModel2<PdfFilterViewState, PdfFilterViewEffect>(PdfFilterViewState()) {

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(tagPickerResult: TagPickerResult) {
        viewModelScope.launch {
            setTags(tagPickerResult.tags)
        }
    }

    private fun setTags(tags: List<Tag>) {
        updateState {
            copy(tags = tags.map { it.name }.toSet())
        }
        updateFilter()

    }

    fun init() = initOnce {
        EventBus.getDefault().register(this)
        val args = ScreenArguments.pdfFilterArgs

        updateState {
            copy(
                colors = args.filter?.colors ?: emptySet(),
                tags = args.filter?.tags ?: emptySet(),
                availableColors = args.availableColors,
                availableTags = args.availableTags,
            )
        }
    }

    fun onClose() {
        triggerEffect(PdfFilterViewEffect.OnBack)
        updateFilter()
    }

    private fun updateFilter() {
        if (viewState.colors.isEmpty() && viewState.tags.isEmpty()) {
            EventBus.getDefault().post(PdfFilterResult(null))
        } else {
            val filter = AnnotationsFilter(colors = viewState.colors, tags = viewState.tags)
            EventBus.getDefault().post(PdfFilterResult(filter))
        }
    }

    fun onClear() {
        updateState {
            copy(tags = emptySet(), colors = emptySet())
        }
        updateFilter()
    }

    fun toggleColor(color: String) {
        if (!viewState.availableColors.contains(color)) { return }
        if (viewState.colors.contains(color)) {
            updateState {
                copy(colors = viewState.colors - color)
            }
        } else {
            updateState {
                copy(colors = viewState.colors + color)
            }
        }
        updateFilter()
    }

    fun onTagsClicked() {
        ScreenArguments.tagPickerArgs = TagPickerArgs(
            libraryId = LibraryIdentifier.custom(RCustomLibraryType.myLibrary),
            selectedTags = viewState.tags,
            tags = viewState.availableTags
        )

        triggerEffect(PdfFilterViewEffect.NavigateToTagPickerScreen)
    }

    override fun onCleared() {
        super.onCleared()
        EventBus.getDefault().unregister(this)
    }

}

internal data class PdfFilterViewState(
    val availableColors: List<String> = emptyList(),
    val availableTags: List<Tag> = emptyList(),
    var colors: Set<String> = emptySet(),
    var tags: Set<String> = emptySet(),
) : ViewState {
    fun isClearVisible(): Boolean {
        return !colors.isEmpty() || !tags.isEmpty()
    }

    fun formattedTags(): String {
        return tags.joinToString(separator = ", ")
    }
}

internal sealed class PdfFilterViewEffect : ViewEffect {
    object OnBack: PdfFilterViewEffect()
    object NavigateToTagPickerScreen: PdfFilterViewEffect()
}
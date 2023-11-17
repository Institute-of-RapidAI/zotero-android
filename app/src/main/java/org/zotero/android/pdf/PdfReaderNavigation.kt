
package org.zotero.android.pdf

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import com.google.accompanist.navigation.animation.composable
import org.zotero.android.architecture.navigation.ZoteroNavigation
import org.zotero.android.architecture.navigation.dialogFixedDimens
import org.zotero.android.pdf.annotation.PdfAnnotationNavigation
import org.zotero.android.pdf.annotation.pdfAnnotationNavScreens
import org.zotero.android.pdf.annotation.toPdfAnnotationScreen
import org.zotero.android.pdf.annotationmore.PdfAnnotationMoreNavigation
import org.zotero.android.pdf.annotationmore.pdfAnnotationMoreNavScreens
import org.zotero.android.pdf.annotationmore.toPdfAnnotationMoreScreen
import org.zotero.android.pdf.colorpicker.PdfReaderColorPickerScreen
import org.zotero.android.pdf.reader.PdfReaderScreen
import org.zotero.android.pdf.settings.PdfSettingsScreen
import org.zotero.android.pdffilter.PdfFilterNavigation
import org.zotero.android.pdffilter.pdfFilterNavScreens
import org.zotero.android.pdffilter.toPdfFilterScreen

internal fun NavGraphBuilder.pdfReaderScreenAndNavigationForTablet(
    navigation: ZoteroNavigation,
    navigateToTagPickerDialog: () -> Unit,
) {
    pdfScreen(
        onBack = navigation::onBack,
        navigateToPdfFilter = navigation::toPdfFilterNavigation,
        navigateToPdfSettings = navigation::toPdfSettings,
        navigateToPdfColorPicker = navigation::toPdfColorPicker,
        navigateToPdfAnnotation = navigation::toPdfAnnotationNavigation,
        navigateToPdfAnnotationMore = navigation::toPdfAnnotationMoreNavigation,
        navigateToTagPicker = navigateToTagPickerDialog,
    )
    dialogFixedDimens(
        modifier = Modifier
            .height(400.dp)
            .width(400.dp),
        route = PdfReaderDestinations.PDF_FILTER_NAVIGATION,
    ) {
        PdfFilterNavigation()
    }
    dialogFixedDimens(
        modifier = Modifier
            .height(500.dp)
            .width(420.dp),
        route = PdfReaderDestinations.PDF_SETTINGS,
    ) {
        PdfSettingsScreen(onBack = navigation::onBack)
    }
    dialogFixedDimens(
        modifier = Modifier
            .height(500.dp)
            .width(420.dp),
        route = PdfReaderDestinations.PDF_ANNOTATION_NAVIGATION,
    ) {
        PdfAnnotationNavigation()
    }
    dialogFixedDimens(
        modifier = Modifier
            .height(500.dp)
            .width(420.dp),
        route = PdfReaderDestinations.PDF_ANNOTATION_MORE_NAVIGATION,
    ) {
        PdfAnnotationMoreNavigation()
    }
    dialogFixedDimens(
        modifier = Modifier
            .height(220.dp)
            .width(300.dp),
        route = PdfReaderDestinations.PDF_COLOR_PICKER,
    ) {
        PdfReaderColorPickerScreen(onBack = navigation::onBack)
    }
}

internal fun NavGraphBuilder.pdfReaderNavScreensForPhone(
    navigation: ZoteroNavigation,
    navigateToTagPicker: () -> Unit,
) {
    pdfScreen(
        onBack = navigation::onBack,
        navigateToPdfFilter = navigation::toPdfFilterScreen,
        navigateToPdfSettings = navigation::toPdfSettings,
        navigateToPdfAnnotation = navigation::toPdfAnnotationScreen,
        navigateToPdfAnnotationMore = navigation::toPdfAnnotationMoreScreen,
        navigateToPdfColorPicker = navigation::toPdfColorPicker,
        navigateToTagPicker = navigateToTagPicker,
    )
    pdfFilterNavScreens(navigation)
    pdfSettings(navigation)
    pdfColorPicker(navigation)
    pdfAnnotationMoreNavScreens(navigation)
    pdfAnnotationNavScreens(navigation)
}

private fun NavGraphBuilder.pdfSettings(navigation: ZoteroNavigation) {
    composable(
        route = PdfReaderDestinations.PDF_SETTINGS,
        arguments = listOf(),
    ) {
        PdfSettingsScreen(onBack = navigation::onBack)
    }
}

private fun NavGraphBuilder.pdfColorPicker(navigation: ZoteroNavigation) {
    composable(
        route = PdfReaderDestinations.PDF_COLOR_PICKER,
        arguments = listOf(),
    ) {
        PdfReaderColorPickerScreen(onBack = navigation::onBack)
    }
}



private fun NavGraphBuilder.pdfScreen(
    onBack: () -> Unit,
    navigateToPdfFilter: () -> Unit,
    navigateToPdfSettings: () -> Unit,
    navigateToPdfColorPicker: () -> Unit,
    navigateToPdfAnnotation: () -> Unit,
    navigateToPdfAnnotationMore: () -> Unit,
    navigateToTagPicker: () -> Unit,
) {
    composable(
        route = PdfReaderDestinations.PDF_SCREEN,
    ) {
        PdfReaderScreen(
            onBack = onBack,
            navigateToPdfFilter = navigateToPdfFilter,
            navigateToPdfSettings = navigateToPdfSettings,
            navigateToPdfAnnotation = navigateToPdfAnnotation,
            navigateToPdfAnnotationMore = navigateToPdfAnnotationMore,
            navigateToPdfColorPicker = navigateToPdfColorPicker,
            navigateToTagPicker = navigateToTagPicker,
        )
    }
}

private object PdfReaderDestinations {
    const val PDF_FILTER_NAVIGATION = "pdfFilterNavigation"
    const val PDF_SCREEN = "pdfScreen"
    const val PDF_SETTINGS = "pdfSettings"
    const val PDF_COLOR_PICKER = "pdfColorPicker"
    const val PDF_ANNOTATION_NAVIGATION = "pdfAnnotationNavigation"
    const val PDF_ANNOTATION_MORE_NAVIGATION = "pdfAnnotationMoreNavigation"
}

fun ZoteroNavigation.toPdfScreen() {
    navController.navigate(PdfReaderDestinations.PDF_SCREEN)
}

private fun ZoteroNavigation.toPdfFilterNavigation() {
    navController.navigate(PdfReaderDestinations.PDF_FILTER_NAVIGATION)
}

private fun ZoteroNavigation.toPdfSettings() {
    navController.navigate(PdfReaderDestinations.PDF_SETTINGS)
}

private fun ZoteroNavigation.toPdfAnnotationNavigation() {
    navController.navigate(PdfReaderDestinations.PDF_ANNOTATION_NAVIGATION)
}

private fun ZoteroNavigation.toPdfAnnotationMoreNavigation() {
    navController.navigate(PdfReaderDestinations.PDF_ANNOTATION_MORE_NAVIGATION)
}

private fun ZoteroNavigation.toPdfColorPicker() {
    navController.navigate(PdfReaderDestinations.PDF_COLOR_PICKER)
}

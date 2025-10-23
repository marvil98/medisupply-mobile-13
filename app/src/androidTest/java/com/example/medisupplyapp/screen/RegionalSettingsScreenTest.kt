import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.*
import com.example.medisupplyapp.screen.RegionalSettingsScreen
import org.junit.Rule
import org.junit.Test

class RegionalSettingsScreenUiTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private var languageChanged = ""
    private var saveClicked = false

    private val onLanguageChange: (String) -> Unit = { languageChanged = it }
    private val onNavigate: (String) -> Unit = { }
    private val onBack: () -> Unit = { }
    private val onSave: () -> Unit = { saveClicked = true }

    @Test
    fun dropdown_selection_debugTest() {
        composeTestRule.setContent {
            RegionalSettingsScreen(
                currentLanguage = "Español",
                onLanguageChange = onLanguageChange,
                onNavigate = onNavigate,
                selectedRoute = "settings",
                onBack = onBack,
                onSave = onSave
            )
        }

        // Antes de hacer click
        composeTestRule.onRoot().printToLog("UI_TREE_INITIAL")

        // Abrir dropdown
        composeTestRule.onNodeWithText("Idioma").performClick()

        // Después de abrir dropdown
        composeTestRule.onRoot().printToLog("UI_TREE_AFTER_CLICK")
    }

}

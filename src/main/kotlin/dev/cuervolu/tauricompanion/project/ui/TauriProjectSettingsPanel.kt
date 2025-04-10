package dev.cuervolu.tauricompanion.project.ui

import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.ui.ValidationInfo
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBTextField
import com.intellij.ui.dsl.builder.*
import dev.cuervolu.tauricompanion.TauriBundle
import dev.cuervolu.tauricompanion.project.FrontendFramework
import dev.cuervolu.tauricompanion.project.FrontendLanguage
import dev.cuervolu.tauricompanion.project.PackageManager
import dev.cuervolu.tauricompanion.project.TauriProjectSettings
import java.awt.event.ItemEvent
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener

class TauriProjectSettingsPanel(private val settings: TauriProjectSettings) {
    private val nameTextField = JBTextField().apply {
        text = settings.projectName
        document.addDocumentListener(object : DocumentListener {
            override fun insertUpdate(e: DocumentEvent) = updateSettings()
            override fun removeUpdate(e: DocumentEvent) = updateSettings()
            override fun changedUpdate(e: DocumentEvent) = updateSettings()
        })
    }

    private val languageComboBox = ComboBox(FrontendLanguage.entries.toTypedArray()).apply {
        selectedItem = settings.language
        addItemListener { event ->
            if (event.stateChange == ItemEvent.SELECTED) {
                updatePackageManagerOptions()
                updateFrameworkOptions()
                updateTypeScriptVisibility()
                updateSettings()
            }
        }
    }

    private val packageManagerComboBox = ComboBox(PackageManager.getForLanguage(settings.language)).apply {
        selectedItem = settings.packageManager
        addItemListener { if (it.stateChange == ItemEvent.SELECTED) updateSettings() }
    }

    private val frameworkComboBox = ComboBox(FrontendFramework.getForLanguage(settings.language)).apply {
        selectedItem = settings.frontendFramework
        addItemListener { if (it.stateChange == ItemEvent.SELECTED) updateSettings() }
    }

    private val typeScriptCheckBox = JBCheckBox(
        TauriBundle.message("project.settings.typescript"),
        settings.useTypeScript
    ).apply {
        addItemListener { if (it.stateChange == ItemEvent.SELECTED) updateSettings() }
    }

    private val descriptionLabel = JBLabel(TauriBundle.message("project.settings.description"))

    private lateinit var mainPanel: JPanel
    private lateinit var typescriptRow: Row

    init {
        createUIComponents()
        updateTypeScriptVisibility() // Establecer visibilidad inicial
    }

    private fun updatePackageManagerOptions() {
        val language = languageComboBox.selectedItem as FrontendLanguage
        val packageManagers = PackageManager.getForLanguage(language)

        packageManagerComboBox.removeAllItems()
        packageManagers.forEach { packageManagerComboBox.addItem(it) }

        if (packageManagers.isNotEmpty()) {
            packageManagerComboBox.selectedItem = packageManagers[0]
        }
    }

    private fun updateFrameworkOptions() {
        val language = languageComboBox.selectedItem as FrontendLanguage
        val frameworks = FrontendFramework.getForLanguage(language)

        frameworkComboBox.removeAllItems()
        frameworks.forEach { frameworkComboBox.addItem(it) }

        if (frameworks.isNotEmpty()) {
            frameworkComboBox.selectedItem = frameworks[0]
        }
    }

    private fun updateTypeScriptVisibility() {
        val language = languageComboBox.selectedItem as FrontendLanguage
        typeScriptCheckBox.isVisible = language == FrontendLanguage.TS_JS
    }

    private fun createUIComponents() {
        mainPanel = panel {
            row(TauriBundle.message("project.settings.name")) {
                cell(nameTextField)
                    .resizableColumn()
                    .align(Align.FILL)
                    .validationOnInput { validateProjectName() }
                    .validationOnApply { validateProjectName() }
            }

            row(TauriBundle.message("project.settings.frontend.language")) {
                cell(languageComboBox)
                    .resizableColumn()
                    .align(Align.FILL)
            }

            row(TauriBundle.message("project.settings.package.manager")) {
                cell(packageManagerComboBox)
                    .resizableColumn()
                    .align(Align.FILL)
            }

            row(TauriBundle.message("project.settings.frontend.framework")) {
                cell(frameworkComboBox)
                    .resizableColumn()
                    .align(Align.FILL)
            }

            typescriptRow = row {
                cell(typeScriptCheckBox)
            }

            row {
                cell(descriptionLabel)
                    .align(Align.FILL)
            }
        }
    }

    private fun validateProjectName(): ValidationInfo? {
        return if (nameTextField.text.isNullOrBlank()) {
            ValidationInfo(TauriBundle.message("project.settings.name.empty"), nameTextField)
        } else {
            null
        }
    }

    fun getComponent(): JComponent = mainPanel

    fun validate(): List<ValidationInfo> {
        val errors = mutableListOf<ValidationInfo>()

        validateProjectName()?.let { errors.add(it) }

        return errors
    }

    fun updateSettings() {
        settings.apply {
            projectName = nameTextField.text
            language = languageComboBox.selectedItem as? FrontendLanguage ?: FrontendLanguage.TS_JS
            packageManager = packageManagerComboBox.selectedItem as? PackageManager ?: PackageManager.PNPM
            frontendFramework = frameworkComboBox.selectedItem as? FrontendFramework ?: FrontendFramework.VANILLA
            useTypeScript = typeScriptCheckBox.isSelected
        }
    }
}
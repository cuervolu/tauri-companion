package dev.cuervolu.tauricompanion.project.ui

import com.intellij.ide.util.projectWizard.ModuleWizardStep
import com.intellij.openapi.options.ConfigurationException
import dev.cuervolu.tauricompanion.project.TauriModuleBuilder
import javax.swing.JComponent

class TauriModuleWizardStep(private val moduleBuilder: TauriModuleBuilder) : ModuleWizardStep() {

    private val settingsPanel = TauriProjectSettingsPanel(moduleBuilder.settings)

    override fun getComponent(): JComponent {
        return settingsPanel.getComponent()
    }

    override fun updateDataModel() {
        settingsPanel.updateSettings()
    }

    override fun validate(): Boolean {
        val validationErrors = settingsPanel.validate()
        if (validationErrors.isNotEmpty()) {
            throw ConfigurationException(validationErrors[0].message)
        }
        return true
    }
}
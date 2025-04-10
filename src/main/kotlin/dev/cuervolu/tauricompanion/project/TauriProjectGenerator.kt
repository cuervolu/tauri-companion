package dev.cuervolu.tauricompanion.project

import com.intellij.ide.util.projectWizard.WebProjectTemplate
import com.intellij.openapi.module.Module
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.ValidationInfo
import com.intellij.openapi.util.io.FileUtil
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.platform.ProjectGeneratorPeer
import dev.cuervolu.tauricompanion.TauriBundle
import dev.cuervolu.tauricompanion.project.ui.TauriProjectSettingsPanel
import java.io.File
import java.io.IOException
import javax.swing.Icon
import javax.swing.JComponent

class TauriProjectGenerator : WebProjectTemplate<TauriProjectSettings>() {

    override fun getName(): String = TauriBundle.message("project.generator.name")

    override fun getDescription(): String = TauriBundle.message("project.generator.description")

    override fun getIcon(): Icon = TauriIcons.TAURI_ICON

    override fun getLogo(): Icon = TauriIcons.TAURI_ICON

    override fun generateProject(
        project: Project,
        baseDir: VirtualFile,
        settings: TauriProjectSettings,
        module: Module
    ) {
        try {
            // En una implementación real, llamaríamos al CLI de Tauri aquí
            val readme = File(baseDir.path, "README.md")
            FileUtil.writeToFile(readme, "# ${settings.projectName}\n\nTauri project created with ${settings.packageManager} and ${settings.frontendFramework}")
        } catch (e: IOException) {
            // Manejar errores de creación
        }
    }

    override fun createPeer(): ProjectGeneratorPeer<TauriProjectSettings> {
        return object : ProjectGeneratorPeer<TauriProjectSettings> {
            private val settings = TauriProjectSettings()
            private val settingsPanel = TauriProjectSettingsPanel(settings)

            override fun getComponent(): JComponent {
                return settingsPanel.getComponent()
            }

            override fun getSettings(): TauriProjectSettings {
                settingsPanel.updateSettings()
                return settings
            }

            override fun validate(): ValidationInfo? {
                val validationResults = settingsPanel.validate()
                return if (validationResults.isNotEmpty()) validationResults[0] else null
            }

            override fun buildUI(settingsStep: com.intellij.ide.util.projectWizard.SettingsStep) {
                settingsStep.addSettingsComponent(settingsPanel.getComponent())
            }

            override fun isBackgroundJobRunning(): Boolean = false
        }
    }
}
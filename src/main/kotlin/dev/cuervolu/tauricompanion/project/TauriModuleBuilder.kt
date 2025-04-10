package dev.cuervolu.tauricompanion.project

import com.intellij.ide.util.projectWizard.ModuleBuilder
import com.intellij.ide.util.projectWizard.ModuleWizardStep
import com.intellij.ide.util.projectWizard.WizardContext
import com.intellij.openapi.Disposable
import com.intellij.openapi.module.ModuleType
import com.intellij.openapi.options.ConfigurationException
import com.intellij.openapi.roots.ModifiableRootModel
import com.intellij.openapi.util.io.FileUtil
import dev.cuervolu.tauricompanion.TauriBundle
import dev.cuervolu.tauricompanion.project.ui.TauriModuleWizardStep
import java.io.File
import java.io.IOException
import javax.swing.Icon

class TauriModuleBuilder : ModuleBuilder() {

    var settings = TauriProjectSettings()

    override fun setupRootModel(modifiableRootModel: ModifiableRootModel) {
        val contentEntry = doAddContentEntry(modifiableRootModel)
        val moduleDir = contentEntry?.file ?: return

        // Aquí ejecutaríamos los comandos de CLI de Tauri para crear el proyecto
        // Por ahora, solo crearemos un archivo dummy para simular la creación del proyecto
        try {
            val projectDir = File(moduleDir.path)
            if (!projectDir.exists()) {
                projectDir.mkdirs()
            }

            // Crear un archivo placeholder
            val readme = File(projectDir, "README.md")
            FileUtil.writeToFile(readme, "# ${settings.projectName}\n\nTauri project created with ${settings.packageManager} and ${settings.frontendFramework}")

        } catch (e: IOException) {
            throw ConfigurationException("Failed to create project files: ${e.message}")
        }
    }

    override fun getModuleType(): ModuleType<*> {
        return ModuleType.EMPTY
    }

    override fun getCustomOptionsStep(context: WizardContext, parentDisposable: Disposable): ModuleWizardStep {
        return TauriModuleWizardStep(this)
    }

    override fun getPresentableName(): String {
        return TauriBundle.message("project.generator.name")
    }

    override fun getDescription(): String {
        return TauriBundle.message("project.generator.description")
    }

    override fun getNodeIcon(): Icon {
        return TauriIcons.TAURI_ICON
    }

    override fun getBuilderId(): String {
        return "TAURI_MODULE_BUILDER"
    }
}
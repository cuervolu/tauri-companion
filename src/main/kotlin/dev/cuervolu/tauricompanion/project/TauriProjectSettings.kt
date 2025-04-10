package dev.cuervolu.tauricompanion.project

class TauriProjectSettings {
    var projectName: String = ""
    var language: FrontendLanguage = FrontendLanguage.TS_JS
    var packageManager: PackageManager = PackageManager.PNPM
    var frontendFramework: FrontendFramework = FrontendFramework.VANILLA
    var useTypeScript: Boolean = true
}

enum class FrontendLanguage(val displayName: String) {
    TS_JS("TypeScript / JavaScript"),
    RUST("Rust"),
    DOTNET(".NET");

    override fun toString(): String = displayName
}

enum class PackageManager(val displayName: String, val command: String, val language: FrontendLanguage) {
    // JavaScript/TypeScript package managers
    NPM("npm", "npm", FrontendLanguage.TS_JS),
    YARN("Yarn", "yarn", FrontendLanguage.TS_JS),
    PNPM("pnpm", "pnpm", FrontendLanguage.TS_JS),
    BUN("Bun", "bun", FrontendLanguage.TS_JS),

    // Rust package manager
    CARGO("Cargo", "cargo", FrontendLanguage.RUST),

    // .NET package manager
    DOTNET("dotnet", "dotnet", FrontendLanguage.DOTNET);

    override fun toString(): String = displayName

    companion object {
        fun getForLanguage(language: FrontendLanguage): Array<PackageManager> {
            return entries.filter { it.language == language }.toTypedArray()
        }
    }
}

enum class FrontendFramework(val displayName: String, val template: String, val language: FrontendLanguage) {
    // JavaScript/TypeScript frameworks
    VANILLA("Vanilla", "vanilla", FrontendLanguage.TS_JS),
    REACT("React", "react", FrontendLanguage.TS_JS),
    VUE("Vue", "vue", FrontendLanguage.TS_JS),
    SVELTE("Svelte", "svelte", FrontendLanguage.TS_JS),
    SOLID("SolidJS", "solid", FrontendLanguage.TS_JS),
    ANGULAR("Angular", "angular", FrontendLanguage.TS_JS),
    PREACT("Preact", "preact", FrontendLanguage.TS_JS),

    // Rust frameworks
    YEW("Yew", "yew", FrontendLanguage.RUST),
    LEPTOS("Leptos", "leptos", FrontendLanguage.RUST),
    SYCAMORE("Sycamore", "sycamore", FrontendLanguage.RUST),

    // .NET frameworks
    BLAZOR("Blazor", "blazor", FrontendLanguage.DOTNET);

    override fun toString(): String = displayName

    companion object {
        fun getForLanguage(language: FrontendLanguage): Array<FrontendFramework> {
            return entries.filter { it.language == language }.toTypedArray()
        }
    }
}
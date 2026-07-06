package br.com.appshub.feature.home

/**
 * Um app do hub: [packageName] é o applicationId do app-alvo e [fallbackLabel] é o
 * nome exibido quando o app não está instalado (quando instalado, usamos o label
 * real reportado pelo PackageManager). Ver [resolveHubApps].
 */
data class HubApp(val packageName: String, val fallbackLabel: String)

/** Apps do hub, na ordem em que aparecem na lista. */
val HUB_APPS = listOf(
    HubApp("br.com.notes", "Notas"),
    HubApp("br.com.siteblocker", "Bloqueador de Sites"),
    HubApp("br.com.utils", "Utilitários"),
    HubApp("br.com.watchup", "WatchUp"),
    HubApp("br.com.gastos", "Gastos"),
)

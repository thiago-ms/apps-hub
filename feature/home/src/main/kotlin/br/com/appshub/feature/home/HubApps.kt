package br.com.appshub.feature.home

/**
 * Um app do hub: [packageName] é o applicationId do app-alvo e [fallbackLabel] é o
 * nome exibido quando o app não está instalado (quando instalado, usamos o label
 * real reportado pelo PackageManager). Ver [resolveHubApps].
 */
data class HubApp(val packageName: String, val fallbackLabel: String)

/**
 * Apps do hub, na ordem em que aparecem na lista.
 *
 * Ao acrescentar um app aqui, acrescente também o `<package>` correspondente ao
 * bloco `<queries>` do `app/src/main/AndroidManifest.xml`. Os dois registros andam
 * em par: sem o `<queries>`, o `PackageManager` devolve null para o app mesmo
 * instalado (restrição de visibilidade de pacotes do Android 11+), e a linha
 * aparece permanentemente esmaecida.
 */
val HUB_APPS = listOf(
    HubApp("br.com.notes", "Notas"),
    HubApp("br.com.siteblocker", "Bloqueador de Sites"),
    HubApp("br.com.utils", "Utilitários"),
    HubApp("br.com.watchup", "WatchUp"),
    HubApp("br.com.gastos", "Gastos"),
    HubApp("br.com.myapps", "MyApps"),
    HubApp("br.com.people", "Pessoas"),
    HubApp("br.com.wppchat", "WppChat"),
)

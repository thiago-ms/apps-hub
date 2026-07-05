package br.com.appshub.feature.home

import android.content.Context
import android.content.pm.PackageManager
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.graphics.drawable.toBitmap

/**
 * Estado de um app do hub resolvido no aparelho: quando instalado, traz o label
 * real, o versionName e o ícone real do app; quando não, cai no fallback.
 */
data class HubAppState(
    val app: HubApp,
    val installed: Boolean,
    val label: String,
    val version: String?,
    val icon: ImageBitmap?,
)

/**
 * Lê o [PackageManager] e resolve o estado de cada app de [HUB_APPS]. Depende da
 * declaração `<queries>` no AndroidManifest (API 30+) para enxergar os alvos.
 */
fun resolveHubApps(context: Context): List<HubAppState> {
    val pm = context.packageManager
    return HUB_APPS.map { app ->
        try {
            val info = pm.getPackageInfo(app.packageName, 0)
            val appInfo = info.applicationInfo
            HubAppState(
                app = app,
                installed = true,
                label = appInfo?.let { pm.getApplicationLabel(it).toString() } ?: app.fallbackLabel,
                version = info.versionName,
                icon = pm.getApplicationIcon(app.packageName).toBitmap().asImageBitmap(),
            )
        } catch (e: PackageManager.NameNotFoundException) {
            HubAppState(
                app = app,
                installed = false,
                label = app.fallbackLabel,
                version = null,
                icon = null,
            )
        }
    }
}

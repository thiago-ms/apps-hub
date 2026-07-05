package br.com.appshub.feature.home

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Android
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner

/**
 * Tela única do hub: lista os apps de [HUB_APPS], um por linha. App instalado é
 * clicável (abre pelo launcher) e mostra a versão de forma discreta; app ausente
 * fica esmaecido e sem ação.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HubScreen() {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var apps by remember { mutableStateOf(resolveHubApps(context)) }

    // Reavalia ao voltar pro hub: se um app foi instalado/atualizado enquanto
    // estávamos fora, a versão e o estado da linha se atualizam sozinhos.
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) apps = resolveHubApps(context)
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Apps Hub") }) },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(vertical = 8.dp),
        ) {
            items(apps, key = { it.app.packageName }) { state ->
                HubRow(state = state, onOpen = { openApp(context, state) })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HubRow(state: HubAppState, onOpen: () -> Unit) {
    val base = if (state.installed) Modifier.clickable(onClick = onOpen) else Modifier
    ListItem(
        modifier = base.alpha(if (state.installed) 1f else 0.45f),
        leadingContent = { AppIcon(state) },
        headlineContent = { Text(state.label, fontWeight = FontWeight.Medium) },
        supportingContent = {
            if (!state.installed) {
                Text(
                    "não instalado",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        },
        // Versão discreta: pequena e de baixo contraste, no canto — presente para
        // quem procura, sem competir com o nome do app.
        trailingContent = {
            val version = state.version
            if (state.installed && !version.isNullOrBlank()) {
                Text(
                    version,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        },
    )
}

@Composable
private fun AppIcon(state: HubAppState) {
    val size = 44.dp
    val shape = RoundedCornerShape(12.dp)
    val icon = state.icon
    if (icon != null) {
        Image(
            bitmap = icon,
            contentDescription = null,
            modifier = Modifier
                .size(size)
                .clip(shape),
        )
    } else {
        Box(
            modifier = Modifier
                .size(size)
                .clip(shape)
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                Icons.Filled.Android,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

/** Abre o app pelo intent de launcher; avisa se por algum motivo não for possível. */
private fun openApp(context: Context, state: HubAppState) {
    val intent = context.packageManager.getLaunchIntentForPackage(state.app.packageName)
    if (intent != null) {
        context.startActivity(intent)
    } else {
        Toast.makeText(context, "Não foi possível abrir ${state.label}", Toast.LENGTH_SHORT).show()
    }
}

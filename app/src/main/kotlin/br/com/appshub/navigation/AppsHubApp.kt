package br.com.appshub.navigation

import androidx.compose.runtime.Composable
import br.com.appshub.feature.home.HubScreen

/**
 * Raiz da UI. O hub é uma tela única (sem bottom navigation): apenas a lista de
 * apps. Se um dia houver mais de uma tela, reintroduza um NavHost aqui.
 */
@Composable
fun AppsHubApp() {
    HubScreen()
}

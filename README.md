# apps-hub — hub dos meus apps Android

App que lista os outros apps desta pasta (`notes`, `site-blocker`, `utilities`,
`watch-up`, `gastos`, `myapps`, `people`, `wpp-chat`) e, ao tocar num item,
abre o app correspondente **já instalado** no aparelho. Não embute os outros apps —
só os inicia via `Intent`.

O registro fica em **dois lugares que andam em par**: a constante `HUB_APPS` em
[`HubApps.kt`](feature/home/src/main/kotlin/br/com/appshub/feature/home/HubApps.kt)
e o bloco `<queries>` do
[`AndroidManifest.xml`](app/src/main/AndroidManifest.xml). Os dois são compilados
dentro do APK, então acrescentar um app exige um release novo do hub.

- **Linguagem/UI:** Kotlin + Jetpack Compose (Material 3)
- **Build:** Docker (Makefile / docker compose) — não precisa de JDK/Gradle/SDK no host
- **Package:** `br.com.appshub` · minSdk 26 · targetSdk 35 · compileSdk 35 · JDK 17

## Como funciona

- **Registro dos apps:** [`HubApps.kt`](feature/home/src/main/kotlin/br/com/appshub/feature/home/HubApps.kt)
  — lista fixa de `packageName` + nome de fallback.
- **Resolução no aparelho:** [`HubAppState.kt`](feature/home/src/main/kotlin/br/com/appshub/feature/home/HubAppState.kt)
  lê o `PackageManager` e, para cada app instalado, obtém label real, `versionName`
  e o ícone real.
- **Tela:** [`HubScreen.kt`](feature/home/src/main/kotlin/br/com/appshub/feature/home/HubScreen.kt)
  — um app por linha (`ListItem` em `LazyColumn`). Instalado → clicável
  (`getLaunchIntentForPackage`), com a versão num rótulo discreto no canto. Ausente
  → esmaecido, sem ação. Reavalia a lista no `ON_RESUME`.
- **Visibilidade de pacotes:** o [`AndroidManifest.xml`](app/src/main/AndroidManifest.xml)
  declara `<queries>` com os 4 packages — obrigatório no Android 11+ (API 30+),
  senão o hub não enxerga os outros apps.

### Adicionar/remover um app do hub

Edite a lista `HUB_APPS` em `HubApps.kt` **e** o bloco `<queries>` no
`AndroidManifest.xml` (os dois precisam conter o mesmo `packageName`).

## Módulos

```
:app             # host: MainActivity + AppsHubApp (tela única), tema/ícone, <queries>
:core:ui         # tema (AppsHubTheme) + componentes compartilhados
:feature:home    # o hub: registro, resolução via PackageManager e a tela
```

## Build

```bash
make image     # constrói a imagem Docker de build (1ª vez; baixa SDK/Gradle)
make wrapper   # gera o Gradle wrapper (1ª vez)
make apk           # APK debug em dist/appshub-<versão>-debug.apk
make dist-release  # APK release (assinado + R8) em dist/appshub-<versão>-release.apk
make dist-all      # debug + release de uma vez
```

Instalar no aparelho físico (adb via Docker):

```bash
./adb.sh authorize        # 1ª vez
./adb.sh build-install    # gera o APK e instala
./adb.sh logcat           # segue os logs
```

> Como o hub abre apps por `packageName`, para testar de verdade os apps-alvo
> (`br.com.notes`, `br.com.siteblocker`, `br.com.utils`, `br.com.watchup`)
> precisam estar instalados no mesmo aparelho — os que não estiverem aparecem
> esmaecidos como "não instalado".

## Conformidade

Não trata dados de clientes (CPF/e-mail/telefone) e usa apenas imagens Docker de
registries públicos.

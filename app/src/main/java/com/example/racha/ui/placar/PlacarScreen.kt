package com.example.racha.ui.placar

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.ActivityInfo
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.racha.R
import com.example.racha.domain.Lado
import com.example.racha.domain.Placar
import com.example.racha.util.PLACAR_MAXIMO

private val Dseg7 = FontFamily(Font(R.font.dseg7_classic_bold))
private val Verde = Color(0xFF1FE01F)
private val Amarelo = Color(0xFFFFEB3B)

@Composable
fun PlacarScreen(
    onVoltar: () -> Unit,
    viewModel: PlacarViewModel = viewModel(factory = PlacarViewModel.Factory)
) {
    val placar by viewModel.placar.collectAsStateWithLifecycle()

    var menuAberto by remember { mutableStateOf(false) }
    var editando by remember { mutableStateOf(false) }
    var confirmandoZerar by remember { mutableStateOf(false) }

    ModoPlacar()

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .safeDrawingPadding()
    ) {
        val tamanhoPontos = with(LocalDensity.current) {
            minOf(maxHeight * 0.38f, maxWidth * 0.17f).toSp()
        }
        val tamanhoFaltas = tamanhoPontos * 0.3f

        Column(modifier = Modifier.fillMaxSize()) {

            // Barra do topo: voltar à esquerda, editar e menu à direita
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onVoltar) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, "Voltar", tint = Color.White)
                }
                Spacer(Modifier.weight(1f))
                IconButton(onClick = { editando = true }) {
                    Icon(Icons.Default.Edit, "Editar placar", tint = Color.White)
                }
                Box {
                    IconButton(onClick = { menuAberto = true }) {
                        Icon(Icons.Default.MoreVert, "Mais opções", tint = Color.White)
                    }
                    DropdownMenu(
                        expanded = menuAberto,
                        onDismissRequest = { menuAberto = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Zerar placar") },
                            onClick = {
                                menuAberto = false
                                confirmandoZerar = true
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Trocar de lado") },
                            onClick = {
                                menuAberto = false
                                viewModel.trocarLados()
                            }
                        )
                    }
                }
            }

            // Pontos
            Row(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BotoesPonto(
                    podeMais = placar.pontosEsquerda < PLACAR_MAXIMO,
                    podeMenos = placar.pontosEsquerda > 0,
                    onMais = { viewModel.alterarPontos(Lado.ESQUERDA, 1) },
                    onMenos = { viewModel.alterarPontos(Lado.ESQUERDA, -1) }
                )
                Numero(placar.pontosEsquerda, tamanhoPontos, Verde, Modifier.weight(1f))
                Text(
                    text = "X",
                    color = Color.White,
                    fontSize = tamanhoFaltas,
                    fontWeight = FontWeight.Bold
                )
                Numero(placar.pontosDireita, tamanhoPontos, Verde, Modifier.weight(1f))
                BotoesPonto(
                    podeMais = placar.pontosDireita < PLACAR_MAXIMO,
                    podeMenos = placar.pontosDireita > 0,
                    onMais = { viewModel.alterarPontos(Lado.DIREITA, 1) },
                    onMenos = { viewModel.alterarPontos(Lado.DIREITA, -1) }
                )
            }

            // Faltas
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Faltas",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    BotaoRedondo("−", 44.dp, placar.faltasEsquerda > 0) {
                        viewModel.alterarFaltas(Lado.ESQUERDA, -1)
                    }
                    BotaoRedondo("+", 44.dp, placar.faltasEsquerda < PLACAR_MAXIMO) {
                        viewModel.alterarFaltas(Lado.ESQUERDA, 1)
                    }
                    Numero(placar.faltasEsquerda, tamanhoFaltas, Amarelo, Modifier.widthIn(min = 72.dp))
                    Text(
                        text = "X",
                        color = Color.White,
                        fontSize = tamanhoFaltas,
                        fontWeight = FontWeight.Bold
                    )
                    Numero(placar.faltasDireita, tamanhoFaltas, Amarelo, Modifier.widthIn(min = 72.dp))
                    BotaoRedondo("−", 44.dp, placar.faltasDireita > 0) {
                        viewModel.alterarFaltas(Lado.DIREITA, -1)
                    }
                    BotaoRedondo("+", 44.dp, placar.faltasDireita < PLACAR_MAXIMO) {
                        viewModel.alterarFaltas(Lado.DIREITA, 1)
                    }
                }
            }
        }
    }

    if (editando) {
        EditarPlacarDialog(
            placar = placar,
            onConfirmar = {
                viewModel.definir(it)
                editando = false
            },
            onCancelar = { editando = false }
        )
    }

    if (confirmandoZerar) {
        AlertDialog(
            onDismissRequest = { confirmandoZerar = false },
            title = { Text("Zerar placar") },
            text = { Text("Pontos e faltas dos dois lados voltarão a zero.") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.zerar()
                    confirmandoZerar = false
                }) { Text("Zerar") }
            },
            dismissButton = {
                TextButton(onClick = { confirmandoZerar = false }) { Text("Cancelar") }
            }
        )
    }
}

/** Deita a tela, mantém ela acesa e esconde as barras do sistema; desfaz tudo ao sair. */
@Composable
private fun ModoPlacar() {
    val context = LocalContext.current
    val view = LocalView.current

    DisposableEffect(Unit) {
        val activity = context.findActivity()
        val controller = activity?.window?.let { WindowCompat.getInsetsController(it, view) }

        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
        view.keepScreenOn = true
        controller?.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        controller?.hide(WindowInsetsCompat.Type.systemBars())

        onDispose {
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            view.keepScreenOn = false
            controller?.show(WindowInsetsCompat.Type.systemBars())
        }
    }
}

private fun Context.findActivity(): Activity? {
    var atual = this
    while (atual is ContextWrapper) {
        if (atual is Activity) return atual
        atual = atual.baseContext
    }
    return null
}

@Composable
private fun Numero(
    valor: Int,
    tamanho: androidx.compose.ui.unit.TextUnit,
    cor: Color,
    modifier: Modifier = Modifier
) {
    Text(
        text = valor.toString(),
        modifier = modifier,
        textAlign = TextAlign.Center,
        style = TextStyle(fontFamily = Dseg7, fontSize = tamanho, color = cor)
    )
}

@Composable
private fun BotoesPonto(
    podeMais: Boolean,
    podeMenos: Boolean,
    onMais: () -> Unit,
    onMenos: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BotaoRedondo("+", 64.dp, podeMais, onMais)
        BotaoRedondo("−", 64.dp, podeMenos, onMenos)
    }
}

@Composable
private fun BotaoRedondo(
    texto: String,
    tamanho: Dp,
    habilitado: Boolean,
    onClick: () -> Unit
) {
    FilledIconButton(
        onClick = onClick,
        enabled = habilitado,
        modifier = Modifier.size(tamanho),
        colors = IconButtonDefaults.filledIconButtonColors(
            containerColor = Color.White,
            contentColor = Color.Black,
            disabledContainerColor = Color(0xFF3A3A3A),
            disabledContentColor = Color(0xFF777777)
        )
    ) {
        Text(
            text = texto,
            fontSize = (tamanho.value * 0.5f).sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun EditarPlacarDialog(
    placar: Placar,
    onConfirmar: (Placar) -> Unit,
    onCancelar: () -> Unit
) {
    var pontosEsq by remember { mutableStateOf(placar.pontosEsquerda.toString()) }
    var pontosDir by remember { mutableStateOf(placar.pontosDireita.toString()) }
    var faltasEsq by remember { mutableStateOf(placar.faltasEsquerda.toString()) }
    var faltasDir by remember { mutableStateOf(placar.faltasDireita.toString()) }

    AlertDialog(
        onDismissRequest = onCancelar,
        title = { Text("Editar placar") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    CampoNumero("Pontos esq.", pontosEsq, { pontosEsq = it }, Modifier.weight(1f))
                    CampoNumero("Pontos dir.", pontosDir, { pontosDir = it }, Modifier.weight(1f))
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    CampoNumero("Faltas esq.", faltasEsq, { faltasEsq = it }, Modifier.weight(1f))
                    CampoNumero("Faltas dir.", faltasDir, { faltasDir = it }, Modifier.weight(1f))
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onConfirmar(
                    Placar(
                        pontosEsquerda = pontosEsq.toIntOrNull() ?: 0,
                        pontosDireita = pontosDir.toIntOrNull() ?: 0,
                        faltasEsquerda = faltasEsq.toIntOrNull() ?: 0,
                        faltasDireita = faltasDir.toIntOrNull() ?: 0
                    )
                )
            }) { Text("Salvar") }
        },
        dismissButton = {
            TextButton(onClick = onCancelar) { Text("Cancelar") }
        }
    )
}

@Composable
private fun CampoNumero(
    rotulo: String,
    valor: String,
    onChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = valor,
        onValueChange = { if (it.length <= 2 && it.all(Char::isDigit)) onChange(it) },
        label = { Text(rotulo) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = modifier
    )
}
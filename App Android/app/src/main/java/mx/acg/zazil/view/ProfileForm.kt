package mx.acg.zazil.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


/**
 * Composable que muestra el formulario de perfil del usuario con sus datos personales.
 * @author Melissa Mireles Rendón
 * @author Alberto Cebreros González
 * Organiza los campos de nombre, teléfono, correo y contraseña en filas.
 * @param [modifier] Modificador para personalizar la disposición y el estilo del formulario.
 */
@Composable
fun ProfileForm(modifier: Modifier = Modifier) {
    val nombre = remember { mutableStateOf("Ana Sofía Vázquez Delgado") }
    val telefono = remember { mutableStateOf("5567892234") }
    val correo = remember { mutableStateOf("ana.vazquez@gmail.com") }
    val contrasena = remember { mutableStateOf("********") }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Recuadro Nombre
            ProfileInputField(
                label = "Nombre",
                value = nombre.value,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                onValueChange = { nombre.value = it }
            )

            // Recuadro Teléfono
            ProfileInputField(
                label = "Teléfono",
                value = telefono.value,
                modifier = Modifier
                    .weight(0.5f)
                    .fillMaxWidth(),
                onValueChange = { telefono.value = it }
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Recuadro Correo
            ProfileInputField(
                label = "Correo",
                value = correo.value,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                onValueChange = { correo.value = it }
            )

            // Recuadro Contraseña
            ProfileInputField(
                label = "Contraseña",
                value = contrasena.value,
                modifier = Modifier
                    .weight(0.5f)
                    .fillMaxWidth(),
                onValueChange = { contrasena.value = it }
            )
        }
    }
}
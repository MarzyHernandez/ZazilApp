package mx.acg.zazil.view

import UserProfile
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
 * Composable que representa un formulario de perfil para editar la información del usuario.
 * Muestra campos para los nombres, apellidos, correo electrónico y teléfono del usuario,
 * permitiendo que se actualicen sus valores.
 *
 * @param profile El objeto UserProfile que contiene los datos actuales del usuario.
 * @param modifier Modificador opcional para personalizar el componente.
 *
 * @author Alberto Cebreros González
 * @author Melissa Mireles Rendón
 */
@Composable
fun ProfileForm(profile: UserProfile, modifier: Modifier = Modifier) {
    // Estado mutable para manejar los valores de los campos del formulario
    val nombres = remember { mutableStateOf(profile.nombres) }
    val apellidos = remember { mutableStateOf(profile.apellidos) }
    val telefono = remember { mutableStateOf(profile.telefono) }
    val correo = remember { mutableStateOf(profile.email) }

    // Estructura de la columna que contiene los campos del formulario
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp) // Espaciado vertical entre los elementos
    ) {
        // Fila para los campos de nombres y apellidos
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp) // Espaciado horizontal entre los campos
        ) {
            ProfileInputField(
                label = "Nombres",
                value = nombres.value,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                onValueChange = { nombres.value = it } // Actualiza el estado de nombres
            )

            ProfileInputField(
                label = "Apellidos",
                value = apellidos.value,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                onValueChange = { apellidos.value = it } // Actualiza el estado de apellidos
            )
        }

        // Fila para los campos de correo y teléfono
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp) // Espaciado horizontal entre los campos
        ) {
            ProfileInputField(
                label = "Correo",
                value = correo.value,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                onValueChange = { correo.value = it } // Actualiza el estado de correo
            )

            ProfileInputField(
                label = "Teléfono",
                value = telefono.value,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                onValueChange = { telefono.value = it } // Actualiza el estado de teléfono
            )
        }
    }
}

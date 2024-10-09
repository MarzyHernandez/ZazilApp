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




@Composable
fun ProfileForm(profile: UserProfile, modifier: Modifier = Modifier) {
    val nombres = remember { mutableStateOf(profile.nombres) }
    val apellidos = remember { mutableStateOf(profile.apellidos) }
    val telefono = remember { mutableStateOf(profile.telefono) }
    val correo = remember { mutableStateOf(profile.email) }

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
            ProfileInputField(
                label = "Nombres",
                value = nombres.value,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                onValueChange = { nombres.value = it }
            )

            ProfileInputField(
                label = "Apellidos",
                value = apellidos.value,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                onValueChange = { apellidos.value = it }
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ProfileInputField(
                label = "Correo",
                value = correo.value,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                onValueChange = { correo.value = it }
            )

            ProfileInputField(
                label = "Teléfono",
                value = telefono.value,
                modifier = Modifier
                    .weight(0.5f)
                    .fillMaxWidth(),
                onValueChange = { telefono.value = it }
            )
        }
    }
}

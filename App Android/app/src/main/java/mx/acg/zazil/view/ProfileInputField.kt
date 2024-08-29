package mx.acg.zazil.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Composable que muestra un campo de entrada para el perfil del usuario.
 * @author Melissa Mireles Rendón
 * @author Alberto Cebreros González
 * @param [label] Texto que describe el campo (e.g., "Nombre", "Teléfono").
 * @param [value] Valor actual del campo que se mostrará en la interfaz.
 * @param [onValueChange] Función lambda que se ejecuta cuando el valor cambia, permitiendo su actualización.
 * @param [modifier] Modificador para personalizar la disposición y el estilo del campo de entrada.
 */
@Composable
fun ProfileInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(Color(0xFFEFEEEE), shape = RoundedCornerShape(8.dp))
            .padding(8.dp)
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFE17F61)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            fontSize = 14.sp,
            color = Color.Gray
        )
    }
}
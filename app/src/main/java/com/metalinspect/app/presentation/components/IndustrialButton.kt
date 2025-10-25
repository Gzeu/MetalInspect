package com.metalinspect.app.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun IndustrialButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: ImageVector? = null,
    variant: ButtonVariant = ButtonVariant.Primary,
    size: ButtonSize = ButtonSize.Standard
) {
    val buttonHeight = when (size) {
        ButtonSize.Small -> 48.dp
        ButtonSize.Standard -> 56.dp
        ButtonSize.Large -> 64.dp
    }

    val colors = when (variant) {
        ButtonVariant.Primary -> ButtonDefaults.buttonColors()
        ButtonVariant.Secondary -> ButtonDefaults.outlinedButtonColors()
        ButtonVariant.Danger -> ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
        ButtonVariant.Success -> ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary)
    }

    when (variant) {
        ButtonVariant.Primary, ButtonVariant.Danger, ButtonVariant.Success -> {
            Button(
                onClick = onClick,
                enabled = enabled,
                modifier = modifier.height(buttonHeight),
                colors = colors,
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
            ) { ButtonContent(text, icon) }
        }
        ButtonVariant.Secondary -> {
            OutlinedButton(
                onClick = onClick,
                enabled = enabled,
                modifier = modifier.height(buttonHeight),
                colors = colors as ButtonColors,
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
            ) { ButtonContent(text, icon) }
        }
    }
}

@Composable
private fun ButtonContent(text: String, icon: ImageVector?) {
    if (icon != null) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
            Icon(imageVector = icon, contentDescription = null, modifier = Modifier.size(20.dp))
            Text(text = "  $text", style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Medium))
        }
    } else {
        Text(text = text, style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Medium))
    }
}

enum class ButtonVariant { Primary, Secondary, Danger, Success }

enum class ButtonSize { Small, Standard, Large }

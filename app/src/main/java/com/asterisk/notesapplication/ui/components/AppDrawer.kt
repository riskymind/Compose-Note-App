package com.asterisk.notesapplication.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asterisk.notesapplication.routing.NotesRouter
import com.asterisk.notesapplication.routing.Screen
import com.asterisk.notesapplication.ui.theme.NotesApplicationTheme
import com.asterisk.notesapplication.ui.theme.NotesThemeSettings

@Composable
fun AppDrawerHeader() {

    Row(Modifier.fillMaxWidth()) {
        Image(
            imageVector = Icons.Filled.Menu,
            contentDescription = "Drawer header icon",
            colorFilter = ColorFilter.tint(MaterialTheme.colors.onSurface),
            modifier = Modifier.padding(16.dp)
        )
        Text(text = "Notes", modifier = Modifier.align(CenterVertically))
    }

}

@Preview(showBackground = true)
@Composable
fun AppDrawerHeaderPreview() {
    NotesApplicationTheme {
        AppDrawerHeader()
    }
}


@Composable
fun ScreenNavigationButton(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val colors = MaterialTheme.colors
    val imageAlpha = if (isSelected) 1f else 0.6f
    val textColor = if (isSelected) colors.primary else colors.onSurface.copy(alpha = 0.6f)
    val background = if (isSelected) colors.primary.copy(alpha = 0.12f) else colors.surface

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp, top = 8.dp),
        color = background,
        shape = MaterialTheme.shapes.small
    ) {
        Row(
            modifier = Modifier
                .clickable(onClick = onClick)
                .fillMaxWidth()
                .padding(4.dp)
        ) {

            Image(
                imageVector = icon,
                contentDescription = "Screen Navigation button",
                colorFilter = ColorFilter.tint(textColor),
                alpha = imageAlpha
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(text = label,
                style = MaterialTheme.typography.body2,
                color = textColor,
                modifier = Modifier.fillMaxWidth())
        }
    }

}

@Preview(showBackground = true)
@Composable
fun ScreenNavigationButtonPreview() {
    ScreenNavigationButton(
        Icons.Filled.Home, "Home", true, {}
    )
}


@Composable
fun LightDarkThemeItem() {
    Row(Modifier.padding(8.dp)) {
        Text(
            text = "Turn on dark theme",
            style = MaterialTheme.typography.body2,
            color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
            modifier = Modifier
                .weight(1f)
                .padding(8.dp)
                .align(CenterVertically)
        )
        Switch(
            checked = NotesThemeSettings.isDarkThemeEnabled,
            onCheckedChange = { NotesThemeSettings.isDarkThemeEnabled = it },
            modifier = Modifier
                .padding(start = 8.dp, end = 8.dp)
                .align(CenterVertically)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LightDarkThemeItemPreview() {
    LightDarkThemeItem()
}


@Composable
fun AppDrawer(
    currentScreen: Screen,
    closeDrawerAction: () -> Unit,
) {
    Column(Modifier.fillMaxWidth()) {
        AppDrawerHeader()
        Divider(color = MaterialTheme.colors.onSurface.copy(.2f))
        ScreenNavigationButton(
            icon = Icons.Filled.Home,
            label = "Notes",
            isSelected = currentScreen == Screen.Notes,
            onClick = {
                NotesRouter.navigateTo(Screen.Notes)
                closeDrawerAction()
            }
        )

        ScreenNavigationButton(
            icon = Icons.Filled.Delete,
            label = "Trash",
            isSelected = currentScreen == Screen.Trash,
            onClick = {
                NotesRouter.navigateTo(Screen.Trash)
                closeDrawerAction()
            }
        )

        LightDarkThemeItem()
    }

}

@Preview(showBackground = true)
@Composable
fun AppDrawerPreview() {
    AppDrawer(Screen.Notes) {}
}
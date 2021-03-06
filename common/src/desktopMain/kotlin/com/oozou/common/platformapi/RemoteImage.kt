package com.oozou.common.platformapi

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import org.jetbrains.skija.Image.makeFromEncoded
import androidx.compose.foundation.Image
import com.oozou.common.util.ImageLoader


@Composable
actual fun RemoteImage(
  url: String,
  contentDescription: String?,
  modifier: Modifier,
  contentScale: ContentScale
) {
  val image = remember(url) { mutableStateOf<ImageBitmap?>(null) }
  LaunchedEffect(url) {
    ImageLoader.load(url)?.let {
      image.value = makeFromEncoded(it).asImageBitmap()
    }
  }
  if (image.value != null) {
    Image(
      bitmap = image.value!!,
      contentDescription = contentDescription,
      modifier = modifier,
      contentScale = contentScale
    )
  }
}
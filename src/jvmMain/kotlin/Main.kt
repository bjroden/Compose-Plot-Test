// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
import androidx.compose.material.MaterialTheme
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.SwingPanel
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.knowm.xchart.XChartPanel
import org.knowm.xchart.XYChartBuilder

@Composable
@Preview
fun App() {
    var text by remember { mutableStateOf("Hello, World!") }
    var count = remember { mutableStateOf(0) }
    var chart = remember { mutableStateOf(XYChartBuilder().build()) }

    MaterialTheme {
        Column(Modifier.fillMaxSize(), Arrangement.spacedBy(5.dp)) {
            Button(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = {count.value++}
            ) {
                Text(if (count.value == 0) text else "Clicked ${count.value}!")
            }
            SwingPanel(
                background = Color.Red,
                modifier = Modifier.size(570.dp, 270.dp).align(Alignment.CenterHorizontally),
                factory = {
                    XChartPanel(chart.value)
                }
            )
            Spacer(modifier = Modifier.height(20.dp))
            Button(modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = {
                    chart.value.addSeries("a", DoubleArray(1) { 2.0 }, DoubleArray(1) { 5.0 })
                }
            ) {
                Text("Add a")
            }
            Button(modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = {
                    chart.value.addSeries("b", DoubleArray(1) { 3.0 }, DoubleArray(1) { 8.0 })
                }
            ) {
                Text("Add b")
            }
            Button(modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = {
                    chart.value.addSeries("c", DoubleArray(1) { 5.0 }, DoubleArray(1) { 10.0 })
                }
            ) {
                Text("Add c")
            }
        }
    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}

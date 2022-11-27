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
import org.knowm.xchart.XYChart
import org.knowm.xchart.XYChartBuilder
import org.knowm.xchart.XYSeries
import org.knowm.xchart.style.Styler
import org.knowm.xchart.style.markers.SeriesMarkers
import kotlin.random.Random

val baseLineCoords = arrayOf(0.0, 1.0).toDoubleArray()

fun getRandomString(length: Int) : String {
    val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
    return (1..length)
        .map { allowedChars.random() }
        .joinToString("")
}

fun makeChart(): XYChart {
    val chart = XYChartBuilder().theme(Styler.ChartTheme.GGPlot2).build()
    chart.styler.defaultSeriesRenderStyle = XYSeries.XYSeriesRenderStyle.Scatter
    chart.styler.markerSize = 6
    val baseLine = chart.addSeries("Expected values", baseLineCoords, baseLineCoords)
    baseLine.xySeriesRenderStyle = XYSeries.XYSeriesRenderStyle.Line
    baseLine.marker = SeriesMarkers.NONE
    return chart
}

fun XYChart.reset() {
    this.seriesMap.clear()
    val baseLine = this.addSeries("Expected values", baseLineCoords, baseLineCoords)
    baseLine.xySeriesRenderStyle = XYSeries.XYSeriesRenderStyle.Line
    baseLine.marker = SeriesMarkers.NONE
}

fun randomCoord() = Pair(Random.nextDouble(1.0), Random.nextDouble(1.0))

fun coordNearLine(): Pair<Double, Double> {
    val baseCoord = Random.nextDouble(1.0)
    val xEpsilon = Random.nextDouble(0.05)
    val yEpsilon = Random.nextDouble(0.05)
    val x = baseCoord - xEpsilon
    val y = baseCoord - yEpsilon
    return Pair(x, y)
}

@Composable
@Preview
fun App() {
    var text by remember { mutableStateOf("Hello, World!") }
    var count = remember { mutableStateOf(0) }
    var chart = remember { mutableStateOf(makeChart()) }

    MaterialTheme {
        Column(Modifier.fillMaxSize(), Arrangement.spacedBy(5.dp)) {
            Button(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = { count.value++ }
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
                    val pairs = (1..5).map { randomCoord() }
                    val xs = pairs.map { it.first }.toDoubleArray()
                    val ys = pairs.map { it.second }.toDoubleArray()
                    chart.value.addSeries(getRandomString(5), xs, ys)
                }
            ) {
                Text("Add random coordinates")
            }
            Button(modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = {
                    val pairs = (1..5).map { coordNearLine() }
                    val xs = pairs.map { it.first }.toDoubleArray()
                    val ys = pairs.map { it.second }.toDoubleArray()
                    chart.value.addSeries(getRandomString(5), xs, ys)
                }
            ) {
                Text("Add coordinates near line")
            }
            Button(modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = { chart.value.reset() }
            ) {
                Text("Clear data")
            }
        }
    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}

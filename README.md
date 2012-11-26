scalaplot
=========

This is a library for quick and easy plotting of simple plots (such as XY line plots, scatter plots) and supports outputs using a few different engines (such as Gnuplot, JFreeGraph, and matplotlib). **Note:** The project is still very much in *alpha*. If you just need a clean way to interface Java with gnuplot, see [gnujavaplot](http://gnujavaplot.sourceforge.net/JavaPlot/About.html).

## Requirements

- maven
- *for gnuplot*: [gnuplot 4.6](http://www.gnuplot.info/) with pdf support (on the mac+[homebrew](http://mxcl.github.com/homebrew/), `brew install pdflib-lite gnuplot`)
- *for jfreechart*: None

## Installation

### Maven dependency

The easiest (and recommended) way to use scalaplot is as a maven dependency. Insert the following in your `pom` file:

```xml
<repositories>
  <repository>
    <id>sameer-snapshots</id>
    <name>Sameer repository</name>
    <url>https://github.com/sameersingh/maven-repo/raw/master/snapshots</url>
    <snapshots>
      <enabled>true</enabled>
    </snapshots>
  </repository>
  ...
</repositories>
...
<dependencies>
  ...
  <dependency>
    <groupId>org.sameersingh.scalaplot</groupId>
    <artifactId>scalaplot</artifactId>
    <version>0.1-SNAPSHOT</version>
  </dependency>
  ...
</dependencies>
```

### Install from Source

Download the source and make sure everything is up and running:

```shell
$ mvn install
```

Then include the dependency (as above) into your `pom.xml`.
	
## Creating Charts

Currently, the library supports line charts.

### XY Line Charts

First step is to get your data into `Seq[Double]`.

```scala
val x = (1 until 100).map(_.toDouble)
val y = x.map(i => i*i)
```

Create a dataset that represents these sequences.

```scala
val series = new MemXYSeries(x, y, "Square")
val data = new XYData(series)
```

You can add more series too.

```scala
data += new MemXYSeries(x, x.map(i => i*i*i), "Cube")
```

Let's create the chart.

```scala
val chart = new XYChart("Powers!", data)
chart.showLegend = true
```

## Rendering Charts

Even though multiple backends are being supported to render the charts, gnuplot is the most actively developed and supported since it allows post plotting customizations (editing the script files), may possible output formats, and ease of use.

### Gnuplot

Generates gnuplot scripts that will need to be run to actually generate the images.

```scala
val plotter = new GnuplotPlotter(chart)
plotter.writeToPdf("dir/", "name")
```
	
followed by

```shell
$ cd dir/
$ gnuplot name.gpl
$ open name.pdf
```

produces

![Example gnuplot output](https://github.com/sameersingh/scalaplot/raw/master/docs/img/gnuplot.png)

### JFreegraph

JFreegraph can also be called similarly to produce pdf plots (use `JFGraphPlotter`).
However, it also supports a `gui()` option for when you just want to see the graph.

```scala
val plotter = new JFGraphPlotter(chart)
plotter.gui()
```

produces

![Example jfreegraph output](https://github.com/sameersingh/scalaplot/raw/master/docs/img/jfreegraph.png)

### Matplotlib

Currently not supported.

## Customizations

List of various customizations to charts, and how well they are supported.

*Coming soon!*

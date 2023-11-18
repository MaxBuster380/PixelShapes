# PixelShapes

- I - Introduction
- II - Download
- III - Use
- IV - About

### I - Introduction

This project aims to create usable pixel shapes. A pixel shape is a collection of integer coordinates. Think of it like opening MSPaint and using black on a white background ; the black pixels make up a pixel shape. 

### II - Download

__Gradle__

In `build.gradle` :

```gradle
allprojects {
	repositories {
		/*...*/
		maven { url 'https://jitpack.io' }
	}
}
```

```gradle
dependencies {
    implementation 'com.github.MaxBuster380:PixelShapes:alpha-1.2.1'
}
```

__Maven__

In `pom.xml` :
```xml
<repositories>
	<repository>
	    <id>jitpack.io</id>
	    <url>https://jitpack.io</url>
	</repository>
</repositories>
```

```xml
<dependency>
    <groupId>com.github.MaxBuster380</groupId>
    <artifactId>PixelShapes</artifactId>
    <version>alpha-1.2.1</version>
</dependency>
```

### III - Use

A pixel shape is a set of 2-dimensional integer values.

All pixel shapes implement the `PixelShape` interface :

```kotlin
interface PixelShape : Iterable<Pair<Int, Int>> {
    fun contains(point: Pair<Int, Int>): Boolean
    fun getBox(): BoxPixelShape
}
```

A pixel shape is, by default, read-only. Read/write pixel shapes implement the `MutablePixelShape` interface :

```kotlin
interface MutablePixelShape : PixelShape {
    // PixelShape methods
    fun contains(point: Pair<Int, Int>): Boolean
    fun getBox(): BoxPixelShape

    fun add(point: Pair<Int, Int>)
    fun add(shape: PixelShape)
    fun remove(point: Pair<Int, Int>)
    fun remove(shape: PixelShape)
}
```

There are currently three implementations of `PixelShape`:

- `SetMutablePixelShape`, stores coordinates using a standard Java Set.
- `GridMutablePixelShape`, stores coordinates in a 2-dimensional grid.
- `BoxPixelShape`, stores two ranges for X and Y each.

### IV - About

By MaxBuster380

[GitHub Repository](https://github.com/MaxBuster380/PixelShapes)
# PixelShapes

- I - Introduction
- II - Download
- III - About

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
    implementation 'com.github.MaxBuster380:PixelShapes:alpha-1.1.0'
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
    <version>alpha-1.1.0</version>
</dependency>
```

### III - About

By MaxBuster380

[GitHub Repository](https://github.com/MaxBuster380/PixelShapes)
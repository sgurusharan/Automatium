# Automatium
A web automation framework based on Selenium Java and Page Object Model

## Importing Latest Version (alpha1.0) : 
You can import this library into a gradle project by following the below steps:

- Since this project has not made into maven repo yet - I am currently using JitPack for distribution. So add the JitPack repo to your build's repositories:
```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
}
```
- Now you can add the dependency:
```
dependencies {
    compile 'com.github.sgurusharan:Automatium:alpha1.0-SNAPSHOT'
}
```

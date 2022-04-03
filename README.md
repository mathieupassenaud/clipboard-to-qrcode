# Clipboard to QR-Code

## Usage

Shows an icon in your system tray : 

![](.README_images/740d2a69.png)

A simple click on it shows the content of your clipboard in a simple QRCode : 

![](.README_images/85a14abc.png)

## Build

Needs Java and Maven

```
mvn clean compile assembly:single
```

## Run

```
java -jar target/qr-code-clipboard-1.0-SNAPSHOT-jar-with-dependencies.jar
```


 
package fr.mathieupassenaud.qrcodeclipboard;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import javax.swing.*;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class QrCodePanel extends JFrame {


    JLabel content = new JLabel("Content");


    JFrame frame;

    public QrCodePanel(){
        super("qr code clipboard");
        this.frame = this;
        setUndecorated(true);
        setAlwaysOnTop(true);
        setLayout(new BorderLayout());
        setSize(50, 100);


        String data = "error";
        try {
            data = (String) Toolkit.getDefaultToolkit()
                    .getSystemClipboard().getData(DataFlavor.stringFlavor);
        } catch (UnsupportedFlavorException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        content.setText(data);

        BufferedImage image = generateQRCodeImage(data);

        JLabel wIcon = new JLabel(new ImageIcon(image));

        JPanel imagePanel = new JPanel();
        imagePanel.add(wIcon);

        add(imagePanel, BorderLayout.CENTER);
        add(content, BorderLayout.NORTH);
        pack();
    }

    public static BufferedImage generateQRCodeImage(String barcodeText) {
        QRCodeWriter barcodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix =
                null;
        try {
            bitMatrix = barcodeWriter.encode(barcodeText, BarcodeFormat.QR_CODE, 200, 200);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }
}

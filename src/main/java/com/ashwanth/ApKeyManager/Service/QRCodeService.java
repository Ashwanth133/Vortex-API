package com.ashwanth.ApKeyManager.Service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class QRCodeService {

    public byte[] generateQR(String text,int width,int height ) throws WriterException , IOException {

        //import qr
        QRCodeWriter qrCodeWriter = new QRCodeWriter();

        //create the qr by text and convert to bitmatrix
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE,width,height);

        //we need to return bytearray as image
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        MatrixToImageWriter.writeToStream(bitMatrix,"PNG",outputStream);

        return  outputStream.toByteArray();
    }
}

package com.ashwanth.ApKeyManager.Controller;

import com.ashwanth.ApKeyManager.Service.QRCodeService;
import com.google.zxing.WriterException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.stylesheets.MediaList;

import java.io.IOException;
@RestController

public class QRCodeController {

    private final QRCodeService qrCodeService;

    public QRCodeController(QRCodeService qrCodeService) {
        this.qrCodeService = qrCodeService;
    }


    @GetMapping("/generate-qr")
    public ResponseEntity<byte[]> generateQrCode (
            @RequestParam String text,
            @RequestParam(defaultValue = "300") int width,
            @RequestParam(defaultValue = "300" ) int height
    ){

        try{
            if(text == null || text.isEmpty()){
                return  ResponseEntity.badRequest().build();
            }
            byte[] qr = qrCodeService.generateQR(text, width, height);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);

            return  ResponseEntity.ok()
                    .headers(headers)
                    .body(qr);



        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

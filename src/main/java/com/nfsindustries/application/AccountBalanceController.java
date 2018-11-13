package com.nfsindustries.application;

import com.nfsindustries.response.BalanceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class AccountBalanceController {

    private final StorageService storageService;

    private final AtomicLong counter = new AtomicLong();

    @Autowired
    public AccountBalanceController(StorageService storageService) {
        this.storageService = storageService;
    }

    @RequestMapping("/getbalance/")
    public BalanceResponse getBalance(@RequestParam("file") MultipartFile file) {
        storageService.store(file);
        BalanceResponse balanceResponse = new BalanceResponse(0, 0, 0, 0);
        return balanceResponse;
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.badRequest().build();
    }

    private static boolean validateAgainstXSD(InputStream xml, InputStream xsd)
    {
        try
        {
            SchemaFactory factory =
                    SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new StreamSource(xsd));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(xml));
            return true;
        }
        catch(Exception ex)
        {
            return false;
        }
    }
}

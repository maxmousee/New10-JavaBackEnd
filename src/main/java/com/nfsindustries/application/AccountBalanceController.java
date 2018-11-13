package com.nfsindustries.application;

import com.nfsindustries.response.BalanceResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicLong;

import static java.lang.ClassLoader.getSystemResourceAsStream;

@RestController
public class AccountBalanceController {
    private static Logger LOGGER = LoggerFactory.getLogger(AccountBalanceController.class);

    private final StorageService storageService;

    private final AtomicLong counter = new AtomicLong();

    @Autowired
    public AccountBalanceController(StorageService storageService) {
        this.storageService = storageService;
    }

    @RequestMapping("/getbalance/")
    public BalanceResponse getBalance(@RequestParam("file") MultipartFile file) {
        boolean isValidFile = false;
        storageService.store(file);
        try {
            InputStream inputStream = file.getInputStream();
            isValidFile = AccountBalanceController.validateAgainstXSD(inputStream);
        } catch (Exception ex) {
            LOGGER.error(ex.toString());
        }
        LOGGER.info("ISVALIDFILE" + isValidFile);
        BalanceResponse balanceResponse = new BalanceResponse(0, 0, 0, 0);
        return balanceResponse;
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.badRequest().build();
    }

    private static boolean validateAgainstXSD(InputStream xml)
    {
        try
        {
            Resource resource = new ClassPathResource("camt.053.001.02.xsd");
            InputStream xsd = resource.getInputStream();
            SchemaFactory factory =
                    SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new StreamSource(xsd));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(xml));
            return true;
        }
        catch(Exception ex)
        {
            LOGGER.error(ex.toString());
            return false;
        }
    }
}

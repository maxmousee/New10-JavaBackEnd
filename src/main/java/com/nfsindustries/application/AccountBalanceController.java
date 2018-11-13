package com.nfsindustries.application;

import com.nfsindustries.business.BalanceCalculator;
import com.nfsindustries.model.Document;
import com.nfsindustries.response.BalanceResponse;
import com.nfsindustries.utils.Camt053Parser;
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

    Camt053Parser camt053Parser = new Camt053Parser();

    BalanceCalculator balanceCalculator = new BalanceCalculator();

    @Autowired
    public AccountBalanceController(StorageService storageService) {
        this.storageService = storageService;
    }

    @RequestMapping("/getbalance/")
    public BalanceResponse getBalance(@RequestParam("file") MultipartFile file) throws Exception {
        storageService.store(file);
        InputStream inputStream = file.getInputStream();
        Document document = camt053Parser.parse(inputStream);
        int daysInDebt = balanceCalculator.getDaysInDebt(document);
        double netBalance = balanceCalculator.getNetBalance(document);
        double startBalance = balanceCalculator.getStartBalance(document);
        double endBalance = balanceCalculator.getEndBalance(document);
        BalanceResponse balanceResponse = new BalanceResponse(startBalance, endBalance, netBalance, daysInDebt);
        return balanceResponse;
    }

    @ExceptionHandler
    public ResponseEntity<?> handleException(StorageFileNotFoundException exc) {
        LOGGER.error(exc.toString());
        return ResponseEntity.badRequest().build();
    }

}

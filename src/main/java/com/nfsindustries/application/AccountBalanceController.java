package com.nfsindustries.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.concurrent.atomic.AtomicLong;

@Controller
public class AccountBalanceController {

    private final StorageService storageService;

    private final AtomicLong counter = new AtomicLong();

    @Autowired
    public AccountBalanceController(StorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping("/getbalance/")
    public String getBalance(@RequestParam("file") MultipartFile file,
                                      RedirectAttributes redirectAttributes) {
        storageService.store(file);
        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");

        return "redirect:/";
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }
}

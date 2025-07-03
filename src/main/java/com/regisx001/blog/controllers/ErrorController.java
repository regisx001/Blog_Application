package com.regisx001.blog.controllers;

// This class has been migrated to GlobalExceptionHandler
// All exception handlers are now consolidated in:
// com.regisx001.blog.exceptions.GlobalExceptionHandler

import org.springframework.web.bind.annotation.RestController;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class ErrorController {

    // All exception handlers have been moved to GlobalExceptionHandler
    // This class can be safely deleted or kept as a placeholder

    public ErrorController() {
        log.info("ErrorController created - Exception handling is now in GlobalExceptionHandler");
    }
}
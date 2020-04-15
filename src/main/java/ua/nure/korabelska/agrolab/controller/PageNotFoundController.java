package ua.nure.korabelska.agrolab.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
@Slf4j
public class PageNotFoundController {
    @ExceptionHandler(NoHandlerFoundException.class)
    public String handleError404() {
        log.info("IN handleError404 - 404 redirect occurred");
        return "redirect:/";
    }
}

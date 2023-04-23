package com.example.demo.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class IndexController implements ErrorController {

    private final static String PATH = "/error";
    @RequestMapping(PATH)
    @ResponseBody
    public void getErrorPath(HttpServletRequest request, HttpServletResponse response) {
        try {
            request.getRequestDispatcher("/").forward(request, response);

        } catch(ServletException e) {

        } catch(IOException e) {

        }
    }
}

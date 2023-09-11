package com.goit.security.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/public")
public class ForAllController {
  
  @GetMapping("/guest")
  public @ResponseBody  Map<String,String> getResponse(){
    return Map.of("resp","hello guest");
  } 
  
}

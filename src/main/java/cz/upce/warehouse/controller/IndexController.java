package cz.upce.warehouse.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/")
@CrossOrigin
public class IndexController {

    @RequestMapping
    public String index(){
        return "Hello";
    }
}

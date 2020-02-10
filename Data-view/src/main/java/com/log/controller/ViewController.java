package com.log.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @ClassName: ViewController
 * @Description:
 * @Author: xiedong
 * @Date: 2020/2/10 13:49
 */
@Controller
public class ViewController {
    @RequestMapping("/")
    public String goHome(){
        return "index";
    }
}

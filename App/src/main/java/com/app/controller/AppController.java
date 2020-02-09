package com.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @ClassName: AppController
 * @Description:
 * @Author: xiedong
 * @Date: 2020/2/9 13:30
 */
@Controller
public class AppController {

    @RequestMapping("/a")
    public String toA(){
        return "a";
    }
    @RequestMapping("/b")
    public String toB(){
        return "b";
    }
}

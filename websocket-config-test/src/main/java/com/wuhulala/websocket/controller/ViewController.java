package com.wuhulala.websocket.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 作甚的
 *
 * @author wuhulala
 * @version 1.0
 * @since 2018/1/21
 */
@Controller
public class ViewController {
    @RequestMapping("{path}.htm")
    public String chatView(@PathVariable("path") String path){
        return path;
    }

}

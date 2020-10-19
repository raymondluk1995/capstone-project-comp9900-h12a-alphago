package com.zxh.Controller;

import com.zxh.Config.WebsocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

/**
 * @program: chatroot
 * @description:
 * @author: XIAO HAN
 * @create: 2020-10-15 19:12
 **/
@Controller
public class HelloController {
    @Autowired
    WebsocketServer websocketServer;

    @RequestMapping("hello")
    public ModelAndView hello(){
        return new ModelAndView("index");
    }

    @RequestMapping("send/{uid}")
    public void send(@PathVariable String uid) throws IOException {
        WebsocketServer.sendMsg(uid , "From server!!");
    }
}
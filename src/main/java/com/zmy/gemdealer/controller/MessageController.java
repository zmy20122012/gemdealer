package com.zmy.gemdealer.controller;

import com.zmy.gemdealer.dto.Greeting;
import com.zmy.gemdealer.dto.HelloMessage;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

@Controller
public class MessageController {

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting send(@Header("simpSessionId")String sessionId, HelloMessage message) {
        // initialize dealer and investors
//        GemPool gem = new GemPool(2);
        System.out.println(sessionId);
        return new Greeting("Hello, "+ HtmlUtils.htmlEscape(message.getName())+"!");
    }
}

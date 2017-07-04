package com.maciejsobala.aws.webpush;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@RestController
@EnableWebMvc
public class PingPongEndpoint {

    @RequestMapping(path = "/ping", method = RequestMethod.GET)
    public String ping() {
        return "pong pong";
    }
}

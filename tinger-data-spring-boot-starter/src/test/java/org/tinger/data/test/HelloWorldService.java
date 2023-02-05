package org.tinger.data.test;

import org.springframework.stereotype.Component;

@Component
public class HelloWorldService {
    public String welcome() {
        return "hello world";
    }
}

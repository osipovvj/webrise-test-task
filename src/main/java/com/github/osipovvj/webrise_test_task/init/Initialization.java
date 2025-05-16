package com.github.osipovvj.webrise_test_task.init;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class Initialization {

    @PostConstruct
    public void init() {}
}

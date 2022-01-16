package ru.senin.pk.split.check.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Component used to show spring application context beans
 */
@Component
@ConditionalOnProperty(prefix = "application", name = "show.context")
public class ShowContextComponent implements CommandLineRunner {

    private ApplicationContext applicationContext;

    @Autowired
    public void CommandLineRunner(
            ApplicationContext applicationContext
    ) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Context beans on startup:");
        String[] beans = applicationContext.getBeanDefinitionNames();
        Arrays.stream(beans).sorted().forEach(System.out::println);
    }
}

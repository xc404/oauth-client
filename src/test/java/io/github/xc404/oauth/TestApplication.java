package io.github.xc404.oauth;

import io.github.xc404.oauth.springboot.OAuthClientConfiguration;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

/**
 * @Author chaox
 * @Date 12/26/2023 3:30 PM
 */
@SpringBootApplication
@Import(OAuthClientConfiguration.class)
@ComponentScan("io.github.xc404")
public class TestApplication
{

}

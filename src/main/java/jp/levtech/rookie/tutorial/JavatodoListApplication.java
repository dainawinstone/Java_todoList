package jp.levtech.rookie.tutorial;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("jp.levtech.rookie.tutorial.repository.mybatis")
public class JavatodoListApplication {

	public static void main(String[] args) {
		SpringApplication.run(JavatodoListApplication.class, args);
	}

}

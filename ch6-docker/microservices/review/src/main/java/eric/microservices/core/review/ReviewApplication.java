package eric.microservices.core.review;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;;

@SpringBootApplication
@ComponentScan("eric")
public class ReviewApplication {
	private static final Logger LOG = LoggerFactory.getLogger(ReviewApplication.class);
	public static void main(String[] args) {
		ConfigurableApplicationContext ctx = SpringApplication.run(ReviewApplication.class, args);
		String mysqlUri = ctx.getEnvironment().getProperty("spring.datasource.url");
		LOG.info("Connected to MySQL: " + mysqlUri);
	}

}

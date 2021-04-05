package socialNetwork;

import io.sentry.Sentry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;



@SpringBootApplication
public class SocialNetworkApplication {


	public static void main(String[] args) {
		Sentry.capture("Application started");
		SpringApplication.run(SocialNetworkApplication.class, args);
	}

}

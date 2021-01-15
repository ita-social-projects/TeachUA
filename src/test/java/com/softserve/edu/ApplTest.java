package com.softserve.edu;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

//@SpringBootTest
@Slf4j
public class ApplTest {
	//private static final Logger log = LoggerFactory.getLogger(ApplTest.class);

	@Test
    public void checkApp() {
        // From Maven
        //System.out.println("surefire.application.password = " + System.getProperty("surefire.application.password"));
        // From OS
        //System.out.println("JWT_SECRET = " + System.getenv("JWT_SECRET"));
        //System.out.println("DATASOURCE_URL = " + System.getenv("DATASOURCE_URL"));
        //System.out.println("DATASOURCE_USER = " + System.getenv("DATASOURCE_USER"));
        //System.out.println("DATASOURCE_PASSWORD = " + System.getenv("DATASOURCE_PASSWORD"));
		// From Eclipse/Idea
        //System.out.println("MY_PASSWORD = " + System.getenv().get("MY_PASSWORD"));
        //
	    log.info("JWT_SECRET = " + System.getenv("JWT_SECRET"));
	    log.info("DATASOURCE_URL = " + System.getenv("DATASOURCE_URL"));
	    log.info("DATASOURCE_USER = " + System.getenv("DATASOURCE_USER"));
	    log.info("DATASOURCE_PASSWORD = " + System.getenv("DATASOURCE_PASSWORD"));
	    log.info("MY_PASSWORD = " + System.getenv().get("MY_PASSWORD"));
        //
        boolean isCredentials = true;
        isCredentials = isCredentials && (System.getenv("JWT_SECRET") != null) && !System.getenv("JWT_SECRET").isEmpty();
        isCredentials = isCredentials && (System.getenv("DATASOURCE_URL") != null) && !System.getenv("DATASOURCE_URL").isEmpty();
        isCredentials = isCredentials && (System.getenv("DATASOURCE_USER") != null) && !System.getenv("DATASOURCE_USER").isEmpty();
        isCredentials = isCredentials && (System.getenv("DATASOURCE_PASSWORD") != null) && !System.getenv("DATASOURCE_PASSWORD").isEmpty();
        Assertions.assertTrue(isCredentials);
	}
	
}

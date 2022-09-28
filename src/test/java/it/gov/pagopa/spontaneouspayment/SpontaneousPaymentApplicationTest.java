package it.gov.pagopa.spontaneouspayment;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import it.gov.pagopa.spontaneouspayment.initializer.Initializer;

@SpringBootTest
@ContextConfiguration(initializers = {Initializer.class})
class SpontaneousPaymentApplicationTest {
	
	@Test
	void contextLoads() {
		assertTrue(true); // it just tests that an error has not occurred
	}

}

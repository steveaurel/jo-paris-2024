package com.infoevent.eventservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.Mockito.mockStatic;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
class EventServiceApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	public void testMain() {
		try (var mocked = mockStatic(SpringApplication.class)) {
			mocked.when(() -> SpringApplication.run(any(Class.class), any(String[].class)))
					.thenReturn(null);

			EventServiceApplication.main(new String[]{});

			mocked.verify(() -> SpringApplication.run(EventServiceApplication.class, new String[]{}));
		}
	}

}

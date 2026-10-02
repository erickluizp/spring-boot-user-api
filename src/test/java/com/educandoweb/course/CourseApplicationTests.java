package com.educandoweb.course;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = "JWT_SECRET=minha-chave-secreta-com-mais-de-32-bytes")
class CourseApplicationTests {

	@Test
	void contextLoads() {
	}

}
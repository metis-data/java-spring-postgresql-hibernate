package io.metisdata.demo.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.FixedHostPortGenericContainer;
import org.testcontainers.utility.DockerImageName;


@SpringBootTest
@AutoConfigureMockMvc
public class TitleRatingsControllerTests {

	@Autowired
	private MockMvc mockMvc;

	public static GenericContainer database;
	public static GenericContainer otelCollector;

	@BeforeAll
	public static void setUp(){
		if(System.getenv("MOCK_CONTAINER_DEPENDENCIES") != null){
			database = new FixedHostPortGenericContainer("public.ecr.aws/o2c0x5x8/metis-demo-mini-db:latest")
			.withFixedExposedPort(5432, 5432)
			.withNetworkAliases("database");
			database.start();

			otelCollector = new FixedHostPortGenericContainer("public.ecr.aws/o2c0x5x8/metis-otel-collector:latest")
			.withFixedExposedPort(4318, 4318)
			.withEnv("METIS_API_KEY", System.getenv("METIS_API_KEY"))
			.withEnv("CONNECTION_STRING", "postgresql://postgres:postgres@database:5432/demo?schema=imdb")
			.withEnv("LOG_LEVEL", "debug")
			.withNetworkAliases("otelCollector");
			
			otelCollector.start();

		}
	}

	@Test
	public void getBestMoviesShouldReturnMovies() throws Exception {
		this.mockMvc.perform(get("/titles/ratings/best")).andDo(print()).andExpect(status().isOk());
	}

	@AfterAll
	public static void tearDown(){
		try{
			Thread.sleep(30000);

			if(database != null){
				otelCollector.close();
				database.close();
			}
		}catch(Exception e){}
	}
}

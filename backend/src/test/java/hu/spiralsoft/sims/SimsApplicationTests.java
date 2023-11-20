package hu.spiralsoft.sims;

import hu.spiralsoft.sims.entities.User;
import hu.spiralsoft.sims.repositories.UserRepository;
import hu.spiralsoft.sims.security.http.RegisterRequest;

import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SimsApplicationTests {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;



	@Test
	void Register() throws Exception {
		RegisterRequest registerRequest = RegisterRequest.builder()
				.email("regteszt@elek.hu")
				.password("jelszo12345")
				.build();

		String requestBody = registerRequest.toString();

		MvcResult result = this.mockMvc.perform(post("/api/v1/user/register").content(requestBody).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content()
						.contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andReturn();

		String responseBody = result.getResponse().getContentAsString();
		JSONObject jsonObject = new JSONObject(responseBody);
		String token = jsonObject.getString("token");


		/*this.mockMvc.perform(get("/api/v1/test").header("Authorization","Bearer "+token))
				.andExpect(status().isOk());*/

	}

	@Test
	void Authentication() throws Exception {
		String password = "jelszo12345";
		User testUser = User.builder()
				.email("teszt@elek.hu")
				.password(this.passwordEncoder.encode(password))
				.build();
		this.userRepository.save(testUser);
		//JSON
		String requestBody = String.format("{\"email\":\"%s\",\"password\":\"%s\"}",testUser.getEmail(),password);
		this.mockMvc.perform(
				post("/api/v1/user/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestBody))
				.andExpect(status().isOk());
	}




}
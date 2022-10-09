package com._247ffa.qe.runner.action;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com._247ffa.qe.runner.model.Server;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

public class Validator extends ActionDecorator {

	protected final Action conditionalAction;
	protected final URI validationEndpoint;
	protected final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .connectTimeout(Duration.ofSeconds(30))
            .build();
	
	public Validator(Action action, String validationEndpoint, String command, Action conditionalAction) {
		super(action, command);
		this.conditionalAction = conditionalAction;
		this.validationEndpoint = URI.create(validationEndpoint);
	}
	
	protected boolean validate() throws IOException, InterruptedException {
		HttpRequest req = HttpRequest.newBuilder().uri(validationEndpoint).GET().build();
		HttpClient client = HttpClient.newHttpClient();
		HttpResponse<String> response = client.send(req, BodyHandlers.ofString());
		System.out.println(LocalDateTime.now().toString() + " - Validating: " + response);
		ObjectMapper om = JsonMapper.builder().findAndAddModules().build();
		// todo: more efficient to put server id in path instead of getting all...
		Server[] servers = om.readValue(response.body(), Server[].class); 
		List<Server> filteredList = Arrays.asList(servers)
			.stream()
			.filter(server -> {return server.getId().equals(command);})
			.filter(server -> {return server.getSession().getValue().getCurrentPlayers() >= 1;})
			.collect(Collectors.toList());
		return !filteredList.isEmpty();
	}

	@Override
	public void perform() {
		super.perform();
		System.out.println(LocalDateTime.now().toString() + " - Validating: " + command + " via " + validationEndpoint);
		try {
			// poll twice two minutes apart
			boolean up1 = validate();
			Thread.sleep(120000);
			boolean up2 = validate();
			
			if(!up1 && !up2) {
				System.out.println(LocalDateTime.now().toString() + " - Validating: Server " + command + " down, restarting");
				conditionalAction.perform();
			} else {
				System.out.println(LocalDateTime.now().toString() + " - Validating: Server " + command + " up");
			}
		} catch (IOException | InterruptedException e) {
			System.err.println(LocalDateTime.now().toString() + " - Couldn't validate. Exception " + e);
		}
	}
}

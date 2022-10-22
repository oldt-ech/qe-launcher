package com._247ffa.qe.runner;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com._247ffa.qe.runner.model.ServerOnlineInfoReport;
import com._247ffa.qe.runner.model.ServerOnlineInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

public class ServerOnlineChecker {

	protected final URI validationEndpoint;
	protected final HttpClient httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1)
			.connectTimeout(Duration.ofSeconds(30)).build();

	public ServerOnlineChecker(String validationEndpoint) {
		this.validationEndpoint = URI.create(validationEndpoint);
	}

	protected List<ServerOnlineInfo> getStatuses() throws IOException, InterruptedException {
		HttpRequest req = HttpRequest.newBuilder().uri(validationEndpoint).GET().build();
		HttpClient client = HttpClient.newHttpClient();
		HttpResponse<String> response = client.send(req, BodyHandlers.ofString());
		System.out.println(LocalDateTime.now().toString() + " - Checking online status: " + response);
		ObjectMapper om = JsonMapper.builder().findAndAddModules().build();
		ServerOnlineInfoReport report = om.readValue(response.body(), ServerOnlineInfoReport.class);
		System.out.println(LocalDateTime.now().toString() + " - Uptime report received: " + report.getDescription());
		return report.getItems();
	}

	protected boolean check() {
		List<ServerOnlineInfo> statuses = new ArrayList<>();
		;

		try {
			statuses = getStatuses();
		} catch (IOException | InterruptedException e) {
			System.err.println(LocalDateTime.now().toString() + " - Error checking online status via "
					+ validationEndpoint + ". Defaulting true. " + e);
			e.printStackTrace();
			return true;
		}

		for (ServerOnlineInfo status : statuses) {
			if (status.getServersOnline() == 1) {
				return true;
			}
		}

		System.out.println(LocalDateTime.now().toString() + " - Server offline");
		return false;
	}
}

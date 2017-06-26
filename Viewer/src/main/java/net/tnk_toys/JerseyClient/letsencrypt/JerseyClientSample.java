package net.tnk_toys.JerseyClient.letsencrypt;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;

public class JerseyClientSample {

	public static void main(String[] args) {
		ClientConfig configuration = new ClientConfig()
		.property(ClientProperties.CONNECT_TIMEOUT, 5000)
		.property(ClientProperties.CONNECT_TIMEOUT, 10000)
		.property(ClientProperties.USE_ENCODING, "UTF-8");
		Response response = ClientBuilder.newClient(configuration)
		.target("http://localhost:8080")
		.path("/base/command")
		.queryParam("quary", "test")
		.request(MediaType.TEXT_PLAIN)
		.accept(MediaType.TEXT_PLAIN)
		.get();
		System.out.println(response.readEntity(String.class));
	}

}

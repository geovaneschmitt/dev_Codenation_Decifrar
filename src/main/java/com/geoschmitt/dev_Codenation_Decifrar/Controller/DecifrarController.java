package com.geoschmitt.dev_Codenation_Decifrar.Controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.geoschmitt.dev_Codenation_Decifrar.Component.JsonComponent;
import com.google.gson.Gson;

@RestController
public class DecifrarController {

	public static void answer() throws IOException {

		URL url = null;
		url = new URL("https://api.codenation.dev/v1/challenge/dev-ps/generate-data?token=0ab861333a792a64e97b7fac4f4d7ce61aca481f");

		BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
		BufferedWriter out = new BufferedWriter(new FileWriter("answer.json"));
		String inputLine;
		while ((inputLine = in.readLine()) != null) {
			out.write(inputLine);
			out.newLine();
		}
		in.close();
		out.flush();
		out.close();

		Gson gson = new Gson();

		BufferedReader br = new BufferedReader(new FileReader("./answer.json"));

		JsonComponent json = gson.fromJson(br, JsonComponent.class);
		String cifrada = json.getCifrado();
		String decifrada = "";
		int casas = json.getNumero_casas();
		char word;
		for (int x = 0; x < cifrada.length(); x++) {
			int position = cifrada.charAt(x);
			if(position == 44){
				word = ',';
			} else if (position == 32) {
				word = ' ';
			} else if (position == 46) {
				word = '.';
			} else {
				position = position - casas;
				if (position < 97) {
					word = (char) (122 - (97 - position - 1));
				} else {
					word = (char) (position);
				}
			}
			decifrada = decifrada + word;
		}

		String resumo_criptografico = encrypt(decifrada);
		json.setResumo_criptografico(resumo_criptografico);
		json.setDecifrado(decifrada);
		String s = gson.toJson(json);
		FileWriter writer = new FileWriter("./answer.json");
		writer.write(s);
		writer.close();
		System.out.println(decifrada);

	}

	private static String encrypt(String decifrada) {
		String sha1 = "";
		try {
			MessageDigest crypt = MessageDigest.getInstance("SHA-1");
			crypt.reset();
			crypt.update(decifrada.getBytes("UTF-8"));
			sha1 = byteToHex(crypt.digest());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return sha1;
	}

	private static String byteToHex(final byte[] hash) {
		Formatter formatter = new Formatter();
		for (byte b : hash) {
			formatter.format("%02x", b);
		}
		String result = formatter.toString();
		formatter.close();
		return result;
	}

	@PostMapping(path = "/geoschmitt")
	public HttpResponse codenation() throws ClientProtocolException, IOException, URISyntaxException {

		answer();

		URI url;
		url = new URI("https://api.codenation.dev/v1/challenge/dev-ps/submit-solution?token=4953808e9d36e89a6a051267e802397f9b647291");

		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);
		
		File f = new File("./answer.json");
		
		FileBody uploadFilePart = new FileBody(f);
		MultipartEntity reqEntity = new MultipartEntity();
		reqEntity.addPart("answer", uploadFilePart);
		httpPost.setEntity(reqEntity);
		httpPost.setHeader("Content-Type", "multipart/form-data");

		HttpResponse response = httpclient.execute(httpPost);
		return response;
	}

	@RequestMapping("/")
	public String home() {
		return "GeoSchmitt";
	}
	

}

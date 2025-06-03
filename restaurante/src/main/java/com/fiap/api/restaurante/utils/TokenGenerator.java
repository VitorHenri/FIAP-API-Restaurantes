package com.fiap.api.restaurante.utils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fiap.api.restaurante.entities.Usuario;


@Service
public class TokenGenerator {

	@Value("${restaurante.api.secret}")
	private String secretAPI;

	public String generateToken(Usuario user) {
		Algorithm algorithm = Algorithm.HMAC256(secretAPI);

		Date dataExpiracao = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(dataExpiracao);
		cal.add(Calendar.DATE, 1);
		dataExpiracao = cal.getTime();

		return JWT.create().withIssuer("RESTAURANTE").withSubject(user.getLogin())
				.withIssuedAt(LocalDateTime.now().toInstant(ZoneOffset.UTC)).withExpiresAt(dataExpiracao)
				.sign(algorithm);

	}
	
	public String getLogin(String token) {
		Algorithm algorithm = Algorithm.HMAC256(secretAPI);
		return JWT.require(algorithm)
				.withIssuer("RESTAURANTE")
				.build()
				.verify(token)
				.getSubject();
	}

}

package efrei.m1.aiws.service;

import com.auth0.jwt.algorithms.Algorithm;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthenticationService {
	@Setter
	private static Algorithm jwtAlgorithm;

	@Setter
	private static String jwtIssuer;
}

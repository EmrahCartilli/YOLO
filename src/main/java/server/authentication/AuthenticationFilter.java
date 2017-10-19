
package server.authentication;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;



import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

import java.io.UnsupportedEncodingException;

import java.util.Date;

/**
 * Class responsible for ???
 */
@Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {
    ContainerRequestContext localContainerRequestContext;
    String httpMethodType;
    AuthEndpoint authEndpoint = new AuthEndpoint();
    private static final String AUTHENTICATION_SCHEME = "Bearer";

    /**
     * Only allows tokens that are authenticated to pass
     * @param containerRequestContext
     * @throws IOException
     */
    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        String authHeader = containerRequestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        localContainerRequestContext = containerRequestContext;
        String token = authHeader.substring(AUTHENTICATION_SCHEME.length()).trim();

        httpMethodType = containerRequestContext.getMethod().toString();

        try {
            if (checkToken(token) == true) {

            } else {
                containerRequestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Method that checks if the token is valid by verifying the token with the JWT library and if it is expired
     * @param token
     * @return
     * @throws Exception
     */
    private boolean checkToken(String token) throws Exception {

        boolean isValidToken = true;
        try {

            //???
            Algorithm algorithm = Algorithm.HMAC256("secret");
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("YOLO").build();

            DecodedJWT jwt = verifier.verify(token);

            //abort if true
            if (jwt.getExpiresAt().before(new Date(System.currentTimeMillis() * 1000))) {
                localContainerRequestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
            }

        } catch (UnsupportedEncodingException e) {
            isValidToken = false;
        } catch (JWTVerificationException e) {
            isValidToken = false;
        }

        //else return valid
        return isValidToken;
    }
}


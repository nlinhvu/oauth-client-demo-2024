package vn.cloud.oauth_client_demo;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class PKCETests {

    /**
     * code_verifier
     * code_challenge = code_challenge_method(code_verifier)
     *
     * ||||||||||         code_challenge           |||||||||||||||||
     * |        |      code_challenge_method       |               |
     * |        |  -----Authorization URI------->  |               | received_code_challenge, received_code_challenge_method
     * | Client |                                  | Authorization |
     * |        |                                  |    Server     |
     * |        |       code_verifier              |               |
     * ||||||||||  -------Token URI------------->  ||||||||||||||||| received_code_verifier
     *
     *                                             new_code_challenge = received_code_challenge_method(received_code_verifier)
     *                                             Compare: new_code_challenge == received_code_challenge?
     */
    @Test
    void createCodeChallenge() throws NoSuchAlgorithmException {
        String codeVerifier = "abc";
        String codeChallengeMethod = "SHA-256";

        MessageDigest md = MessageDigest.getInstance(codeChallengeMethod);
        byte[] digest = md.digest(codeVerifier.getBytes(StandardCharsets.US_ASCII));
        String codeChallenge = Base64.getUrlEncoder().withoutPadding().encodeToString(digest);

        System.out.println(codeChallenge);
    }
}

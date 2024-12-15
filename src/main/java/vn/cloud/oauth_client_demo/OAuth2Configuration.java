package vn.cloud.oauth_client_demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.endpoint.RestClientAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.util.LinkedMultiValueMap;

import java.util.function.Consumer;

import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter.DEFAULT_AUTHORIZATION_REQUEST_BASE_URI;

@Configuration
public class OAuth2Configuration {

    private final ClientRegistrationRepository clientRegistrationRepository;

    public OAuth2Configuration(ClientRegistrationRepository clientRegistrationRepository) {
        this.clientRegistrationRepository = clientRegistrationRepository;
    }

    @Bean
    SecurityFilterChain oauth2SecurityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((requests) -> requests.anyRequest().authenticated());
        http.oauth2Login(oauth2 -> oauth2
                .authorizationEndpoint(endpoint -> endpoint
                        .authorizationRequestResolver(authorizationRequestResolver(clientRegistrationRepository))
                )
        );
        http.oauth2Client(withDefaults());
        return http.build();
    }

    private Consumer<OAuth2AuthorizationRequest.Builder> authorizationRequestCustomizer() {
        return customizer -> customizer
                .additionalParameters(params -> params.put("realm", "invoice-realm"));
    }

    private OAuth2AuthorizationRequestResolver authorizationRequestResolver(
            ClientRegistrationRepository clientRegistrationRepository) {

        DefaultOAuth2AuthorizationRequestResolver authorizationRequestResolver =
                new DefaultOAuth2AuthorizationRequestResolver(clientRegistrationRepository, DEFAULT_AUTHORIZATION_REQUEST_BASE_URI);
        authorizationRequestResolver.setAuthorizationRequestCustomizer(authorizationRequestCustomizer());

        return authorizationRequestResolver;
    }

    @Bean
    public OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> accessTokenResponseClient() {
        RestClientAuthorizationCodeTokenResponseClient tokenResponseClient = new RestClientAuthorizationCodeTokenResponseClient();

        tokenResponseClient.addParametersConverter(codeGrantRequest -> {
            LinkedMultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
            parameters.set("realm", "invoice-realm");
            return parameters;
        });

        tokenResponseClient.addHeadersConverter(codeGrantRequest -> {
            HttpHeaders headers = new HttpHeaders();
            headers.set(HttpHeaders.USER_AGENT, "my-user-agent");
            return headers;
        });

        return tokenResponseClient;
    }
}

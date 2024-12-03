package vn.cloud.oauth_client_demo;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.gateway.server.mvc.handler.ProxyExchange;
import org.springframework.cloud.gateway.server.mvc.handler.RestClientProxyExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;
import static org.springframework.cloud.gateway.server.mvc.predicate.GatewayRequestPredicates.path;


@SpringBootApplication
@RestController
public class OauthClientDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(OauthClientDemoApplication.class, args);
    }

    static final String UI_PREFIX = "/";
    static final String UI_HOST = "http://localhost:3200";


    static final String API_PREFIX = "/api/";
    static final String API_HOST = "http://localhost:8080";

    static final String WILDCARD = "**";

//	@Bean
//	SecurityFilterChain mySecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
//		return httpSecurity
//				.authorizeHttpRequests(a -> a.anyRequest().authenticated())
//				.csrf(AbstractHttpConfigurer::disable)
//				.cors(AbstractHttpConfigurer::disable)
//				.oauth2Login(Customizer.withDefaults())
//				.oauth2Client(Customizer.withDefaults())
//				.build();
//	}

//	@Bean
//	public RouteLocator customRouteLocator1(RouteLocatorBuilder builder) {
//		return builder.routes()
//				.route("react_route1", r -> r
//						.path("/get")
//						.uri("https://httpbin.org"))
//				.build();
//	}
//
//	@Bean
//	public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
//		return builder.routes()
//				.route("react_route", r -> r
//						.path("/**")
//						.uri("http://localhost:3200"))
//				.build();
//	}


//	@Bean
//	public RouterFunction<ServerResponse> getRoute() {
//		return org.springframework.web.servlet.function.RouterFunctions.route().GET("/get1", http("https://httpbin.org")).build();
//	}
//
//
//	@Bean
//	public RouterFunction<ServerResponse> gatewayRouterFunctionsPath1() {
//		return route("path_route1")
//				.route(path("/get"), http("https://httpbin.org"))
//				.build();
//	}

//	@Bean
//	public RouterFunction<ServerResponse> gatewayRouterFunctionsPath() {
//		return route("path_route")
//				.route(path("/**"), http("http://localhost:3200"))
//				.build();
//	}

//	@Bean
//	public RouterFunction<ServerResponse> customRouteLocator() {
//		return RouterFunctions.route()
//				.route(path("/**"), request -> ServerResponse.ok().(
//						URI.create("http://localhost:3200" + request.path())).build())
//				.build();
//	}


//    @GetMapping("/**")
//    ResponseEntity<?> ui(ProxyExchange<?> request) {
//        var path = request.path("/");
//        return request
//                .uri(URI.create("http://localhost:3200" + "/" + path))
//                .get();
//    }

//    @Bean
//    ApplicationRunner runner(RestClient restClient) {
//        return args -> {
//            try {
//
//                String body = restClient.get().uri("http://localhost:3200").retrieve().body(String.class);
//
//
//                System.out.println("Response body: " + body);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        };
//    }

    @Bean
    RestClient restClient(RestClient.Builder builder) {
        SimpleClientHttpRequestFactory simpleClientHttpRequestFactory = new SimpleClientHttpRequestFactory();
        simpleClientHttpRequestFactory.setConnectTimeout(Duration.ofSeconds(1L));
                simpleClientHttpRequestFactory.setReadTimeout(Duration.ofSeconds(5L));

                return builder
                .requestFactory(simpleClientHttpRequestFactory)
                .build();

    }

    @Bean
    public RestClientProxyExchange restClientProxyExchange(RestClient restClient) {
        return new RestClientProxyExchange(restClient);
    }
}

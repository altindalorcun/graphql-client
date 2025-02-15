package dev.altindal.graphql_client.service;

import dev.altindal.graphql_client.dto.UserCreateRequest;
import dev.altindal.graphql_client.dto.UserResponse;
import dev.altindal.graphql_client.dto.UserUpdateRequest;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.graphql.client.WebSocketGraphQlClient;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import org.springframework.web.reactive.socket.client.WebSocketClient;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class UserGraphqlRequester {

    private final HttpGraphQlClient graphQlClient;
    private final WebSocketGraphQlClient graphQlWebSocketClient;

    public UserGraphqlRequester() {
        WebClient client = WebClient.builder()
                .baseUrl("http://localhost:8080/graphql")
                .build();
        graphQlClient = HttpGraphQlClient.builder(client).build();

        WebSocketClient webSocketClient = new ReactorNettyWebSocketClient();
        graphQlWebSocketClient = WebSocketGraphQlClient
                .builder("ws://localhost:8080/graphql", webSocketClient)
                .keepAlive(Duration.ofSeconds(30))
                .build();
    }

    public List<UserResponse> getAllUsers() {
        //language=graphql
        String query = """
                query {
                    getAllUsers {
                        id
                        createdAt
                        updatedAt
                        username
                        firstName
                        lastName
                        mail
                        role
                    }
                }
                """;

        return graphQlClient.document(query)
                .retrieve("getAllUsers")
                .toEntityList(UserResponse.class)
                .block();
    }

    public UserResponse getUserById(UUID id) {

        Map<String, Object> variables = new HashMap<>();
        variables.put("id", id);

        //language=graphql
        String query = """
                query getUserById($id : UUID!){
                  getUserById(id : $id) {
                        id
                        createdAt
                        updatedAt
                        username
                        firstName
                        lastName
                        mail
                        role
                  }
                }
                """;

        return graphQlClient.document(query)
                .variables(variables)
                .retrieve("getUserById")
                .toEntity(UserResponse.class)
                .block();
    }

    public UserResponse createUser(UserCreateRequest request) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("username", request.username());
        variables.put("password", request.password());
        variables.put("firstName", request.firstName());
        variables.put("lastName", request.lastName());
        variables.put("mail", request.mail());
        variables.put("role", request.role());
        Map<String, Object> input = new HashMap<>();
        input.put("input", variables);

        //language=graphql
        String mutation = """
                mutation($input : UserCreateRequest!) {
                    createUser(userCreateRequest: $input) {
                        id
                        createdAt
                        updatedAt
                        username
                        firstName
                        lastName
                        mail
                        role
                    }
                }
                """;

        return graphQlClient.document(mutation)
                .variables(input)
                .retrieve("createUser")
                .toEntity(UserResponse.class)
                .block();
    }

    public UserResponse updateUser(UserUpdateRequest request) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("id", request.id());
        variables.put("password", request.password());
        variables.put("firstName", request.firstName());
        variables.put("lastName", request.lastName());
        variables.put("mail", request.mail());
        variables.put("role", request.role());
        Map<String, Object> input = new HashMap<>();
        input.put("input", variables);

        //language=graphql
        String mutation = """
                mutation($input : UserUpdateRequest!) {
                    updateUser(userUpdateRequest: $input) {
                        id
                        createdAt
                        updatedAt
                        username
                        firstName
                        lastName
                        mail
                        role
                    }
                }
                """;

        return graphQlClient.document(mutation)
                .variables(input)
                .retrieve("updateUser")
                .toEntity(UserResponse.class)
                .block();
    }

    public Boolean deleteUser(UUID id) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("id", id);

        //language=graphql
        String mutation = """
                mutation ($id : UUID!) {
                    deleteUser(id : $id)
                }
                """;

        return graphQlClient.document(mutation)
                .variables(variables)
                .retrieve("deleteUser")
                .toEntity(Boolean.class)
                .block();
    }

    @PostConstruct
    public void listenSubscription() {
        //language=graphql
        String subscription = """
                subscription {
                    userStatusChanged {
                        id
                        createdAt
                        updatedAt
                        username
                        firstName
                        lastName
                        mail
                        role
                    }
                }
                """;

        graphQlWebSocketClient.document(subscription)
                .retrieveSubscription("userStatusChanged")
                .toEntity(UserResponse.class)
                .subscribe(userResponse -> log.info("User changed -> " + userResponse.toString()),
                        error -> log.info("Error : " + error),
                        () -> log.info("Subscription completed")
                );
    }
}

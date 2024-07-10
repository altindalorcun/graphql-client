package dev.altindal.graphql_client.service;

import dev.altindal.graphql_client.dto.UserResponse;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class UserService {

    private final HttpGraphQlClient graphQlClient;

    public UserService() {
        WebClient client = WebClient.builder()
                .baseUrl("http://localhost:8080/graphql")
                .build();
        graphQlClient = HttpGraphQlClient.builder(client).build();
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
}

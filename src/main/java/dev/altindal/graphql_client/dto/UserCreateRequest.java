package dev.altindal.graphql_client.dto;

public record UserCreateRequest(
        String username,
        String password,
        String firstName,
        String lastName,
        String mail,
        String role
) {
}

package dev.altindal.graphql_client.dto;

import java.util.UUID;

public record UserUpdateRequest(
        UUID id,
        String username,
        String password,
        String firstName,
        String lastName,
        String mail,
        String role
) {
}

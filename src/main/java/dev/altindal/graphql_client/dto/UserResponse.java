package dev.altindal.graphql_client.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record UserResponse(
        UUID id,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt,
        String username,
        String firstName,
        String lastName,
        String mail,
        Role role
) {
}

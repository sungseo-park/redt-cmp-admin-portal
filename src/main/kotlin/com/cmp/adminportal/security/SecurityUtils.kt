package com.cmp.adminportal.security

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser

import java.util.Optional

/**
 * Get the login of the current user.
 *
 * @return the login of the current user.
 */
fun getCurrentUserLogin(): Optional<String> {
    val securityContext = SecurityContextHolder.getContext()
    return Optional.ofNullable(securityContext.authentication)
        .map { authentication ->
            when (val principal = authentication.principal) {
                is UserDetails -> principal.username
                is DefaultOidcUser -> {
                    if (principal.attributes.containsKey("preferred_username")) {
                        principal.attributes["preferred_username"].toString()
                    } else {
                        null
                    }
                }
                is String -> principal
                else -> null
            }
        }
}

/**
 * Check if a user is authenticated.
 *
 * @return true if the user is authenticated, false otherwise.
 */
fun isAuthenticated(): Boolean {
    val securityContext = SecurityContextHolder.getContext()
    return Optional.ofNullable(securityContext.authentication)
        .map { authentication ->
            authentication.authorities.none { it.authority == ANONYMOUS }
        }
        .orElse(false)
}

/**
 * If the current user has a specific authority (security role).
 *
 * The name of this method comes from the `isUserInRole()` method in the Servlet API
 *
 * @param authority the authority to check.
 * @return true if the current user has the authority, false otherwise.
 */
fun isCurrentUserInRole(authority: String): Boolean {
    val securityContext = SecurityContextHolder.getContext()
    return Optional.ofNullable(securityContext.authentication)
        .map { authentication ->
            authentication.authorities.any { it.authority == authority }
        }
        .orElse(false)
}

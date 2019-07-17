package com.cmp.adminportal.service.mapper

import com.cmp.adminportal.domain.Authority
import com.cmp.adminportal.domain.User
import com.cmp.adminportal.service.dto.UserDTO

import org.springframework.stereotype.Service

/**
 * Mapper for the entity [User] and its DTO called [UserDTO].
 *
 * Normal mappers are generated using MapStruct, this one is hand-coded as MapStruct
 * support is still in beta, and requires a manual step with an IDE.
 */
@Service
class UserMapper {

    fun usersToUserDTOs(users: List<User?>): MutableList<UserDTO> {
        return users.asSequence()
            .filterNotNull()
            .mapTo(mutableListOf()) { this.userToUserDTO(it) }
    }

    fun userToUserDTO(user: User): UserDTO {
        return UserDTO(user)
    }

    fun userDTOsToUsers(userDTOs: List<UserDTO?>): MutableList<User> {
        return userDTOs.asSequence()
            .map { userDTOToUser(it) }
            .filterNotNullTo(mutableListOf())
    }

    fun userDTOToUser(userDTO: UserDTO?): User? {
        return when (userDTO) {
            null -> null
            else -> {
                User(
                    id = userDTO.id,
                    login = userDTO.login,
                    firstName = userDTO.firstName,
                    lastName = userDTO.lastName,
                    email = userDTO.email,
                    imageUrl = userDTO.imageUrl,
                    activated = userDTO.activated,
                    langKey = userDTO.langKey,
                    authorities = authoritiesFromStrings(userDTO.authorities)
                )
            }
        }
    }

    private fun authoritiesFromStrings(authoritiesAsString: Set<String>?): MutableSet<Authority> {
        return authoritiesAsString?.mapTo(mutableSetOf()) { Authority(name = it) } ?: mutableSetOf()
    }

    fun userFromId(id: String?): User? {
        return id?.let { User(id = it) }
    }
}

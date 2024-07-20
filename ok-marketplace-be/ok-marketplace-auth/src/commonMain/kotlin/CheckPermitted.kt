package ru.otus.otuskotlin.marketplace.auth

import ru.otus.otuskotlin.marketplace.common.models.MkplCommand
import ru.otus.otuskotlin.marketplace.common.permissions.MkplPrincipalRelations
import ru.otus.otuskotlin.marketplace.common.permissions.MkplUserPermissions

/**
 * Вычисляет доступность выполнения операции.
 * Здесь происходит сравнение доступных прав (пермишинов) и фактических отношений принципала к объекту, с которым работаем
 */
fun checkPermitted(
    command: MkplCommand,
    relations: Iterable<MkplPrincipalRelations>,
    permissions: Iterable<MkplUserPermissions>,
) =
    relations.asSequence().flatMap { relation ->
        permissions.map { permission ->
            AccessTableConditions(
                command = command,
                permission = permission,
                relation = relation,
            )
        }
    }.any {
        accessTable[it] != null
    }
    // Дополнительно можно сделать проверку на отсутствие в результатах false

private data class AccessTableConditions(
    val command: MkplCommand,
    val permission: MkplUserPermissions,
    val relation: MkplPrincipalRelations
)

private val accessTable = mapOf(
    // Create
    AccessTableConditions(
        command = MkplCommand.CREATE,
        permission = MkplUserPermissions.CREATE_OWN,
        relation = MkplPrincipalRelations.NEW,
    ) to true,

    // Read
    AccessTableConditions(
        command = MkplCommand.READ,
        permission = MkplUserPermissions.READ_OWN,
        relation = MkplPrincipalRelations.OWN,
    ) to true,
    AccessTableConditions(
        command = MkplCommand.READ,
        permission = MkplUserPermissions.READ_PUBLIC,
        relation = MkplPrincipalRelations.PUBLIC,
    ) to true,

    // Update
    AccessTableConditions(
        command = MkplCommand.UPDATE,
        permission = MkplUserPermissions.UPDATE_OWN,
        relation = MkplPrincipalRelations.OWN,
    ) to true,

    // Delete
    AccessTableConditions(
        command = MkplCommand.DELETE,
        permission = MkplUserPermissions.DELETE_OWN,
        relation = MkplPrincipalRelations.OWN,
    ) to true,

    // Offers
    AccessTableConditions(
        command = MkplCommand.OFFERS,
        permission = MkplUserPermissions.OFFER_FOR_OWN,
        relation = MkplPrincipalRelations.OWN,
    ) to true,
)

package ru.otus.otuskotlin.marketplace.app.common

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import ru.otus.otuskotlin.marketplace.common.models.MkplUserId
import ru.otus.otuskotlin.marketplace.common.permissions.MkplPrincipalModel
import ru.otus.otuskotlin.marketplace.common.permissions.MkplUserGroups
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

const val AUTH_HEADER: String = "x-jwt-payload"

@OptIn(ExperimentalEncodingApi::class)
fun String?.jwt2principal(): MkplPrincipalModel = this?.let { jwtHeader ->
        val jwtJson = Base64.decode(jwtHeader).decodeToString()
        println("JWT JSON PAYLOAD: $jwtJson")
        val jwtObj = jsMapper.decodeFromString(JwtPayload.serializer(), jwtJson)
        jwtObj.toPrincipal()
    }
    ?: run {
        println("No jwt found in headers")
        MkplPrincipalModel.NONE
    }

@OptIn(ExperimentalEncodingApi::class)
fun MkplPrincipalModel.createJwtTestHeader(): String {
    val jwtObj = fromPrincipal()
    val jwtJson = jsMapper.encodeToString(JwtPayload.serializer(), jwtObj)
    return Base64.encode(jwtJson.encodeToByteArray())
}

private val jsMapper = Json {
    ignoreUnknownKeys = true
}

@Serializable
private data class JwtPayload(
    val aud: List<String>? = null,
    val sub: String? = null,
    @SerialName("family_name")
    val familyName: String? = null,
    @SerialName("given_name")
    val givenName: String? = null,
    @SerialName("middle_name")
    val middleName: String? = null,
    val groups: List<String>? = null,
)

private fun JwtPayload.toPrincipal(): MkplPrincipalModel = MkplPrincipalModel(
    id = sub?.let { MkplUserId(it) } ?: MkplUserId.NONE,
    fname = givenName ?: "",
    mname = middleName ?: "",
    lname = familyName ?: "",
    groups = groups?.mapNotNull { it.toPrincipalGroup() }?.toSet() ?: emptySet(),
)

private fun MkplPrincipalModel.fromPrincipal(): JwtPayload = JwtPayload(
    sub = id.takeIf { it != MkplUserId.NONE }?.asString(),
    givenName = fname.takeIf { it.isNotBlank() },
    middleName = mname.takeIf { it.isNotBlank() },
    familyName = lname.takeIf { it.isNotBlank() },
    groups = groups.mapNotNull { it.fromPrincipalGroup() }.toList().takeIf { it.isNotEmpty() } ?: emptyList(),
)

private fun String?.toPrincipalGroup(): MkplUserGroups? = when (this?.uppercase()) {
    "USER" -> MkplUserGroups.USER
    "ADMIN_AD" -> MkplUserGroups.ADMIN_AD
    "MODERATOR_MP" -> MkplUserGroups.MODERATOR_MP
    "TEST" -> MkplUserGroups.TEST
    "BAN_AD" -> MkplUserGroups.BAN_AD
    // TODO сделать обработку ошибок
    else -> null
}

private fun MkplUserGroups?.fromPrincipalGroup(): String? = when (this) {
    MkplUserGroups.USER -> "USER"
    MkplUserGroups.ADMIN_AD -> "ADMIN_AD"
    MkplUserGroups.MODERATOR_MP -> "MODERATOR_MP"
    MkplUserGroups.TEST -> "TEST"
    MkplUserGroups.BAN_AD -> "BAN_AD"
    // TODO сделать обработку ошибок
    else -> null
}

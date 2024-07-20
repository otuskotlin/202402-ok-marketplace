package ru.otus.otuskotlin.marketplace.common

import kotlinx.datetime.Instant
import ru.otus.otuskotlin.marketplace.common.models.*
import ru.otus.otuskotlin.marketplace.common.permissions.MkplPrincipalModel
import ru.otus.otuskotlin.marketplace.common.permissions.MkplUserPermissions
import ru.otus.otuskotlin.marketplace.common.repo.IRepoAd
import ru.otus.otuskotlin.marketplace.common.stubs.MkplStubs
import ru.otus.otuskotlin.marketplace.common.ws.IMkplWsSession
import ru.otus.otuskotlin.marketplace.states.common.models.MkplStateRq

data class MkplContext(
    var command: MkplCommand = MkplCommand.NONE,
    var state: MkplState = MkplState.NONE,
    val errors: MutableList<MkplError> = mutableListOf(),

    var corSettings: MkplCorSettings = MkplCorSettings(),
    var workMode: MkplWorkMode = MkplWorkMode.PROD,
    var stubCase: MkplStubs = MkplStubs.NONE,
    var wsSession: IMkplWsSession = IMkplWsSession.NONE,

    var principal: MkplPrincipalModel = MkplPrincipalModel.NONE,
    val permissionsChain: MutableSet<MkplUserPermissions> = mutableSetOf(),
    var permitted: Boolean = false,

    var requestId: MkplRequestId = MkplRequestId.NONE,
    var timeStart: Instant = Instant.NONE,
    var adRequest: MkplAd = MkplAd(),
    var adFilterRequest: MkplAdFilter = MkplAdFilter(),

    var adValidating: MkplAd = MkplAd(),
    var adFilterValidating: MkplAdFilter = MkplAdFilter(),

    var adValidated: MkplAd = MkplAd(),
    var adFilterValidated: MkplAdFilter = MkplAdFilter(),

    var adRepo: IRepoAd = IRepoAd.NONE,
    var adRepoRead: MkplAd = MkplAd(), // То, что прочитали из репозитория
    var adRepoPrepare: MkplAd = MkplAd(), // То, что готовим для сохранения в БД
    var adRepoDone: MkplAd = MkplAd(),  // Результат, полученный из БД
    var adsRepoDone: MutableList<MkplAd> = mutableListOf(),

    // Запрашиваем статус из модуля статистики
    var adState: MkplStateRq = MkplStateRq(),

    var adResponse: MkplAd = MkplAd(),
    var adsResponse: MutableList<MkplAd> = mutableListOf(),
)

package ru.otus.otuskotlin.marketplace.biz.exceptions

import ru.otus.otuskotlin.marketplace.common.models.MkplWorkMode

class MkplAdDbNotConfiguredException(val workMode: MkplWorkMode): Exception(
    "Database is not configured properly for workmode $workMode"
)

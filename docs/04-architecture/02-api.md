# API

## Описание сущности ad (объявление)

1. Info
    1. Title
    2. Description
    3. Owner
    4. Visibility
2. DealSide: Demand/Proposal
3. ProductType (гаечный ключ, ...)
4. ProductId - идентификатор модели товара

## Функции (эндпониты)

1. CRUDS (create, read, update, delete, search) для объявлений (ad)
2. ad.offers (опционально): возвращает список объявлений, подходящих для выбранного (предложений для запроса или
   запросов для предложения)


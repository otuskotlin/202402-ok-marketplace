--liquibase formatted sql

--changeset sokatov:1 labels:v0.0.1
CREATE TYPE "AD_TYPES" AS ENUM ('demand', 'supply');
CREATE TYPE "VISIBILITY" AS ENUM ('public', 'owner', 'group');

CREATE TABLE "ads" (
	"id" text primary key constraint ads_id_length_ctr check (length("id") < 64),
	"title" text constraint ads_title_length_ctr check (length(title) < 128),
	"description" text constraint ads_description_length_ctr check (length(title) < 4096),
	"ad_type" "AD_TYPES" not null,
	"visibility" "VISIBILITY" not null,
	"owner_id" text not null constraint ads_owner_id_length_ctr check (length(id) < 64),
	"product_id" text constraint ads_product_id_length_ctr check (length(id) < 64),
	"lock" text not null constraint ads_lock_length_ctr check (length(id) < 64)
);

CREATE INDEX ads_owner_id_idx on "ads" using hash ("owner_id");

CREATE INDEX ads_product_id_idx on "ads" using hash ("product_id");

CREATE INDEX ads_ad_type_idx on "ads" using hash ("ad_type");

CREATE INDEX ads_visibility_idx on "ads" using hash ("visibility");

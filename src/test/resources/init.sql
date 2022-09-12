ALTER USER "postgres" WITH PASSWORD 'password';


create table decoded_transaction (
    transaction_id bytea primary key,
    op_uid uuid not null, -- ip_uid from digest (contract service)
    block_id bigint not null, -- block number
    index_value smallint not null, -- transaction index in block
    recipient bytea,
    origin bytea, -- tx from
    nonce bigint not null,
    signature_value bytea not null,
    message_uuid uuid not null,
    payload text,
    processed timestamp
);

create table state_certificate
(
    certificate_id /*text*/ varchar(100) not null primary key,
    member_id text not null,
    wallet_id text not null,
    creator_id text,
    created timestamp not null,
    decoded_transaction_id bytea not null references decoded_transaction
);


create table certificate_decode
(
    certificate_id /*text*/ varchar(100) not null primary key references state_certificate,
    cert_begin_date timestamp not null,
    cert_expired_date timestamp not null,
    key_begin_date timestamp not null,
    key_expired_date timestamp not null,
    cert_status text not null,
    org_o text not null,
    name_cn text not null,
    department_ou text not null,
    oid_value text not null,
    dn_cn_cert text not null
);
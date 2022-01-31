--liquibase formatted sql
--changeset vbabxy:100000001
--



create table kycs
(
    id bigserial
        constraint kycs_pkey
            primary key,
    created_at timestamp not null,
    updated_at timestamp not null,
    level_name varchar(255),
    transaction_limit numeric(19,2)
);


create table transactions
(
    id bigserial
        constraint transactions_pkey
            primary key,
    created_at timestamp not null,
    updated_at timestamp not null,
    amount numeric(19,2) not null,
    transaction_reference varchar(255) not null,
    transaction_type varchar(255) not null
);


create table users
(
    id bigserial
        constraint users_pkey
            primary key,
    created_at timestamp not null,
    updated_at timestamp not null,
    bvn varchar(255),
    email_address varchar(255) not null
        constraint uk_1ar956vx8jufbghpyi09yr16l
            unique,
    first_name varchar(255) not null,
    house_address varchar(255),
    identity_doc_url varchar(255),
    last_name varchar(255) not null,
    phone_number varchar(255) not null
        constraint uk_9q63snka3mdh91as4io72espi
            unique,
    kyc_id bigint not null
        constraint fk54ra16v3da38j5fs2lubwe9l6
            references kycs
            on delete cascade,
    is_email_confirmed boolean default false
);

create index idxh4egir6vsp3jm51n5yt70inqn
    on users (id, email_address);

create table wallets
(
    id bigserial
        constraint wallets_pkey
            primary key,
    created_at timestamp not null,
    updated_at timestamp not null,
    balance numeric(19,2) not null,
    status varchar(255) not null,
    wallet_account_number varchar(255) not null,
    user_id bigint not null
        constraint fkc1foyisidw7wqqrkamafuwn4e
            references users
            on delete cascade
);


create table transaction_details
(
    id bigserial
        constraint transaction_details_pkey
            primary key,
    created_at timestamp not null,
    updated_at timestamp not null,
    final_balance numeric(19,2),
    starting_balance numeric(19,2),
    transaction_class varchar(255) not null,
    transaction_id bigint not null
        constraint fkm5vjjt9jqvf7y69innpgqnipr
            references transactions
            on delete cascade,
    wallet_id bigint not null
        constraint fkm0ki5rav97tn5cp74iqtynpme
            references wallets
            on delete cascade
);

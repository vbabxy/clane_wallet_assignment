--liquibase formatted sql
--changeset vbabxy:100000002
--

INSERT INTO public.kycs (id, created_at, updated_at, level_name, transaction_limit) VALUES (1, '2022-01-30 07:14:28.000000', '2022-01-30 07:14:34.000000', 'KYC0', 0.00);
INSERT INTO public.kycs (id, created_at, updated_at, level_name, transaction_limit) VALUES (2, '2022-01-30 07:15:17.000000', '2022-01-30 07:15:20.000000', 'KYC1', 20000.00);
INSERT INTO public.kycs (id, created_at, updated_at, level_name, transaction_limit) VALUES (3, '2022-01-30 07:15:47.000000', '2022-01-30 07:15:53.000000', 'KYC2', 100000.00);
INSERT INTO public.kycs (id, created_at, updated_at, level_name, transaction_limit) VALUES (4, '2022-01-30 07:16:29.000000', '2022-01-30 07:16:33.000000', 'KYC3', 1000000.00);
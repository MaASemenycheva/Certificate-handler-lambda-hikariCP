INSERT INTO public.state_certificate (certificate_id, member_id, wallet_id, creator_id, created, decoded_transaction_id) VALUES (
'0xf1768e0151d2a23fd7b6a543edbe9fb1572b5cfe',
'test.ru.cbrdc.prt.Iss.111111111-1111-4111-8111-11111111111',
'test.ru.cbrdc.wlt.Iss.111111111-1111-4111-8111-11111111111',
NULL,
'2022-08-27 10:23:04.665023',
'\xe6f8bd79f282ffa567f5c1e5960dcd68bf6ea73169728fc5bb4e617765162526'
);

INSERT INTO public.state_certificate (certificate_id, member_id, wallet_id, creator_id, created, decoded_transaction_id) VALUES (
'0xcdb5d5d41602d92c6c5a7cf09faccffd28d1c7c3',
'test.ru.cbrdc.prt.FI.ed8b233c-773c-40b9-8302-9a0dbbdaf627',
'test.ru.cbrdc.wlt.FI.205ce3b3-9dfd-4c0b-a098-b8bf222757fe',
'test.ru.cbrdc.prt.Oprt.55555555-5555-4555-8555-55555555555',
'2022-08-27 10:23:04.665023',
'\x9e38f48dc9b31f35da77c7381ecaee4b44aca3541b757a6de169362208790443'
);

INSERT INTO public.state_certificate (certificate_id, member_id, wallet_id, creator_id, created, decoded_transaction_id) VALUES (
'0x53abf40afc10b9fdb6b4bcb41c81fe530ef736c9',
'test.ru.cbrdc.prt.Prsn.d4e63a38-841e-4380-9e5a-a59f4e4007fd',
'test.ru.cbrdc.wlt.Clt.7a208d12-9974-4ea3-9db4-b3a1d1vd5c07',
'test.ru.cbrdc.prt.FI.ed8b233c-773c-40b9-8302-9a0dbbdaf627',
'2022-08-27 10:23:04.665023',
'\x98e55af33edcabc073626e5136e6974ac8c64f6ea325ee82c872cde2dd214441'
);


-- error
INSERT INTO public.state_certificate (certificate_id, member_id, wallet_id, creator_id, created, decoded_transaction_id) VALUES (
'0xf1768e0151d2a23fd7b6a543edbe9fb1572b5cfe',
'test.ru.cbrdc.prt.Iss.11111111-1111-4111-8111-11111111111',
'test.ru.cbrdc.wlt.Iss.11111111-1111-4111-8111-11111111111',
NULL,
'2022-08-27 10:23:04.665023',
'\xe6f8bd79f282ffa567f5c1e5960dcd68bf6ea73169728fc5bb4e617765162526'
);
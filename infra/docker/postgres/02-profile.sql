\connect profiledb;

INSERT INTO citizens (
    id,
    subject,
    email,
    first_name,
    last_name,
    street,
    postal_code,
    city,
    country,
    housing_status,
    children_count,
    single_parent
) VALUES (
    '11111111-2222-3333-4444-555555555555',
    'ed633384-afba-4dfa-8fb3-49cf6070affe',
    'citizen@example.com',
    'Fatoumata',
    'Diallo',
    '10 Rue de Lille',
    '59200',
    'Tourcoing',
    'France',
    'TENANT',
    2,
    false
);
INSERT INTO users (tc, fname, lname, password, phonenr, address)
VALUES 
('12345678901', 'Ali', 'Kaya', 'password1', '5551234567', 'Ankara'),
('12345678902', 'Ayşe', 'Yılmaz', 'password2', '5551234568', 'İstanbul'),
('12345678903', 'Mehmet', 'Demir', 'password3', '5551234569', 'İzmir'),
('12345678904', 'Fatma', 'Çelik', 'password4', '5551234570', 'Bursa'),
('12345678905', 'Ahmet', 'Şahin', 'password5', '5551234571', 'Adana'),
('12345678906', 'Emine', 'Öztürk', 'password6', '5551234572', 'Antalya'),
('12345678907', 'Hasan', 'Erdoğan', 'password7', '5551234573', 'Kayseri'),
('12345678908', 'Hülya', 'Kara', 'password8', '5551234574', 'Trabzon'),
('12345678909', 'Cem', 'Güneş', 'password9', '5551234575', 'Konya'),
('12345678910', 'Zeynep', 'Taş', 'password10', '5551234576', 'Mersin'),
('12345678911', 'Burak', 'Aydın', 'password11', '5551234577', 'Gaziantep'),
('12345678912', 'Elif', 'Ekin', 'password12', '5551234578', 'Samsun'),
('12345678913', 'Murat', 'Ak', 'password13', '5551234579', 'Eskişehir'),
('12345678914', 'Derya', 'Bayrak', 'password14', '5551234580', 'Çanakkale'),
('12345678915', 'Serkan', 'Yıldız', 'password15', '5551234581', 'Edirne');

------------------------------------------------------------------------------------

INSERT INTO species (species_name)
VALUES 
('Kedi'),
('Köpek'),
('Kuş'),
('Balık'),
('Hamster'),
('Tavşan'),
('Kaplumbağa'),
('Yılan'),
('Kertenkele'),
('Papağan'),
('İnek'),
('At'),
('Deve'),
('Maymun'),
('Fok');


------------------------------------------------------------------------------------

INSERT INTO breeds (breed_name, species_name)
VALUES
('Van Kedisi', 'Kedi'),
('Sivas Kangalı', 'Köpek'),
('Muhabbet Kuşu', 'Kuş'),
('Japon Balığı', 'Balık'),
('Golden Retriever', 'Köpek'),
('Ankara Kedisi', 'Kedi'),
('Chihuahua', 'Köpek'),
('Amazon Papağanı', 'Papağan'),
('Holstein', 'İnek'),
('Ardennes', 'At'),
('Siyah Tetra', 'Balık'),
('Fırat Yılanı', 'Yılan'),
('Habeş Kedisi', 'Kedi'),
('Dalmaçyalı', 'Köpek'),
('Labrador', 'Köpek');

------------------------------------------------------------------------------------
INSERT INTO pets (pname, species, breed, owner_tc, birthdate)
VALUES
('Maviş', 'Kedi', 'Van Kedisi', '12345678901', '2020-03-15'),
('Karabaş', 'Köpek', 'Sivas Kangalı', '12345678902', '2019-07-20'),
('Boncuk', 'Kuş', 'Muhabbet Kuşu', '12345678903', '2021-05-10'),
('Balık', 'Balık', 'Japon Balığı', '12345678904', '2022-06-25'),
('Leo', 'Köpek', 'Golden Retriever', '12345678905', '2018-09-12'),
('Pamuk', 'Kedi', 'Ankara Kedisi', '12345678906', '2020-11-30'),
('Fıstık', 'Köpek', 'Chihuahua', '12345678907', '2021-01-10'),
('Renkli', 'Papağan', 'Amazon Papağanı', '12345678908', '2019-03-08'),
('Holstein1', 'İnek', 'Holstein', '12345678909', '2015-12-05'),
('Şimşek', 'At', 'Ardennes', '12345678910', '2013-08-01'),
('Neon', 'Balık', 'Siyah Tetra', '12345678911', '2022-10-21'),
('Kıvrım', 'Yılan', 'Fırat Yılanı', '12345678912', '2017-02-11'),
('Ares', 'Kedi', 'Habeş Kedisi', '12345678913', '2018-05-19'),
('Benek', 'Köpek', 'Dalmaçyalı', '12345678914', '2021-07-14'),
('Max', 'Köpek', 'Labrador', '12345678915', '2016-09-22');

------------------------------------------------------------------------------------

INSERT INTO pets (pname, species, breed, owner_tc, birthdate)
VALUES
('Maviş', 'Kedi', 'Van Kedisi', '12345678901', '2020-03-15'),
('Karabaş', 'Köpek', 'Sivas Kangalı', '12345678902', '2019-07-20'),
('Boncuk', 'Kuş', 'Muhabbet Kuşu', '12345678903', '2021-05-10'),
('Balık', 'Balık', 'Japon Balığı', '12345678904', '2022-06-25'),
('Leo', 'Köpek', 'Golden Retriever', '12345678905', '2018-09-12'),
('Pamuk', 'Kedi', 'Ankara Kedisi', '12345678906', '2020-11-30'),
('Fıstık', 'Köpek', 'Chihuahua', '12345678907', '2021-01-10'),
('Renkli', 'Papağan', 'Amazon Papağanı', '12345678908', '2019-03-08'),
('Holstein1', 'İnek', 'Holstein', '12345678909', '2015-12-05'),
('Şimşek', 'At', 'Ardennes', '12345678910', '2013-08-01'),
('Neon', 'Balık', 'Siyah Tetra', '12345678911', '2022-10-21'),
('Kıvrım', 'Yılan', 'Fırat Yılanı', '12345678912', '2017-02-11'),
('Ares', 'Kedi', 'Habeş Kedisi', '12345678913', '2018-05-19'),
('Benek', 'Köpek', 'Dalmaçyalı', '12345678914', '2021-07-14'),
('Max', 'Köpek', 'Labrador', '12345678915', '2016-09-22');

------------------------------------------------------------------------------------

INSERT INTO appointments (pet_id, appointment_date, appointment_type)
VALUES
(1, '2023-02-01 14:30', 'Treatment'),
(2, '2023-02-10 15:00', 'Vaccine'),
(3, '2023-03-01 10:00', 'Treatment'),
(4, '2023-03-15 11:30', 'Treatment'),
(5, '2023-04-01 09:00', 'Vaccine'),
(6, '2023-04-20 16:00', 'Treatment'),
(7, '2023-05-01 10:30', 'Treatment'),
(8, '2023-05-15 11:00', 'Vaccine'),
(9, '2023-06-01 14:00', 'Treatment'),
(10, '2023-06-20 15:30', 'Treatment'),
(11, '2023-07-01 09:30', 'Vaccine'),
(12, '2023-07-15 16:30', 'Treatment'),
(13, '2023-08-01 10:15', 'Treatment'),
(14, '2023-08-20 14:45', 'Vaccine'),
(15, '2023-09-01 13:00', 'Treatment');
------------------------------------------------------------------------------------

INSERT INTO store (product_name, amount, price)
VALUES
('Kedi Maması', 100, 50.00),
('Köpek Maması', 80, 70.00),
('Balık Yemi', 150, 30.00),
('Tasma', 50, 25.00),
('Kedi Kum Kabı', 30, 40.00),
('Köpek Oyuncağı', 60, 35.00),
('Kuş Kafesi', 20, 120.00),
('Kedi Tırmalama Tahtası', 15, 150.00),
('Papağan Yemi', 100, 60.00),
('Kemik', 200, 10.00),
('Su Kabı', 70, 20.00),
('Tarak', 40, 15.00),
('Kedi Şampuanı', 30, 50.00),
('Köpek Şampuanı', 25, 55.00),
('Yılan Kafesi', 10, 200.00);


------------------------------------------------------------------------------------
-- Orders
INSERT INTO orders (tc, order_date, total_price)
VALUES
('12345678901', '2023-01-01', 100.00),
('12345678902', '2023-01-05', 150.00),
('12345678903', '2023-01-10', 80.00),
('12345678904', '2023-01-15', 200.00),
('12345678905', '2023-01-20', 120.00),
('12345678906', '2023-01-25', 90.00),
('12345678907', '2023-01-30', 75.00),
('12345678908', '2023-02-01', 110.00),
('12345678909', '2023-02-05', 140.00),
('12345678910', '2023-02-10', 100.00),
('12345678911', '2023-02-15', 180.00),
('12345678912', '2023-02-20', 160.00),
('12345678913', '2023-02-25', 200.00),
('12345678914', '2023-03-01', 90.00),
('12345678915', '2023-03-05', 50.00);




------------------------------------------------------------------------------------
-- Order Items
INSERT INTO order_items (order_id, product_id, quantity, unit_price)
VALUES
(1, 1, 2, 50.00),
(2, 2, 1, 70.00),
(3, 3, 5, 30.00),
(4, 4, 1, 25.00),
(5, 5, 3, 40.00),
(6, 6, 2, 35.00),
(7, 7, 1, 120.00),
(8, 8, 1, 150.00),
(9, 9, 2, 60.00),
(10, 10, 10, 10.00),
(11, 11, 3, 20.00),
(12, 12, 2, 15.00),
(13, 13, 1, 50.00),
(14, 14, 1, 55.00),
(15, 15, 1, 200.00);


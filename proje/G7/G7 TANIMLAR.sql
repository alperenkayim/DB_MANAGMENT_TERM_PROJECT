CREATE TABLE users (
    tc CHAR(11) PRIMARY KEY,               -- TC kimlik numarası, 11 haneli
    fname VARCHAR(20) NOT NULL,            -- Ad, 50 karaktere kadar
    lname VARCHAR(20) NOT NULL, 		           -- Soyad, 50 karaktere kadar
    password VARCHAR(20) NOT NULL,         -- Şifre, 100 karaktere kadar
    phonenr VARCHAR(12) NOT NULL,          -- Telefon numarası, 15 karaktere kadar
    address TEXT NOT NULL                  -- Adres, metin türü
);

CREATE TABLE species (
    species_name VARCHAR(20) PRIMARY KEY  -- animal_name yalnızca bu sütun ve birincil anahtar olarak tanımlandı
);

CREATE TABLE breeds (
    breed_name VARCHAR(20) PRIMARY KEY,       -- Birincil anahtar
    species_name VARCHAR(20) NOT NULL,                -- Yabancı anahtar
    FOREIGN KEY (species_name) REFERENCES species(species_name) ON DELETE CASCADE -- species tablosuna referans
);


CREATE SEQUENCE pet_id_seq START 1;

CREATE TABLE pets (
    pet_id INT PRIMARY KEY DEFAULT NEXTVAL('pet_id_seq'), -- Sequence kullanılarak pet_id otomatik artacak
    pname VARCHAR(20) NOT NULL,            -- Pet adı, 20 karaktere kadar
    species VARCHAR(20) NOT NULL,          -- Tür adı, 20 karaktere kadar
    breed VARCHAR(20) NOT NULL,            -- Irk adı, 20 karaktere kadar
    owner_tc CHAR(11) NOT NULL,            -- Sahibinin TC kimlik numarası, 11 haneli
    birthdate DATE NOT NULL,               -- Doğum tarihi, tarih formatında
    FOREIGN KEY (owner_tc) REFERENCES users(tc) ON DELETE RESTRICT, -- RESTRICT kısıtlaması eklendi
    FOREIGN KEY (species) REFERENCES species(species_name) ON DELETE CASCADE, -- Tür adı species tablosuna referans
    FOREIGN KEY (breed) REFERENCES breeds(breed_name) ON DELETE CASCADE -- Irk adı breeds tablosuna referans
);

CREATE TABLE vaccine (
    vaccine_id SERIAL PRIMARY KEY,     -- Benzersiz aşı kimliği
    pet_id INT NOT NULL,               -- Aşı yapılan hayvanın kimliği (foreign key ile ilişkilendirilebilir)
    vaccine_date TIMESTAMP,   -- Aşının yapıldığı tarih ve saat
    FOREIGN KEY (pet_id) REFERENCES pets (pet_id) ON DELETE CASCADE -- Hayvan silinirse ilişkili kayıtlar da silinir
);

CREATE TABLE appointments (
    appointment_id SERIAL PRIMARY KEY,  -- appointment_id, otomatik artan bir birincil anahtar
    pet_id INT NOT NULL,                -- pet_id, ilgili pet'in ID'si
    appointment_date TIMESTAMP NOT NULL, -- appointment_date, randevu tarihi ve saati
    appointment_type VARCHAR(10) NOT NULL,                   -- type, randevu türü (örneğin: muayene, aşı vb.)
    FOREIGN KEY (pet_id) REFERENCES pets(pet_id) ON DELETE CASCADE  -- pet_id, pets tablosuna referans verir
);

CREATE TABLE store (
    product_id SERIAL PRIMARY KEY,           -- Benzersiz ürün kimliği
    product_name VARCHAR(30) NOT NULL,     -- Ürün adı
    amount INT NOT NULL,                    -- Ürün miktarı (stokta bulunan)
    price NUMERIC(10, 2) NOT NULL,          -- Ürünün fiyatı
    product_image BYTEA                      -- Ürün resmi (genellikle bir URL veya dosya yolu)
);

CREATE TABLE orders (
    order_id SERIAL PRIMARY KEY,              -- order_id, otomatik artan bir birincil anahtar
    tc VARCHAR(11) NOT NULL,                  -- tc, kullanıcı TC kimlik numarası
    order_date TIMESTAMP NOT NULL,            -- order_date, siparişin tarihi ve saati
    total_price NUMERIC(10, 2) NOT NULL,      -- total_price, siparişin toplam fiyatı (10 basamağa kadar, 2 basamağı ondalıklı)
    FOREIGN KEY (tc) REFERENCES users(tc) ON DELETE CASCADE,  -- tc, users tablosuna referans verir
    CONSTRAINT chk_total_price_positive CHECK (total_price >= 0)  -- Toplam fiyat negatif olamaz
);


CREATE TABLE order_items (
    order_item_id SERIAL PRIMARY KEY,      -- order_item_id, her bir sipariş öğesine benzersiz bir kimlik atar ve otomatik artar
    order_id INT NOT NULL,                  -- order_id, siparişin kimliği (foreign key)
    product_id INT NOT NULL,                -- product_id, ürünün kimliği (foreign key)
    quantity INT NOT NULL,                  -- quantity, sipariş edilen ürün miktarı
    unit_price NUMERIC(10, 2) NOT NULL,     -- unit_price, ürünün birim fiyatı
    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE, -- order_id, orders tablosuna referans verir
    FOREIGN KEY (product_id) REFERENCES store(product_id) ON DELETE RESTRICT -- product_id, store tablosuna referans verir
);


----------------------------------------------------------------------------------




ALTER TABLE pets DROP COLUMN birthdate; --pets tablosundan birthdate sütunu drop edilir


----------------------------------------------------------------------------------


CREATE OR REPLACE FUNCTION create_vaccine_card() -- Bu işlem, her yeni evcil hayvan için bir aşı kartının başlangıçta otomatik olarak oluşturulmasını sağlar.
RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO vaccine (pet_id, vaccine_date)
    VALUES (NEW.pet_id, NULL);
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER after_pet_insert  -- pets tablosuna her yeni bir kayıt eklendiğinde otomatik olarak create_vaccine_card fonksiyonunu çalıştırır
AFTER INSERT ON pets
FOR EACH ROW
EXECUTE FUNCTION create_vaccine_card();


----------------------------------------------------------------------------------



drop table store;  --store tablosunun drop edilmesi

----------------------------------------------------------------------------------


INSERT INTO animals (animal_name) VALUES ('Fishar');

--------------------------------------------------------------------------
CREATE OR REPLACE FUNCTION delete_species_if_no_breeds() 
RETURNS TRIGGER AS $$
BEGIN
    -- Kontrol et: Bu türün başka breed'leri var mı?
    IF NOT EXISTS (SELECT 1 FROM breeds WHERE species_name = OLD.species_name) THEN
        -- Eğer başka breed yoksa, species'i sil
        DELETE FROM species WHERE species_name = OLD.species_name;
    END IF;
    
    RETURN OLD;
END;
$$ LANGUAGE plpgsql;

-- Trigger'ı oluştur
CREATE TRIGGER delete_species_if_no_breeds
AFTER DELETE ON breeds
FOR EACH ROW
EXECUTE FUNCTION delete_species_if_no_breeds();

DROP TRIGGER IF EXISTS delete_species_if_no_breeds ON breeds;
DROP FUNCTION IF EXISTS delete_species_if_no_breeds();



----------------------------------------------------------------------------------


CREATE OR REPLACE FUNCTION check_appointment_limit() --Yeni bir randevu eklenmeden önce, aynı hayvan için aynı türde ve aynı tarihte bir randevunun zaten mevcut olup olmadığını
													  --kontrol eder.

RETURNS TRIGGER AS $$
BEGIN
    -- Aynı hayvan için aynı tür ve aynı tarihte bir randevu var mı kontrol et
    IF EXISTS (
        SELECT 1
        FROM appointments
        WHERE pet_id = NEW.pet_id
          AND appointment_date = NEW.appointment_date
          AND appointment_type = NEW.appointment_type
    ) THEN
        -- Hata mesajı fırlat ve SQLSTATE belirt
        RAISE EXCEPTION 'This pet already has an appointment of this type on the selected date.'
        USING ERRCODE = '45000';
    END IF;

    -- İşleme devam et
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP FUNCTION IF EXISTS check_appointment_limit(); --check_appointment_limit adlı fonksiyonu veritabanından siler.


-- Trigger tanımı
CREATE TRIGGER check_appointment_limit_trigger --appointments tablosuna yeni bir randevu eklenmeden önce, check_appointment_limit fonksiyonunu çalıştırır.
BEFORE INSERT ON appointments
FOR EACH ROW
EXECUTE FUNCTION check_appointment_limit();

DROP TRIGGER IF EXISTS check_appointment_limit ON appointments; --Eğer check_appointment_limit adlı tetikleyici appointments tablosunda tanımlıysa, bu tetikleyici kaldırılır.

----------------------------------------------------------------------------------

CREATE OR REPLACE FUNCTION create_user_pets_view(current_tc VARCHAR(11))
RETURNS VOID AS $$
BEGIN
    EXECUTE format(
        'CREATE OR REPLACE VIEW user_pets_view AS 
         SELECT * FROM pets WHERE owner_tc = %L', 
         current_tc
    );
END;
$$ LANGUAGE plpgsql;


----------------------------------------------------------------------------------

CREATE OR REPLACE FUNCTION create_user_appointments_view(current_tc VARCHAR(11))
RETURNS VOID AS $$
BEGIN
    EXECUTE format(
        'CREATE OR REPLACE VIEW user_appointments_view AS 
         SELECT p.pname, p.species, p.breed, a.appointment_date, a.appointment_type 
         FROM appointments a 
         JOIN pets p ON a.pet_id = p.pet_id 
         WHERE p.owner_tc = %L AND a.appointment_date > NOW()', 
         current_tc
    );
END;
$$ LANGUAGE plpgsql;

----------------------------------------------------------------------------------

CREATE OR REPLACE FUNCTION create_user_vaccines_view(current_tc VARCHAR(11))
RETURNS VOID AS $$
BEGIN
    EXECUTE format(
        'CREATE OR REPLACE VIEW user_vaccines_view AS 
         SELECT p.pname, p.species, p.breed, v.vaccine_date 
         FROM vaccine v 
         JOIN pets p ON v.pet_id = p.pet_id 
         WHERE v.vaccine_date IS NULL AND p.owner_tc = %L 
         UNION 
         SELECT p.pname, p.species, p.breed, v.vaccine_date 
         FROM vaccine v 
         JOIN pets p ON v.pet_id = p.pet_id 
         WHERE v.vaccine_date <= NOW() - INTERVAL ''2 months'' AND p.owner_tc = %L', 
         current_tc, current_tc
    );
END;
$$ LANGUAGE plpgsql;

----------------------------------------------------------------------------------

CREATE OR REPLACE FUNCTION create_user_orders_view(current_tc VARCHAR(11))
RETURNS VOID AS $$
BEGIN
    EXECUTE format(
        'CREATE OR REPLACE VIEW user_orders_view AS 
         SELECT order_id, total_price, order_date 
         FROM orders WHERE tc = %L', 
         current_tc
    );
END;
$$ LANGUAGE plpgsql;

----------------------------------------------------------------------------------
CREATE TYPE pets_d AS (
    pet_id INTEGER,
    pname VARCHAR(20),
    species VARCHAR(20),
    breed VARCHAR(20),
    birthdate DATE
);

CREATE OR REPLACE FUNCTION search_pets_array(search_text VARCHAR)
RETURNS pet_d[] AS
$$
DECLARE
    cur_pets CURSOR FOR 
        SELECT pet_id, pname, species, breed, birthDate
        FROM pets
        WHERE LOWER(CAST(pet_id AS CHAR)) LIKE LOWER('%' || search_text || '%') OR
              LOWER(pname) LIKE LOWER('%' || search_text || '%') OR
              LOWER(species) LIKE LOWER('%' || search_text || '%') OR
              LOWER(breed) LIKE LOWER('%' || search_text || '%') OR
              LOWER(CAST(birthDate AS CHAR)) LIKE LOWER('%' || search_text || '%');
    
    result_array pet_d[] := '{}';  
    i INTEGER := 1; 
BEGIN
    FOR rec IN cur_pets LOOP
        
        result_array := array_append(result_array, NULL::pet_d);
        
      
        result_array[i].pet_id := rec.pet_id;
        result_array[i].pname := rec.pname;
        result_array[i].species := rec.species;
        result_array[i].breed := rec.breed;
        result_array[i].birthdate := rec.birthdate;
        
       
        i := i + 1;
    END LOOP;
    
    RETURN result_array;
END;
$$ LANGUAGE plpgsql;

----------------------------------------------------------------------------------
CREATE TYPE order_d AS (
    order_id INTEGER,
    tc VARCHAR(20),
    order_date DATE,
    total_price NUMERIC
);



CREATE OR REPLACE FUNCTION search_orders_array(search_text VARCHAR)
RETURNS order_d[] AS
$$
DECLARE
    cur_orders CURSOR FOR 
        SELECT order_id, tc, order_date, total_price
        FROM orders
        WHERE LOWER(CAST(order_id AS CHAR)) LIKE LOWER('%' || search_text || '%') OR
              LOWER(tc) LIKE LOWER('%' || search_text || '%') OR
              LOWER(CAST(order_date AS CHAR)) LIKE LOWER('%' || search_text || '%') OR
              LOWER(CAST(total_price AS CHAR)) LIKE LOWER('%' || search_text || '%');
    
    result_array order_d[] := '{}'; 
    i INTEGER := 1; 
BEGIN
    FOR rec IN cur_orders LOOP
        
        result_array := array_append(result_array, NULL::order_d);
        
        
        result_array[i].order_id := rec.order_id;
        result_array[i].tc := rec.tc;
        result_array[i].order_date := rec.order_date;
        result_array[i].total_price := rec.total_price;
        
        
        i := i + 1;
    END LOOP;
    
    RETURN result_array;
END;
$$ LANGUAGE plpgsql;

------------------------------------------------------------------------------------

CREATE TYPE user_d AS (
    tc VARCHAR(20),
    fname VARCHAR(20),
    lname VARCHAR(20),
    phonenr VARCHAR(20),
    address TEXT,
    pet_count INTEGER
);

CREATE OR REPLACE FUNCTION search_users_array(search_text VARCHAR)
RETURNS user_d[] AS
$$
DECLARE
    cur_users CURSOR FOR 
        SELECT 
            u.tc, 
            u.fname, 
            u.lname, 
            u.phonenr, 
            u.address,
            (SELECT COUNT(*) FROM pets p WHERE p.owner_tc = u.tc) AS pet_count
        FROM users u
        WHERE LOWER(tc) LIKE LOWER('%' || search_text || '%') OR 
              LOWER(fname) LIKE LOWER('%' || search_text || '%') OR 
              LOWER(lname) LIKE LOWER('%' || search_text || '%') OR 
              LOWER(phonenr) LIKE LOWER('%' || search_text || '%') OR 
              LOWER(address) LIKE LOWER('%' || search_text || '%');
    
    result_array user_d[] := '{}';  
    i INTEGER := 1; 
BEGIN
    FOR rec IN cur_users LOOP
        
        result_array := array_append(result_array, NULL::user_d);
        
        
        result_array[i].tc := rec.tc;
        result_array[i].fname := rec.fname;
        result_array[i].lname := rec.lname;
        result_array[i].phonenr := rec.phonenr;
        result_array[i].address := rec.address;
        result_array[i].pet_count := rec.pet_count;
        
        
        i := i + 1;
    END LOOP;
    
    RETURN result_array;
END;
$$ LANGUAGE plpgsql;


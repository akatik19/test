
DELETE FROM item_itemdata;
DELETE FROM itemdata;
DELETE FROM box;
DELETE FROM item;


INSERT INTO item (id, name, description, status) VALUES (100, 'Wifi Module', 'Wifi modules for connecting Arduino with Wifi Router', 0);
INSERT INTO item (id, name, description, status) VALUES (101, 'Resistor 5.6k', 'Resistors with 5.6 kOhm', 0);
INSERT INTO item (id, name, description, status) VALUES (102, 'Resistor 3k', 'Resistors with 3 kOhm', 0);
INSERT INTO item (id, name, description, status) VALUES (103, 'Resistor 320Ohm', 'Resistors with 320 Ohm', 0);

INSERT INTO itemdata (id, dkey, dvalue) VALUES (100, 'Frequency', '2.4GHz');
INSERT INTO itemdata (id, dkey, dvalue) VALUES (101, 'Producer', 'life.augmented'); 
INSERT INTO itemdata (id, dkey, dvalue) VALUES (102, 'Operating Temperature min.', '-40°C'); 
INSERT INTO itemdata (id, dkey, dvalue) VALUES (103, 'Operating Temperature max.', '85°C'); 
INSERT INTO itemdata (id, dkey, dvalue) VALUES (104, 'RAM Size', '64 kB'); 
INSERT INTO itemdata (id, dkey, dvalue) VALUES (105, 'Com. Protocol', '802.11 b/g/n'); 

INSERT INTO itemdata (id, dkey, dvalue) VALUES (200, 'Power (Watts)', '0.5W');
INSERT INTO itemdata (id, dkey, dvalue) VALUES (201, 'Tolerance', '5%');  
INSERT INTO itemdata (id, dkey, dvalue) VALUES (202, 'Package', 'Axial Leaded');  


INSERT INTO item_itemdata (Item_id, datasheet_id) VALUES (100, 100);
INSERT INTO item_itemdata (Item_id, datasheet_id) VALUES (100, 101);
INSERT INTO item_itemdata (Item_id, datasheet_id) VALUES (100, 102);
INSERT INTO item_itemdata (Item_id, datasheet_id) VALUES (100, 103);
INSERT INTO item_itemdata (Item_id, datasheet_id) VALUES (100, 104);
INSERT INTO item_itemdata (Item_id, datasheet_id) VALUES (100, 105);

INSERT INTO item_itemdata (Item_id, datasheet_id) VALUES (101, 200);
INSERT INTO item_itemdata (Item_id, datasheet_id) VALUES (101, 201);
INSERT INTO item_itemdata (Item_id, datasheet_id) VALUES (101, 202);

INSERT INTO box (id, amount, item_id) VALUES (0, 10, 100);
INSERT INTO box (id, amount) VALUES (1, 10);
INSERT INTO box (id, amount, item_id) VALUES (2, 10, 101);
INSERT INTO box (id, amount, item_id) VALUES (3, 10, 102);
INSERT INTO box (id, amount) VALUES (4, 10);
INSERT INTO box (id, amount) VALUES (5, 10);
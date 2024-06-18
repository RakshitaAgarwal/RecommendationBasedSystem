CREATE TABLE Menu (
    menu_item_id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    menu_item_name VARCHAR(255) NOT NULL,
    menu_item_price FLOAT NOT NULL,
    is_menu_item_available BOOLEAN NOT NULL,
    last_time_prepared DATE
);
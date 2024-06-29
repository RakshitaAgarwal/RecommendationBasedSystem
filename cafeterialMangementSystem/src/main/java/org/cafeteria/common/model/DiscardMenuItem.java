package org.cafeteria.common.model;

public class DiscardMenuItem {
    private int id;
    private int menuItemId;
    private String name;
    private float price;

    public DiscardMenuItem() {}

    public DiscardMenuItem(int id, int menuItemId, String name, float price) {
        this.id = id;
        this.menuItemId = menuItemId;
        this.name = name;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMenuItemId() {
        return menuItemId;
    }

    public void setMenuItemId(int menuItemId) {
        this.menuItemId = menuItemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "DiscardMenuItem{" +
                "id=" + id +
                ", menuItemId=" + menuItemId +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}
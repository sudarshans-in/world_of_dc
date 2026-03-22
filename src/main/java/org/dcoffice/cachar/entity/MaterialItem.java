package org.dcoffice.cachar.entity;

public class MaterialItem {

    private String name;
    private boolean received;

    public MaterialItem() {}

    public MaterialItem(String name, boolean received) {
        this.name = name;
        this.received = received;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public boolean isReceived() { return received; }
    public void setReceived(boolean received) { this.received = received; }
}

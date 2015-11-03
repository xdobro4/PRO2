package PhoneBoook;

public class Item implements Comparable{
    private String name = null;

    private String address = null;

    private String phone = null;

    public Item(String name, String address, String phone) {
        this.name = name;
        this.address = address;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return this.getName() + ItemsManager.SEPARATOR + this.getAddress() + ItemsManager.SEPARATOR + this.getPhone();
    }

    @Override
    public int compareTo(Object o) {
        Item item = (Item) o;

        return this.toString().compareTo(item.toString());
    }
}

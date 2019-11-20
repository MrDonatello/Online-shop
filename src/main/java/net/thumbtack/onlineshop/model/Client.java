package net.thumbtack.onlineshop.model;

import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class Client extends User {

    private String email;
    private String address;
    private String phone;
    private Deposit deposit;

    public Client() {
    }

    public Client(String firstName, String lastName, String patronymic, String login, String password, int id, Role role, String email, String address, String phone, Deposit deposit) {
        super(firstName, lastName, patronymic, login, password, id, role);
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.deposit = deposit;
    }

    public Deposit getDeposit() {
        return deposit;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setDeposit(Deposit deposit) {
        this.deposit = deposit;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Client)) return false;
        Client client = (Client) o;
        return Objects.equals(getEmail(), client.getEmail()) &&
                Objects.equals(getAddress(), client.getAddress()) &&
                Objects.equals(getPhone(), client.getPhone()) &&
                Objects.equals(getDeposit(), client.getDeposit());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getEmail(), getAddress(), getPhone(), getDeposit());
    }
}

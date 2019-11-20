package net.thumbtack.onlineshop.dto.request;

public class DepositDto {

    private String deposit;
    private int clientId;


    public DepositDto() {
    }

    public String getDeposit() {
        return deposit;
    }

    public void setDeposit(String deposit) {
        this.deposit = deposit;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }
}

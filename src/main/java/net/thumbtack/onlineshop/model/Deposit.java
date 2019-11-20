package net.thumbtack.onlineshop.model;

import org.springframework.stereotype.Component;

@Component
public class Deposit {

    private int deposit;
    // REVU а нужно ли ? В Client нужно иметь поле Deposit, клиент должен знать свой депозит. А депозиту зачем знать клиента ?
    // лучше убрать. Кроме того, если бы это поле было нужно, то оно должно бы выглядеть так - Client client
    // не надо экспонировать структуру РБД в классе
    private int clientId;
    private int version;

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public Deposit(int deposit) {
        this.deposit = deposit;
    }

    public Deposit() {
    }

    public int getDeposit() {
        return deposit;
    }

    public void setDeposit(int deposit) {
        this.deposit = deposit;
    }


    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}

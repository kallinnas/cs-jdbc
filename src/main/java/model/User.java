package model;

import lombok.Data;

@Data
public class User {

    private long id;
    private String email;
    private String password;

    private Client client;

    public User(String email, String password, int role) {
        this.id = 0;
        this.email = email;
        this.password = password;
        if (role == 1) {
            this.client = new Customer();
            this.client.setId(0);
        } else if (role == 2) {
            this.client = new Company();
            this.client.setId(0);
        }
    }

}

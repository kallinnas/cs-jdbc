package model;

import lombok.Data;

import javax.persistence.MappedSuperclass;

@Data
@MappedSuperclass
public abstract class Client {
    private long id;
    public static final long NO_ID = -1;
}

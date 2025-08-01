package dto;

public class User {
    private int Id;
    private String UserName;

    public User(int id, String userName) {
        Id = id;
        UserName = userName;
    }

    public int getId() {
        return Id;
    }

    public String getUserName() {
        return UserName;
    }
}

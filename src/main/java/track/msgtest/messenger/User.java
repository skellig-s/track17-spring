package track.msgtest.messenger;

/**
 *
 */
public class User {
    private long id;
    private String name;
    private String pass;

    public User(String name, String pass) {
        this.name = name;
        this.pass = pass;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getPass() {
        return pass;
    }

    public boolean comp(User other) {
        return  ((this.getName().equals(other.getName())) && (this.getPass().equals(other.getPass())));
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", pass='" + pass + '\'' +
                '}';
    }
}

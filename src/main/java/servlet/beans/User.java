package servlet.beans;

public class User {
    private int id;
    private String email;
    private String password;
    private String message;

    public User() {
        this.email = "";
        this.password = "";
        this.message = "";
        this.id = -1;
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User(int id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isValidated() {
        if (email == null) {
            message = "Email is null";
            return false;
        } else if (!email.matches("\\w+@\\w+\\.\\w+")) { // \\w+ = 1 or more alphanumerics
            message = "Invalid email";
            return false;
        }
        if (password == null) {
            message = "Password is null";
            return false;
        } else if (password.length() < 8) {
            message = "Password must be at least 8 chars";
            return false;
        } else if (password.matches("\\w*\\s+\\w*")) { // 0 or more latins, 1 or more spaces, 0 or more latins
            message = "Invalid password";
            return false;
        }
        else return true;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}

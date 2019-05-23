package backend;

import java.io.*;

class UserData {

    private File userFile;

    private String name, password, another;

    private UserStatus status;

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getAnother() {
        return another;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setAnother(String another) {
        this.another = new String(another);
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    synchronized public void newUser(String name, String password) {
        String userlist = "./UserDatabase/UserList.data";
        try {
            File dbFile = new File(userlist);
            BufferedWriter writer = new BufferedWriter(new FileWriter(dbFile, true));
            writer.write(name + "\n");
            writer.flush();
            writer.close();
        } catch (Exception e) {
            System.out.println("Error! Can't write to UserList");
        }

        this.name = new String(name);
        this.password = new String(password);
        this.another = "?null";
        this.status = UserStatus.NOT_ONLINE;
        String path = "./UserDatabase/" + name + ".data";
        userFile = new File(path);
        push();
    }

    public void setUser(String name) {
        String path = "./UserDatabase/" + name + ".data";
        userFile = new File(path);
        pull();
    }

    public void pull() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(userFile));
            name = new String(reader.readLine());
            password = new String(reader.readLine());
            String tempString = reader.readLine();
            status = UserStatus.fromTypeName(tempString);
            another = new String(reader.readLine());
            reader.close();
        } catch (Exception e) {
            System.out.println("Error! Can't link to UserData");
        }
    }

    synchronized public void push() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(userFile));
            writer.write(name + "\n");
            writer.write(password + "\n");
            writer.write(status.getTypeName() + "\n");
            writer.write(another + "\n");
            writer.flush();
            writer.close();
        } catch (Exception e) {
            System.out.println("Error! Can't write to UserData");
        }
    }
}

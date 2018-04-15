package LOLIBRARY.bean;

public class User {
    private String password;
    private  String name;
    private int id;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public String getPassword() {
        return password;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String GetAnonymousName(){
        if(name==null){
            return null;
        }
        if(name.length()<=1){
            return "*";
        }
        if(name.length()==2){
            return name.substring(0,1)+"*";
        }
        char[] cs =name.toCharArray();
        for(int i=1;i<cs.length-1;++i){
            cs[i]='*';
        }
        return new String(cs);
    }
}

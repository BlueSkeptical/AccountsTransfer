package sample.accounts;

import java.util.Objects;

public class OwnerName {
    public final String firstName;
    public final String secondName;
    
    public OwnerName(String firstName, String secondName) {
        this.firstName = Objects.requireNonNull(firstName);
        this.secondName = Objects.requireNonNull(secondName);
    }
            
}

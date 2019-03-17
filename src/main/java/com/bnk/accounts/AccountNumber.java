package com.bnk.accounts;


public class AccountNumber {

    public AccountNumber(long num) {
        this.num = num;
    }
    
    @Override
    public String toString() {
        return Long.toString(num);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AccountNumber other = (AccountNumber) obj;
        if (this.num != other.num) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + (int) (this.num ^ (this.num >>> 32));
        return hash;
    }
    public final long num;
    

    
    
}

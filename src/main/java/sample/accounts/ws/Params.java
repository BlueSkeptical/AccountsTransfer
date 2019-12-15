package sample.accounts.ws;

public class  Params {
    private final Param[] params;

    public Params(Param... params) {
        this.params = params;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        for(int i= 0; i< params.length; i++) {
            sb.append(i==0 ? "" : "&");
            sb.append(params[i].name + "=" + params[i].value.toString());
        }
        return sb.toString();
    }

    public boolean isEmpty() {
        return params.length == 0;
    }
}
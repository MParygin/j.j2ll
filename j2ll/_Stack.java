package j2ll;

/**
 * Stack
 */
public class _Stack {

    int ord = 0;
    int[] names = new int[256];
    long[] imm = new long[256];
    boolean[] bimm = new boolean[256];
    int pos = 0;

    _Stack() {

    }

    public String push() {
        int v = ord;
        this.names[pos] = ord;
        this.imm[pos] = 0;
        this.bimm[pos] = false;
        ord++;
        pos++;
        return "%stack" + v;
    }

    public void push(long imm) {
        //int v = ord;
        this.names[pos] = ord;
        this.imm[pos] = imm;
        this.bimm[pos] = true;
        pos++;
    }

    public String pop() {
        if (pos == 0) return "Understack";
        pos--;
        if (this.bimm[pos]) return Long.toString(this.imm[pos]);
        return "%stack" + names[pos];
    }

}

package j2ll;

import java.util.ArrayList;
import java.util.List;
import static j2ll.Internals.*;

public final class Util {

    public static String javaSignature2irType(String str) {
        if (str.startsWith("[")) {
            return Internals.arrayOf(javaSignature2irType(str.substring(1)));
        }

        if (str.equals("B")) {
            return BYTE;
        } else if (str.equals("S")) {
            return SHORT;
        } else if (str.equals("C")) {
            return CHAR;
        } else if (str.equals("I")) {
            return INT;
        } else if (str.equals("J")) {
            return LONG;
        } else if (str.equals("F")) {
            return FLOAT;
        } else if (str.equals("D")) {
            return DOUBLE;
        } else if (str.equals("V")) {
            return "void";
        }
        return null;
    }

    @Deprecated
    public static List<String> javaSignatures2irTypes(String str) {
        List<String> result = new ArrayList<String>();
        for (char $ : str.toCharArray()) {
            if ($ == 'I') {
                result.add("i32");
            } else if ($ == 'J') {
                result.add("i64");
            } else if ($ == 'F') {
                result.add("float");
            } else if ($ == 'D') {
                result.add("double");
            } else if ($ == 'S') {
                result.add("i16");
            } else if ($ == 'V') {
                result.add("void");
            }
        }
        return result;
    }

    public static String enumArgs(List<String> types, String prefix) {
        StringBuilder tmp = new StringBuilder();
        int index = 0;
        for (int i = 0; i < types.size(); i++) {
            if (i != 0) tmp.append(", ");
            String type = types.get(i);
            tmp.append(type);
            tmp.append(" ");
            tmp.append(prefix);
            tmp.append(index++);
            if ("i64".equals(type) || "double".equals(type)) index++; // long & double have 2 slots
        }
        return tmp.toString();
    }

    public static String classMethodSignature2id(String className, String methodName, _JavaSignature signature) {
        StringBuilder result = new StringBuilder();
        result.append('"');
        result.append(className);
        result.append(':');
        result.append(methodName);
        result.append('_');
        result.append(signature.getJavaArgs());
        result.append('"');
        return result.toString();
    }

}

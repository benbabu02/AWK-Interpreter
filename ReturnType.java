/*
 * ReturnType Class which contains return enums: Normal, Break, Continue, and Return.
 * This class stores a return enum as well as a string if one is provided.
 */
public class ReturnType {

    enum ReturnEnums {
        Normal, Break, Continue, Return
    }

    ReturnEnums returnEnum;
    String str;

    public ReturnType(ReturnEnums returnEnum) {
        this.returnEnum = returnEnum;
    }

    public ReturnType(ReturnEnums returnEnum, String inputStr) {
        this.returnEnum = returnEnum;
        this.str = inputStr;
    }

    ReturnEnums getReturnEnum() {
        return returnEnum;
    }

    String getStr() {
        return str;
    }

    @Override
    public String toString() {
        if (str != null) {
            return "ReturnType " + returnEnum + " " + str;
        } else {
            return "ReturnType " + returnEnum;
        }
    }
}

package smartrics.rest.test.fitnesse.fixture;

import fit.ColumnFixture;

public class ColumnFixtureTest extends ColumnFixture {
    public String returnCode;

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public boolean isSuccess() {
        final int i = Integer.parseInt(returnCode);
        return i >= 200 && i <300;
    }

}

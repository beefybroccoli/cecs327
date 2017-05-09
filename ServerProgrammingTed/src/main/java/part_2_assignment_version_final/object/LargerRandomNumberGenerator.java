package part_2_assignment_version_final.object;

import java.math.BigInteger;

/**
 * generate the next larger random number
 */
public class LargerRandomNumberGenerator {

    private BigInteger mValue = BigInteger.valueOf(0);

    private void reset() {
        mValue = BigInteger.valueOf(1);
    }

    /**
     * @return the next larger random value
     */
    public String getNextLargerRand() {
        try {
            mValue = mValue.add(BigInteger.valueOf(VALUE.getRandomNumberBetween(1000, 1)));
        } catch (OutOfMemoryError e) {
            reset();
        }
        return mValue.toString();
    }
}

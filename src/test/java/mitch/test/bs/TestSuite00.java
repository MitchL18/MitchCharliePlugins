package mitch.test.bs;

import mitch.test.bs.invalid.*;
import mitch.test.bs.section1.Test_12_2_00;
import mitch.test.bs.section1.Test_12_6_00;
import mitch.test.bs.section1.Test_12_7_00;
import mitch.test.bs.section1.Test_20_A_00;
import mitch.test.bs.section2.Test_11_2_00;
import mitch.test.bs.section2.Test_11_7_00;
import mitch.test.bs.section2.Test_8_6_00;
import mitch.test.bs.section2.Test_8_A_00;
import mitch.test.bs.section3.Test_A2_7_00;
import mitch.test.bs.section3.Test_A7_6_00;
import mitch.test.bs.section3.Test_A8_A_00;
import mitch.test.bs.section3.Test_A9_2_00;
import mitch.test.bs.section4.Test_1010_A_00;
import mitch.test.bs.section4.Test_22_6_00;
import mitch.test.bs.section4.Test_22_A_00;
import mitch.test.bs.section4.Test_AA_2_00;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        Test6CardHand.class,
        TestBlackJackHand.class,
        TestBustedHand.class,
        TestNullCard.class,
        TestNullHand.class,

        Test_12_2_00.class,
        Test_12_6_00.class,
        Test_12_7_00.class,
        Test_20_A_00.class,

        Test_8_6_00.class,
        Test_8_A_00.class,
        Test_11_2_00.class,
        Test_11_7_00.class,

        Test_A2_7_00.class,
        Test_A7_6_00.class,
        Test_A8_A_00.class,
        Test_A9_2_00.class,

        Test_22_6_00.class,
        Test_22_A_00.class,
        Test_1010_A_00.class,
        Test_AA_2_00.class
})


public class TestSuite00 {
}

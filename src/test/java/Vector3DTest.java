import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import API.Math.ADT.Vector3D;
import API.Math.ADT.Vector3dInterface;

import static java.lang.Double.NaN;
import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static org.junit.jupiter.api.Assertions.*;

class Vector3DTest {

    @Test
    @DisplayName("TestConstructors")
    void Constructor() {
        Vector3dInterface v = new Vector3D();
        Vector3dInterface v1 = new Vector3D(0, 0, 0);
        assertEquals(v, v1);
    }

    @Test
    @DisplayName("TestDist")
    void Dist() {
        Vector3dInterface a = new Vector3D(3.0, 4.0, 8.0);
        Vector3dInterface b = new Vector3D(0.5, 0.25, 0.5);
        assertEquals(8.75, a.dist(b));
        assertEquals(8.75, Vector3D.dist(a, b));
    }

    @Test
    @DisplayName("TestUnitVectorDistance")
    void UnitVectorDistance() {
        Vector3dInterface a = new Vector3D(3.0, 4.0, 8.0);
        Vector3dInterface b = new Vector3D(0.5, 0.25, 0.5);
        assertEquals(8.75, b.sub(a).norm());
        Vector3dInterface expected = new Vector3D(-0.2857142857142857, -0.42857142857142855, -0.8571428571428571);
        Vector3dInterface result = Vector3D.unitVectorDistance(a, b);
        assertAll(
                () -> assertTrue(() -> expected.sub(result).getX() < 1e-8),
                () -> assertTrue(() -> expected.sub(result).getY() < 1e-8),
                () -> assertTrue(() -> expected.sub(result).getZ() < 1e-8));
    }

    @Test
    @DisplayName("Dot")
    void Dot() {
        Vector3dInterface a = new Vector3D(3.0, 4.0, 8.0);
        Vector3dInterface b = new Vector3D(0.5, 0.25, 0.5);
        assertEquals(6.5, Vector3D.dot(a, b));
    }

    @Test
    @DisplayName("Cross")
    void Cross() {
        Vector3dInterface a = new Vector3D(3.0, 4.0, 8.0);
        Vector3dInterface b = new Vector3D(0.5, 0.25, 0.5);
        assertEquals(new Vector3D(0, 2.5, -1.25), Vector3D.cross(a, b));
    }

    @Test
    @DisplayName("Normalize")
    void Normalize() {
        Vector3dInterface a = new Vector3D(-2.5, -3.75, -7.5);
        Vector3dInterface expected = new Vector3D(-0.2857142857142857, -0.42857142857142855, -0.8571428571428571);
        Vector3dInterface result = Vector3D.normalize(a);
        assertAll(
                () -> assertTrue(() -> expected.sub(result).getX() < 1e-8),
                () -> assertTrue(() -> expected.sub(result).getY() < 1e-8),
                () -> assertTrue(() -> expected.sub(result).getZ() < 1e-8));
    }

    @Test
    @DisplayName("AbsSub")
    void AbsSub() {
        Vector3dInterface test = new Vector3D(-10, 10, -10), testInvert = test.mul(0);
        Vector3dInterface expected = new Vector3D(), result = (test.absSub(testInvert));
        assertAll(
                () -> assertTrue(() -> expected.sub(result).getX() < 1e-8),
                () -> assertTrue(() -> expected.sub(result).getY() < 1e-8),
                () -> assertTrue(() -> expected.sub(result).getZ() < 1e-8));
    }

    @Test
    @DisplayName("TestDiv")
    void TestDiv() {
        Vector3dInterface a = new Vector3D(-1.1, 0.1, 1.1);
        Vector3dInterface b = a.div(0.5);
        assertEquals(-1.1 / 0.5, b.getX());
        assertEquals(0.1 / 0.5, b.getY());
        assertEquals(1.1 / 0.5, b.getZ());
    }

    @Test
    @DisplayName("TestDiv")
    void TestDivVector() {
        Vector3dInterface a = new Vector3D(-1.1, 0.1, 1.1);
        Vector3dInterface dividend = new Vector3D(0.5, 0.5, 0.5);
        Vector3dInterface b = a.div(dividend);
        assertEquals(-1.1 / 0.5, b.getX());
        assertEquals(0.1 / 0.5, b.getY());
        assertEquals(1.1 / 0.5, b.getZ());
    }

    @Test
    @DisplayName("TestClone")
    void TestClone() {
        Vector3dInterface test = new Vector3D(1.1, 1.0, -1.0);
        Vector3dInterface clone = test.clone();
        clone = clone.mul(NaN);
        assertEquals(1.1, test.getX());
        assertEquals(1.0, test.getY());
        assertEquals(-1.0, test.getZ());
    }

    @Test
    @DisplayName("Heading")
    void Heading() {
        Vector3dInterface test = new Vector3D(1.1, 1.0, -1.0);
        assertTrue(() -> abs((0.7378150601204649 * (180 / PI)) - test.heading()) < 1e-8);
    }

    @Test
    @DisplayName("IsZero")
    void IsZero() {
        Vector3dInterface zero = new Vector3D();
        Vector3dInterface nonZero = new Vector3D(1, 1, 1);
        Vector3dInterface nonZeroYZ = new Vector3D(0, 1, 1);
        Vector3dInterface nonZeroZ = new Vector3D(0, 0, 1);

        assertTrue(zero::isZero);
        assertFalse(nonZero::isZero);
        assertFalse(nonZeroYZ::isZero);
        assertFalse(nonZeroZ::isZero);
    }

    @Test
    @DisplayName("TestHashCode")
    void TestHashCode() {
        Vector3dInterface zero = new Vector3D();
        Vector3dInterface nonZero = new Vector3D(1, 1, 1);
        assertEquals(new Vector3D(0, 0, 0).hashCode(), zero.hashCode());
        assertNotEquals(nonZero.hashCode(), zero.hashCode());
    }

    @Test
    @DisplayName("TestEquals")
    void TestEquals() {
        Vector3dInterface zero = new Vector3D();
        Vector3dInterface nonZero = new Vector3D(1, 1, 1);
        Vector3dInterface nonZeroYZ = new Vector3D(0, 1, 1);
        Vector3dInterface nonZeroZ = new Vector3D(0, 0, 1);
        assertEquals(new Vector3D(0, 0, 0), zero);
        assertNotEquals(nonZero, zero);
        assertFalse(zero.equals(nonZero));
        assertFalse(zero.equals(nonZeroYZ));
        assertFalse(zero.equals(nonZeroZ));
        assertFalse(zero.equals(new Object()));
    }

    @Test
    @DisplayName("TestGetX")
    void testGetX() {
        Vector3dInterface v = new Vector3D(-1.1, 0.1, 1.1);
        assertEquals(-1.1, v.getX());
    }

    @Test
    @DisplayName("TestSetX")
    void testSetX() {
        Vector3dInterface v = new Vector3D();
        v.setX(-1.1);
        assertEquals(-1.1, v.getX());
    }

    @Test
    @DisplayName("TestGetY")
    void testGetY() {
        Vector3dInterface v = new Vector3D(-1.1, 0.1, 1.1);
        assertEquals(0.1, v.getY());
    }

    @Test
    @DisplayName("TestSetY")
    void testSetY() {
        Vector3dInterface v = new Vector3D();
        v.setY(0.1);
        assertEquals(0.1, v.getY());
    }

    @Test
    @DisplayName("TestGetZ")
    void testGetZ() {
        Vector3dInterface v = new Vector3D(-1.1, 0.1, 1.1);
        assertEquals(1.1, v.getZ());
    }

    @Test
    @DisplayName("TestSetZ")
    void testSetZ() {
        Vector3dInterface v = new Vector3D();
        v.setZ(1.0);
        assertEquals(1.0, v.getZ());
    }

    @Test
    @DisplayName("TestAddVector")
    void testAddVector3d() {
        Vector3dInterface a = new Vector3D(-1.1, 0.1, 1.1);
        Vector3dInterface b = new Vector3D(0.5, 0.6, 0.7);
        Vector3dInterface ab = a.add(b);
        assertEquals(-1.1 + 0.5, ab.getX());
        assertEquals(0.1 + 0.6, ab.getY());
        assertEquals(1.1 + 0.7, ab.getZ());
    }

    @Test
    @DisplayName("TestSubVector")
    void testSub() {
        Vector3dInterface a = new Vector3D(-1.1, 0.1, 1.1);
        Vector3dInterface b = new Vector3D(0.5, 0.6, 0.7);
        Vector3dInterface ab = a.sub(b);
        assertEquals(-1.1 - 0.5, ab.getX());
        assertEquals(0.1 - 0.6, ab.getY());
        assertEquals(1.1 - 0.7, ab.getZ());
    }

    @Test
    @DisplayName("TestMulVector")
    void testMul() {
        Vector3dInterface a = new Vector3D(-1.1, 0.1, 1.1);
        Vector3dInterface b = a.mul(0.5);
        assertEquals(-1.1 * 0.5, b.getX());
        assertEquals(0.1 * 0.5, b.getY());
        assertEquals(1.1 * 0.5, b.getZ());
    }

    @Test
    @DisplayName("TestAddMulVector")
    void testAddMul() {
        Vector3dInterface a = new Vector3D(0.6, 0.7, 0.8);
        Vector3dInterface b = new Vector3D(-1.1, 0.1, 1.1);
        Vector3dInterface ab = a.addMul(0.5, b);
        assertEquals(0.6 + 0.5 * (-1.1), ab.getX());
        assertEquals(0.7 + 0.5 * 0.1, ab.getY());
        assertEquals(0.8 + 0.5 * 1.1, ab.getZ());
    }

    @Test
    @DisplayName("TestVectorNorm")
    void testNorm() {
        Vector3dInterface v = new Vector3D(3.0, -2.0, 6.0);
        assertEquals(7.0, v.norm());
    }

    @Test
    void testToString() {
        Vector3dInterface v = new Vector3D(-1.1, 2.1, -3.1);
        String stringV = "(-1.1,2.1,-3.1)";
        assertEquals(stringV, v.toString());
    }

}
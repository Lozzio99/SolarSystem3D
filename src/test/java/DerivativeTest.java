import API.Math.Functions.FirstDerivative;
import API.Math.Functions.SecondDerivative;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import API.Math.ADT.Vector3D;
import API.Math.ADT.Vector3dInterface;

import static java.lang.Math.abs;
import static java.lang.Math.pow;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * This class is a Test Class for First and Second Derivative Centered Formulas
 *  it uses the function f(x) = 1/(1+x^2),to approximate f'(2) and f''(2);
 */
class DerivativeTest {
    final double exactVal = -0.16;
    final double exactVal2 =0.176;
    double x,h;
    Vector3dInterface f2hx,fhx,fx,fxh,fx2h;

    static Vector3dInterface f(double x){
        return new Vector3D(1/(1+pow(x,2)),0,0);
    }
    @BeforeEach
    void setup() {
        h = 0.1;
        x = 2;
        fx = f(x);
        f2hx = f(x - 2 * h);
        fx2h = f(x + 2 * h);
        fxh = f(x + h);
        fhx = f(x - h);
    }

    @Test
    @DisplayName("5P_Centered FirstDerivativeTest")
    void _5PCentered1(){
        Vector3dInterface v =  FirstDerivative._5PCentredStep(h,f2hx,fhx,fxh,fx2h);
        System.out.println(abs(v.getX()-exactVal));
        assertTrue(abs(v.getX()-exactVal)<1e-5);
    }
    @Test
    @DisplayName("3P_Centered SecondDerivativeTest")
    void _3PCentered2(){
        Vector3dInterface v =  SecondDerivative._3PCentredStep(h,fhx,fx,fxh);
        System.out.println(abs(v.getX()-exactVal2));
        assertTrue(abs(v.getX()-exactVal2)<3e-4);
    }
}
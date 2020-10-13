
package mnm.bcs106.yoobeecolleges.lifeofdragon;
import androidx.annotation.NonNull;

//**************************************************************************************************
//Math class for 2d vector calculations
//**************************************************************************************************


public class Vector2 {
    public static Vector2 zero = new Vector2(0,0);
    public static Vector2 right = new Vector2(1,0);
    public static Vector2 down = new Vector2(0,1);
    public static Vector2 left = new Vector2(-1,0);
    public static Vector2 up = new Vector2(0,-1);
    public static Vector2 one = new Vector2(1,1);

    public float x,y;
    private float length;
    private Vector2 normal;

    public Vector2(float x, float y){
        this.x = x;
        this.y = y;
    }

    public Vector2 getPerp(){
        return new Vector2(-y,x);
    }

    public float toDegrees(){
        return (float)Math.toDegrees(Math.atan2(y,x));
    }
    public Vector2 getNormal(){
        getLength();
        if(length > 0) {
            normal = new Vector2(x / length, y / length);
        }
        else {
            normal = new Vector2(0, 0);
        }
        return normal;
    }

    public float getLength(){
        length = (float)Math.sqrt(x*x + y*y);

        return length;
    }

    public float sqrMagnitude(){
        return x*x + y*y;
    }

    //Multplication with a scalar value
    public Vector2 multiply(float a){

        return new Vector2(a*x,a*y);
    }

    //Element by element multiplication with another vector
    public Vector2 multiply(Vector2 v){

        return new Vector2(v.x*x,v.y*y);
    }

    //Dot multiplication / projection onto another vector
    public float dot(Vector2 v){
        return v.x*x + v.y*y;
    }

    //Vector addition and subtraction
    public Vector2 add(Vector2 v){
        return new Vector2(x+v.x,y+v.y);
    }
    public Vector2 sub(Vector2 v){
        return new Vector2(x-v.x,y-v.y);
    }


    //Return a random vector with elements of range -1 to 1
    public static Vector2 getRandom(){
        return new Vector2((float)(Math.random()-0.5f)*2,(float)(Math.random()-0.5f)*2);
    }

    //Calculate the distance between two vectors
    public static float distance(Vector2 v, Vector2 u){
        float dx = v.x - u.x;
        float dy = v.y - u.y;

        return (float)Math.sqrt(dx*dx+dy*dy);
    }

    //Rotate a vector by degrees
    public static Vector2 rotate(Vector2 v, float degrees){
        double rad = Math.toRadians(degrees);
        float cos = (float)Math.cos(rad);
        float sin = (float)Math.sin(rad);
        return new Vector2(cos*v.x - sin*v.y, cos*v.y+sin*v.x);
    }

    @NonNull
    @Override
    public String toString() {
        super.toString();
        return "("+x+", "+y+")";
    }

    //R = V - 2*N (V*N)
    public static Vector2 reflection(Vector2 v, Vector2 n){
        return v.sub(n.multiply(2*v.dot(n)));
    }
    public Vector2 inverse(){
        return new Vector2(y,x);
    }
}
